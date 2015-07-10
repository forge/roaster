/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.SyntaxError;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Formatter;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;
import org.jboss.forge.roaster.spi.WildcardImportResolver;

public class JavaPackageInfoImpl implements JavaPackageInfoSource
{
   protected final Document document;
   protected final CompilationUnit unit;
   protected final PackageDeclaration pkg;
   protected final JavaSource<?> enclosingType;

   private final AnnotationAccessor<JavaPackageInfoSource, JavaPackageInfoSource> annotations = new AnnotationAccessor<JavaPackageInfoSource, JavaPackageInfoSource>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private static List<WildcardImportResolver> resolvers;

   public JavaPackageInfoImpl(JavaSource<?> enclosingType, Document document,
            CompilationUnit unit, PackageDeclaration pkg)
   {
      this.enclosingType = enclosingType == null ? this : enclosingType;
      this.document = document;
      this.unit = unit;
      this.pkg = pkg;
   }

   @Override
   public String getName()
   {
      return "package-info";
   }

   @Override
   public JavaSource<?> getEnclosingType()
   {
      return enclosingType;
   }

   /*
    * Annotation modifiers
    */
   @Override
   public AnnotationSource<JavaPackageInfoSource> addAnnotation()
   {
      return annotations.addAnnotation(this, getPackageDeclaration());
   }

   @Override
   public AnnotationSource<JavaPackageInfoSource> addAnnotation(
            final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      return annotations.addAnnotation(this, getPackageDeclaration(), clazz.getName());
   }

   @Override
   public AnnotationSource<JavaPackageInfoSource> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, getPackageDeclaration(), className);
   }

   @Override
   public List<AnnotationSource<JavaPackageInfoSource>> getAnnotations()
   {
      return annotations.getAnnotations(this, getPackageDeclaration());
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, getPackageDeclaration(), type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, getPackageDeclaration(), type);
   }

   @Override
   public JavaPackageInfoSource removeAnnotation(final Annotation<JavaPackageInfoSource> annotation)
   {
      return annotations.removeAnnotation(this, getPackageDeclaration(), annotation);
   }

   @Override
   public AnnotationSource<JavaPackageInfoSource> getAnnotation(
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, getPackageDeclaration(), type);
   }

   @Override
   public AnnotationSource<JavaPackageInfoSource> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, getPackageDeclaration(), type);
   }

   /*
    * Import modifiers
    */

   @Override
   public Import addImport(final Class<?> type)
   {
      return addImport(type.getCanonicalName());
   }

   @Override
   public <T extends JavaType<?>> Import addImport(final T type)
   {
      String qualifiedName = type.getQualifiedName();
      return this.addImport(qualifiedName);
   }

   @Override
   public Import addImport(final Import imprt)
   {
      return addImport(imprt.getQualifiedName()).setStatic(imprt.isStatic());
   }

   @Override
   @SuppressWarnings("unchecked")
   public Import addImport(final String className)
   {
      String strippedClassName = Types.stripGenerics(Types.stripArray(className));
      Import imprt;
      if (Types.isSimpleName(strippedClassName) && !hasImport(strippedClassName))
      {
         throw new IllegalArgumentException("Cannot import class without a package [" + strippedClassName + "]");
      }

      if (!hasImport(strippedClassName) && validImport(strippedClassName))
      {
         imprt = new ImportImpl(this).setName(strippedClassName);
         unit.imports().add(imprt.getInternal());
      }
      else if (hasImport(strippedClassName))
      {
         imprt = getImport(strippedClassName);
      }
      else
      {
         throw new IllegalArgumentException("Attempted to import the illegal type [" + strippedClassName + "]");
      }
      return imprt;
   }

   @Override
   public Import getImport(final String className)
   {
      List<Import> imports = getImports();
      for (Import imprt : imports)
      {
         if (imprt.getQualifiedName().equals(className) || imprt.getSimpleName().equals(className))
         {
            return imprt;
         }
      }
      return null;
   }

   @Override
   public Import getImport(final Class<?> type)
   {
      return getImport(type.getName());
   }

   @Override
   public <T extends JavaType<?>> Import getImport(final T type)
   {
      return getImport(type.getQualifiedName());
   }

   @Override
   public Import getImport(final Import imprt)
   {
      return getImport(imprt.getQualifiedName());
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Import> getImports()
   {
      List<Import> results = new ArrayList<Import>();

      for (ImportDeclaration i : (List<ImportDeclaration>) unit.imports())
      {
         results.add(new ImportImpl(this, i));
      }

      return Collections.unmodifiableList(results);
   }

   @Override
   public boolean hasImport(final Class<?> type)
   {
      return hasImport(type.getName());
   }

   @Override
   public <T extends JavaType<T>> boolean hasImport(final T type)
   {
      return hasImport(type.getQualifiedName());
   }

   @Override
   public boolean hasImport(final Import imprt)
   {
      return hasImport(imprt.getQualifiedName());
   }

   @Override
   public boolean hasImport(final String type)
   {
      String resultType = type;
      if (Types.isArray(type))
      {
         resultType = Types.stripArray(type);
      }
      if (Types.isGeneric(type))
      {
         resultType = Types.stripGenerics(type);
      }
      return getImport(resultType) != null;
   }

   @Override
   public boolean requiresImport(final Class<?> type)
   {
      return requiresImport(type.getName());
   }

   @Override
   public boolean requiresImport(final String type)
   {
      String resultType = type;
      if (Types.isArray(resultType))
      {
         resultType = Types.stripArray(type);
      }
      if (Types.isGeneric(resultType))
      {
         resultType = Types.stripGenerics(resultType);
      }
      if (!validImport(resultType)
               || hasImport(resultType)
               || Types.isJavaLang(resultType))
      {
         return false;
      }
      return true;
   }

   @Override
   public String resolveType(final String type)
   {
      String original = type;
      String result = type;

      // Strip away any characters that might hinder the type matching process
      if (Types.isArray(result))
      {
         original = Types.stripArray(result);
         result = Types.stripArray(result);
      }

      if (Types.isGeneric(result))
      {
         original = Types.stripGenerics(result);
         result = Types.stripGenerics(result);
      }

      if (Types.isPrimitive(result))
      {
         return result;
      }

      // Check for direct import matches first since they are the fastest and least work-intensive
      if (Types.isSimpleName(result))
      {
         if (!hasImport(type) && Types.isJavaLang(type))
         {
            result = "java.lang." + result;
         }

         if (result.equals(original))
         {
            for (Import imprt : getImports())
            {
               if (Types.areEquivalent(result, imprt.getQualifiedName()))
               {
                  result = imprt.getQualifiedName();
                  break;
               }
            }
         }
      }

      // If we didn't match any imports directly, we might have a wild-card/on-demand import.
      if (Types.isSimpleName(result))
      {
         for (Import imprt : getImports())
         {
            if (imprt.isWildcard())
            {
               // TODO warn if no wild-card resolvers are configured
               // TODO Test wild-card/on-demand import resolving
               for (WildcardImportResolver r : getImportResolvers())
               {
                  result = r.resolve(this, result);
                  if (Types.isQualified(result))
                     break;
               }
            }
         }
      }

      // No import matches and no wild-card/on-demand import matches means this class is in the same package.
      if (Types.isSimpleName(result))
      {
         if (getPackage() != null)
            result = getPackage() + "." + result;
      }

      return result;
   }

   private List<WildcardImportResolver> getImportResolvers()
   {
      if (resolvers == null)
      {
         resolvers = new ArrayList<WildcardImportResolver>();
         for (WildcardImportResolver r : ServiceLoader.load(WildcardImportResolver.class, getClass().getClassLoader()))
         {
            resolvers.add(r);
         }
      }
      if (resolvers.size() == 0)
      {
         throw new IllegalStateException("No instances of [" + WildcardImportResolver.class.getName()
                  + "] were found on the classpath.");
      }
      return resolvers;
   }

   private boolean validImport(final String type)
   {
      return !Strings.isNullOrEmpty(type) && !Types.isPrimitive(type);
   }

   @Override
   public JavaPackageInfoSource removeImport(final String name)
   {
      for (Import i : getImports())
      {
         if (i.getQualifiedName().equals(name))
         {
            removeImport(i);
            break;
         }
      }
      return this;
   }

   @Override
   public JavaPackageInfoSource removeImport(final Class<?> clazz)
   {
      return removeImport(clazz.getName());
   }

   @Override
   public <T extends JavaType<?>> JavaPackageInfoSource removeImport(final T type)
   {
      return removeImport(type.getQualifiedName());
   }

   @Override
   public JavaPackageInfoSource removeImport(final Import imprt)
   {
      Object internal = imprt.getInternal();
      if (unit.imports().contains(internal))
      {
         unit.imports().remove(internal);
      }
      return this;
   }

   protected PackageDeclaration getPackageDeclaration()
   {
      if (pkg instanceof PackageDeclaration)
         return pkg;
      throw new ParserException("Source body was not of the expected type (PackageDeclaration).");
   }

   @Override
   public JavaPackageInfoSource setName(final String name)
   {
      throw new UnsupportedOperationException("Changing name of [" + getQualifiedName() + "] not supported.");
   }

   @Override
   public String getCanonicalName()
   {
      String result = getName();

      JavaType<?> enclosingType = this;
      while (enclosingType != enclosingType.getEnclosingType())
      {
         enclosingType = getEnclosingType();
         result = enclosingType.getEnclosingType().getName() + "." + result;
         enclosingType = enclosingType.getEnclosingType();
      }

      if (!Strings.isNullOrEmpty(getPackage()))
         result = getPackage() + "." + result;

      return result;
   }

   @Override
   public String getQualifiedName()
   {
      String result = getName();

      JavaType<?> enclosingType = this;
      while (enclosingType != enclosingType.getEnclosingType())
      {
         enclosingType = getEnclosingType();
         result = enclosingType.getEnclosingType().getName() + "$" + result;
         enclosingType = enclosingType.getEnclosingType();
      }

      if (!Strings.isNullOrEmpty(getPackage()))
         result = getPackage() + "." + result;

      return result;
   }

   /*
    * Package modifiers
    */
   @Override
   public String getPackage()
   {
      PackageDeclaration pkg = unit.getPackage();
      if (pkg != null)
      {
         return pkg.getName().getFullyQualifiedName();
      }
      else
      {
         return null;
      }
   }

   @Override
   public JavaPackageInfoSource setPackage(final String name)
   {
      if (unit.getPackage() == null)
      {
         unit.setPackage(unit.getAST().newPackageDeclaration());
      }
      unit.getPackage().setName(unit.getAST().newName(name));
      return this;
   }

   @Override
   public JavaPackageInfoSource setDefaultPackage()
   {
      unit.setPackage(null);
      return this;
   }

   @Override
   public boolean isDefaultPackage()
   {
      return unit.getPackage() == null;
   }

   /*
    * Visibility modifiers
    */
   @Override
   public boolean isPackagePrivate()
   {
      return (!isPublic() && !isPrivate() && !isProtected());
   }

   @Override
   public JavaPackageInfoSource setPackagePrivate()
   {
      modifiers.clearVisibility(getPackageDeclaration());
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(getPackageDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public JavaPackageInfoSource setPublic()
   {
      modifiers.clearVisibility(getPackageDeclaration());
      modifiers.addModifier(getPackageDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(getPackageDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public JavaPackageInfoSource setPrivate()
   {
      modifiers.clearVisibility(getPackageDeclaration());
      modifiers.addModifier(getPackageDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(getPackageDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public JavaPackageInfoSource setProtected()
   {
      modifiers.clearVisibility(getPackageDeclaration());
      modifiers.addModifier(getPackageDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public JavaPackageInfoSource setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

   /*
    * Non-manipulation methods.
    */
   /**
    * Return this {@link JavaType} file as a String
    */
   @Override
   public String toString()
   {
      return Formatter.format(toUnformattedString());
   }

   @Override
   public String toUnformattedString()
   {
      Document document = new Document(this.document.get());

      try
      {
         TextEdit edit = unit.rewrite(document, null);
         edit.apply(document);
      }
      catch (Exception e)
      {
         throw new ParserException("Could not modify source: " + unit.toString(), e);
      }

      return document.get();
   }

   @Override
   public Object getInternal()
   {
      return unit;
   }

   @Override
   public JavaPackageInfoSource getOrigin()
   {
      return this;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((pkg == null) ? 0 : pkg.hashCode());
      result = prime * result + ((document == null) ? 0 : document.hashCode());
      result = prime * result + ((enclosingType == null) ? 0 : enclosingType.hashCode());
      result = prime * result + ((unit == null) ? 0 : unit.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JavaPackageInfoImpl other = (JavaPackageInfoImpl) obj;
      if (pkg == null)
      {
         if (other.pkg != null)
            return false;
      }
      else if (!pkg.equals(other.pkg))
         return false;
      if (document == null)
      {
         if (other.document != null)
            return false;
      }
      else if (!document.equals(other.document))
         return false;
      if (enclosingType == null)
      {
         if (other.enclosingType != null)
            return false;
      }
      else if (!enclosingType.equals(other.enclosingType))
         return false;
      if (unit == null)
      {
         if (other.unit != null)
            return false;
      }
      else if (!unit.equals(other.unit))
         return false;
      return true;
   }

   @Override
   public List<SyntaxError> getSyntaxErrors()
   {
      List<SyntaxError> result = new ArrayList<SyntaxError>();

      IProblem[] problems = unit.getProblems();
      if (problems != null)
      {
         for (IProblem problem : problems)
         {
            result.add(new SyntaxErrorImpl(this, problem));
         }
      }
      return result;
   }

   @Override
   public boolean hasSyntaxErrors()
   {
      return !getSyntaxErrors().isEmpty();
   }

   @Override
   public boolean isClass()
   {
      return false;
   }

   @Override
   public boolean isEnum()
   {
      return false;
   }

   @Override
   public boolean isInterface()
   {
      return false;
   }

   @Override
   public boolean isAnnotation()
   {
      return false;
   }

   @Override
   public List<JavaSource<?>> getNestedClasses()
   {
      return Collections.emptyList();
   }

   @Override
   public Import addImport(Type<?> type)
   {
      return addImport(type.getQualifiedName());
   }

   @Override
   public JavaDocSource<JavaPackageInfoSource> getJavaDoc()
   {
      Javadoc javadoc = pkg.getJavadoc();
      if (javadoc == null)
      {
         javadoc = pkg.getAST().newJavadoc();
         pkg.setJavadoc(javadoc);
      }
      return new JavaDocImpl<JavaPackageInfoSource>(this, javadoc);
   }

   @Override
   public JavaPackageInfoSource removeJavaDoc()
   {
      pkg.setJavadoc(null);
      return this;
   }

   @Override
   public boolean hasJavaDoc()
   {
      return pkg.getJavadoc() != null;
   }

   public CompilationUnit getUnit()
   {
      return unit;
   }

   public PackageDeclaration getPkg()
   {
      return pkg;
   }
}
