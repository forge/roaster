/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.core.util.CodeSnippetParsingUtil;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.roaster.model.impl.JavaAnnotationImpl;
import org.jboss.forge.roaster.model.impl.JavaClassImpl;
import org.jboss.forge.roaster.model.impl.JavaEnumImpl;
import org.jboss.forge.roaster.model.impl.JavaInterfaceImpl;
import org.jboss.forge.roaster.model.impl.JavaPackageInfoImpl;
import org.jboss.forge.roaster.model.impl.JavaUnitImpl;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.JDTOptions;

/**
 * The default implementation of a {@link JavaParser}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaParserImpl implements JavaParser
{

   @Override
   public JavaUnit parseUnit(final String data)
   {
      Document document = new Document(data);
      ASTParser parser = ASTParser.newParser(AST.JLS11);

      parser.setSource(document.get().toCharArray());
      parser.setCompilerOptions(JDTOptions.getJDTOptions());

      parser.setResolveBindings(true);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      CompilationUnit unit = (CompilationUnit) parser.createAST(null);
      unit.recordModifications();

      TypeDeclarationFinderVisitor visitor = new TypeDeclarationFinderVisitor();
      unit.accept(visitor);

      List<AbstractTypeDeclaration> declarations = visitor.getTypeDeclarations();
      List<JavaType<?>> types = new ArrayList<>();
      if (!declarations.isEmpty())
      {
         for (AbstractTypeDeclaration declaration : declarations)
         {
            if (declaration.isPackageMemberTypeDeclaration())
            {
               types.add(getJavaSource(null, document, unit, declaration));
            }
         }
         return new JavaUnitImpl(types);
      }
      else if (visitor.getPackageDeclaration() != null)
      {
         types.add(getJavaSource(null, document, unit, visitor.getPackageDeclaration()));
         return new JavaUnitImpl(types);
      }
      else
      {
         throw new ParserException("Could not find type declaration in Java source - is this actually code?");
      }
   }

   /**
    * Create a {@link JavaType} instance from the given {@link Document}, {@link CompilationUnit},
    * {@link TypeDeclaration}, and enclosing {@link JavaType} type.
    * 
    * @param enclosingType the enclosing type of the new java type
    * @param document the document of the new java type
    * @param unit the compilation unit of the new java type
    * @param declaration the AST node of the new java type
    * @return a new java type instance with the provided parameters
    * @throws ParserException if the java type in the {@code declaration} is unknown
    */
   public static JavaSource<?> getJavaSource(JavaSource<?> enclosingType, Document document, CompilationUnit unit,
            ASTNode declaration)
   {
      if (declaration instanceof TypeDeclaration)
      {
         TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
         if (typeDeclaration.isInterface())
         {
            return new JavaInterfaceImpl(enclosingType, document, unit, typeDeclaration);
         }
         return new JavaClassImpl(enclosingType, document, unit, typeDeclaration);
      }
      else if (declaration instanceof EnumDeclaration)
      {
         EnumDeclaration enumDeclaration = (EnumDeclaration) declaration;
         return new JavaEnumImpl(enclosingType, document, unit, enumDeclaration);
      }
      else if (declaration instanceof AnnotationTypeDeclaration)
      {
         AnnotationTypeDeclaration annotationTypeDeclaration = (AnnotationTypeDeclaration) declaration;
         return new JavaAnnotationImpl(enclosingType, document, unit, annotationTypeDeclaration);
      }
      else if (declaration instanceof PackageDeclaration)
      {
         PackageDeclaration packageDeclaration = (PackageDeclaration) declaration;
         return new JavaPackageInfoImpl(enclosingType, document, unit, packageDeclaration);
      }
      else
      {
         throw new ParserException("Unknown Java source type [" + declaration + "]");
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends JavaSource<?>> T create(final Class<T> type)
   {
      if (type.isAssignableFrom(JavaClassSource.class))
         return (T) parseUnit("public class JavaClass { }").getGoverningType();

      if (type.isAssignableFrom(JavaEnumSource.class))
         return (T) parseUnit("public enum JavaEnum { }").getGoverningType();

      if (type.isAssignableFrom(JavaAnnotationSource.class))
         return (T) parseUnit("public @interface JavaAnnotation { }").getGoverningType();

      if (type.isAssignableFrom(JavaInterfaceSource.class))
         return (T) parseUnit("public interface JavaInterface { }").getGoverningType();

      if (type.isAssignableFrom(JavaPackageInfoSource.class))
         return (T) parseUnit("package org.example;").getGoverningType();

      return null;
   }

   @Override
   public List<Problem> validateSnippet(String snippet)
   {
      CodeSnippetParsingUtil codeSnippetParsingUtil = new CodeSnippetParsingUtil(false);
      ConstructorDeclaration constructorDeclaration = codeSnippetParsingUtil.parseStatements(snippet.toCharArray(), 0,
               snippet.length(), JDTOptions.getJDTOptions(), true, false);
      CompilationResult compilationResult = constructorDeclaration.compilationResult();
      List<Problem> problems = new ArrayList<>();
      if (compilationResult.hasErrors())
      {
         for (CategorizedProblem problem : compilationResult.getErrors())
         {
            Problem p = new Problem(problem.getMessage(),
                     problem.getSourceStart(),
                     problem.getSourceEnd(),
                     problem.getSourceLineNumber());
            problems.add(p);
         }
      }
      return problems;
   }
}