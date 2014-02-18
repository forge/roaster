/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.roaster.model.impl.JavaAnnotationImpl;
import org.jboss.forge.roaster.model.impl.JavaClassImpl;
import org.jboss.forge.roaster.model.impl.JavaEnumImpl;
import org.jboss.forge.roaster.model.impl.JavaInterfaceImpl;
import org.jboss.forge.roaster.model.impl.JavaPackageInfoImpl;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.JavaParser;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaParserImpl implements JavaParser
{

   @Override
   public JavaType<?> parse(final InputStream data)
   {
      try
      {
         char[] source = Util.getInputStreamAsCharArray(data, data.available(), "ISO8859_1");
         return parse(new String(source));
      }
      catch (IOException e)
      {
         return null;
      }
      finally
      {
         Streams.closeQuietly(data);
      }
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private JavaType<?> parse(final String data)
   {
      Document document = new Document(data);
      ASTParser parser = ASTParser.newParser(AST.JLS4);

      parser.setSource(document.get().toCharArray());
      Map options = JavaCore.getOptions();
      options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_7);
      options.put(CompilerOptions.OPTION_Encoding, "UTF-8");
      parser.setCompilerOptions(options);

      parser.setResolveBindings(true);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      CompilationUnit unit = (CompilationUnit) parser.createAST(null);
      unit.recordModifications();

      TypeDeclarationFinderVisitor visitor = new TypeDeclarationFinderVisitor();
      unit.accept(visitor);

      List<AbstractTypeDeclaration> declarations = visitor.getTypeDeclarations();
      if (!declarations.isEmpty())
      {
         AbstractTypeDeclaration declaration = declarations.get(0);
         return getJavaSource(null, document, unit, declaration);
      }
      else if (visitor.getPackageDeclaration() != null)
      {
         return getJavaSource(null, document, unit, visitor.getPackageDeclaration());
      }
      throw new ParserException("Could not find type declaration in Java source - is this actually code?");
   }

   /**
    * Create a {@link JavaType} instance from the given {@link Document}, {@link CompilationUnit},
    * {@link TypeDeclaration}, and enclosing {@link JavaType} type.
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
         else
         {
            return new JavaClassImpl(enclosingType, document, unit, typeDeclaration);
         }
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
      if (type != null)
      {
         if (type.isAssignableFrom(JavaClassSource.class))
            return (T) parse("public class JavaClass { }");
   
         if (type.isAssignableFrom(JavaEnumSource.class))
            return (T) parse("public enum JavaEnum { }");
   
         if (type.isAssignableFrom(JavaAnnotationSource.class))
            return (T) parse("public @interface JavaAnnotation { }");
   
         if (type.isAssignableFrom(JavaInterfaceSource.class))
            return (T) parse("public interface JavaInterface { }");
   
         if (type.isAssignableFrom(JavaPackageInfoSource.class))
            return (T) parse("package org.example;");
      }
      return null;
   }

}
