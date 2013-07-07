/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.spi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.jboss.forge.parser.ParserException;
import org.jboss.forge.parser.java.JavaAnnotation;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaInterface;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.parser.java.impl.JavaAnnotationImpl;
import org.jboss.forge.parser.java.impl.JavaClassImpl;
import org.jboss.forge.parser.java.impl.JavaEnumImpl;
import org.jboss.forge.parser.java.impl.JavaInterfaceImpl;
import org.jboss.forge.parser.java.impl.JavaPackageInfoImpl;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaParserImpl implements JavaParserProvider
{

   @Override
   public JavaSource<?> parse(final File file) throws FileNotFoundException
   {
      FileInputStream stream = null;
      try
      {
         stream = new FileInputStream(file);
         return parse(new BufferedInputStream(stream));
      }
      finally
      {
         Streams.closeQuietly(stream);
      }
   }

   @Override
   public JavaSource<?> parse(final InputStream data)
   {
      try
      {
         char[] source = Util.getInputStreamAsCharArray(data, data.available(), "ISO8859_1");
         return parse(source);
      }
      catch (IOException e)
      {
         throw new IllegalArgumentException("InputStream must be a parsable java file: ", e);
      }
      finally
      {
         Streams.closeQuietly(data);
      }
   }

   @Override
   public JavaSource<?> parse(final char[] data)
   {
      return parse(new String(data));
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public JavaSource<?> parse(final String data)
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
    * Create a {@link JavaSource} instance from the given {@link Document}, {@link CompilationUnit},
    * {@link TypeDeclaration}, and enclosing {@link JavaSource} type.
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
      if (JavaClass.class.isAssignableFrom(type))
         return (T) parse("public class JavaClass { }");

      if (JavaEnum.class.isAssignableFrom(type))
         return (T) parse("public enum JavaEnum { }");

      if (JavaAnnotation.class.isAssignableFrom(type))
         return (T) parse("public @interface JavaAnnotation { }");

      if (JavaInterface.class.isAssignableFrom(type))
         return (T) parse("public interface JavaInterface { }");

      throw new ParserException("Unknown JavaSource type [" + type.getName() + "]");
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends JavaSource<?>> T parse(final Class<T> type, final InputStream data)
   {

      JavaSource<?> source = parse(data);
      if (type.isAssignableFrom(source.getClass()))
      {
         return (T) source;
      }
      throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
               + source.getClass().getSimpleName() + "] - Cannot convert.");
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends JavaSource<?>> T parse(final Class<T> type, final char[] data)
   {
      JavaSource<?> source = parse(data);
      if (type.isAssignableFrom(source.getClass()))
      {
         return (T) source;
      }
      throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
               + source.getClass().getSimpleName() + "] - Cannot convert.");
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends JavaSource<?>> T parse(final Class<T> type, final String data)
   {
      JavaSource<?> source = parse(data);
      if (type.isAssignableFrom(source.getClass()))
      {
         return (T) source;
      }
      throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
               + source.getClass().getSimpleName() + "] - Cannot convert.");
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends JavaSource<?>> T parse(final Class<T> type, final File file) throws FileNotFoundException
   {
      JavaSource<?> source = parse(file);
      if (type.isAssignableFrom(source.getClass()))
      {
         return (T) source;
      }
      throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
               + source.getClass().getSimpleName() + "] - Cannot convert.");
   }
}
