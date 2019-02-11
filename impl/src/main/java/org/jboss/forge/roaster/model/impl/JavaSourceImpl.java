/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
*/
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
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
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Formatter;
import org.jboss.forge.roaster.model.util.JDTOptions;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;
import org.jboss.forge.roaster.spi.WildcardImportResolver;

/**
 * Represents a Java Source File
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class JavaSourceImpl<O extends JavaSource<O>> implements JavaSource<O>
{
   private static List<WildcardImportResolver> resolvers;
   private final ModifierAccessor modifiers = new ModifierAccessor();
   private final AnnotationAccessor<O, O> annotations = new AnnotationAccessor<>();

   protected final Document document;
   protected final CompilationUnit unit;
   protected final JavaSource<?> enclosingType;

   protected JavaSourceImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit)
   {
      this.enclosingType = enclosingType == null ? this : enclosingType;
      this.document = document;
      this.unit = unit;
   }

   // ================================================================================
   // Location
   // ================================================================================

   @Override
   public int getColumnNumber()
   {
      return unit.getColumnNumber(getStartPosition());
   }

   @Override
   public int getLineNumber()
   {
      return unit.getLineNumber(getStartPosition());
   }

   @Override
   public int getEndPosition()
   {
      int startPosition = getStartPosition();
      return (startPosition == -1) ? -1 : startPosition + getDeclaration().getLength();
   }

   @Override
   public int getStartPosition()
   {
      return getDeclaration().getStartPosition();
   }

   // ================================================================================
   // JavaDoc
   // ================================================================================

   @Override
   public boolean hasJavaDoc()
   {
      return getJDTJavaDoc() != null;
   }

   @SuppressWarnings("unchecked")
   @Override
   public JavaDocSource<O> getJavaDoc()
   {
      Javadoc javadoc = getJDTJavaDoc();
      if (javadoc == null)
      {
         javadoc = getDeclaration().getAST().newJavadoc();
         setJDTJavaDoc(javadoc);
      }
      return new JavaDocImpl<>((O) this, javadoc);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeJavaDoc()
   {
      setJDTJavaDoc(null);
      return (O) this;
   }

   protected abstract Javadoc getJDTJavaDoc();

   protected abstract void setJDTJavaDoc(Javadoc javaDoc);

   // ================================================================================
   // Annotation
   // ================================================================================

   @Override
   public AnnotationSource<O> addAnnotation()
   {
      return annotations.addAnnotation(this, getDeclaration());
   }

   @Override
   public List<AnnotationSource<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, getDeclaration());
   }

   @Override
   public AnnotationSource<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      return annotations.addAnnotation(this, getDeclaration(), clazz.getName());
   }

   @Override
   public AnnotationSource<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, getDeclaration(), className);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, getDeclaration(), type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, getDeclaration(), type);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeAnnotation(final Annotation<O> annotation)
   {
      return (O) annotations.removeAnnotation(this, getDeclaration(), annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(getDeclaration());
   }

   @Override
   public AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, getDeclaration(), type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, getDeclaration(), type);
   }

   // ================================================================================
   // Import
   // ================================================================================

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

   @SuppressWarnings("unchecked")
   @Override
   public Import addImport(final String className)
   {
      String strippedClassName = Types.stripGenerics(Types.stripArray(className));

      if (Types.isGeneric(className))
      {
         for (String genericPart : Types.splitGenerics(className))
         {
            if (Types.isQualified(genericPart))
               addImport(genericPart);
         }
      }

      Import imprt;
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
         imprt = null;
      }
      return imprt;
   }

   @Override
   public Import getImport(final String className)
   {
      for (Import imprt : getImports())
      {
         if (imprt.getQualifiedName().equals(className) || imprt.getSimpleName().equals(className))
         {
            return imprt;
         }
      }
      return null;
   }

   @Override
   public Import addImport(final Type<?> type)
   {
      Import imprt;
      if (requiresImport(type.getQualifiedName()))
      {
         imprt = addImport(type.getQualifiedName());
      }
      else
      {
         imprt = getImport(type.getQualifiedName());
      }
      for (Type<?> arg : type.getTypeArguments())
      {
         if (!arg.isWildcard() && arg.isQualified())
         {
            addImport(arg);
         }
      }
      return imprt;
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

   @SuppressWarnings("unchecked")
   @Override
   public List<Import> getImports()
   {
      List<Import> results = new ArrayList<>();

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
      boolean requiresImport = false;
      String resultType = type;
      if (Types.isArray(resultType))
      {
         resultType = Types.stripArray(resultType);
      }
      if (Types.isGeneric(resultType))
      {
         for (String genericPart : Types.splitGenerics(resultType))
         {
            requiresImport |= requiresImport(genericPart);
         }
         resultType = Types.stripGenerics(resultType);
      }
      requiresImport |= !(!validImport(resultType)
               || hasImport(resultType)
               || Types.isJavaLang(resultType)
               || Strings.areEqual(getPackage(), Types.getPackage(resultType)));
      return requiresImport;
   }

   @Override
   public String resolveType(final String type)
   {
      String result = type;

      // Strip away any characters that might hinder the type matching process
      if (Types.isArray(result))
      {
         result = Types.stripArray(result);
      }

      if (Types.isGeneric(result))
      {
         result = Types.stripGenerics(result);
      }

      // primitive types don't need a import -> direct return
      if (Types.isPrimitive(result))
      {
         return result;
      }

      // qualified names don't need to be resolved
      if (Types.isQualified(result))
      {
         return result;
      }

      // java lang types are implicitly imported and we don't allow a duplicate simple name in another package
      if (Types.isJavaLang(result))
      {
         return "java.lang." + result;
      }

      // Check for an existing direct import
      Import directImport = getImport(result);
      if (directImport != null)
      {
         return directImport.getQualifiedName();
      }

      List<Import> imports = getImports(); // fetch imports only once

      // if we have no imports and no fqn name the following doesn't need to be executed
      if (!imports.isEmpty())
      {
         // if we have only one wildcard import we can use this
         List<Import> wildcardImports = imports.stream().filter(Import::isWildcard).collect(Collectors.toList());
         if (wildcardImports.size() == 1)
         {
            return wildcardImports.get(0).getPackage() + "." + result;
         }

         // If we didn't match any imports directly, we might have a wild-card/on-demand import.
         for (Import imprt : imports)
         {
            if (imprt.isWildcard())
            {
               // TODO warn if no wild-card resolvers are configured
               // TODO Test wild-card/on-demand import resolving
               for (WildcardImportResolver r : getImportResolvers())
               {
                  result = r.resolve(this, result);
                  if (Types.isQualified(result))
                     return result;
               }
            }
         }
      }

      // Nothing matched since here, so try to resolve it with the current package
      if (getPackage() != null)
      {
         return getPackage() + "." + result;
      }

      return result;
   }

   private List<WildcardImportResolver> getImportResolvers()
   {
      if (resolvers == null)
      {
         resolvers = new ArrayList<>();
         for (WildcardImportResolver r : ServiceLoader.load(WildcardImportResolver.class, getClass().getClassLoader()))
         {
            resolvers.add(r);
         }
      }
      if (resolvers.isEmpty())
      {
         throw new IllegalStateException("No instances of [" + WildcardImportResolver.class.getName()
                  + "] were found on the classpath.");
      }
      return resolvers;
   }

   private boolean validImport(final String type)
   {
      String className = Types.toSimpleName(type);
      // check if this class name is equal to the class to import
      if (className.equals(getName()))
      {
         return false;
      }
      // check if any import has the same class name
      for (final Import imprt : getImports())
      {
         String importClassName = imprt.getSimpleName();
         if (importClassName.equals(className))
         {
            return false;
         }
      }
      return !Strings.isNullOrEmpty(type) && !Types.isPrimitive(type) && !Strings.isNullOrEmpty(Types.getPackage(type));
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeImport(final String name)
   {
      for (Import i : getImports())
      {
         if (i.getQualifiedName().equals(name))
         {
            removeImport(i);
            break;
         }
      }
      return (O) this;
   }

   @Override
   public O removeImport(final Class<?> clazz)
   {
      return removeImport(clazz.getName());
   }

   @Override
   public <T extends JavaType<?>> O removeImport(final T type)
   {
      return removeImport(type.getQualifiedName());
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeImport(final Import imprt)
   {
      Object internal = imprt.getInternal();
      if (unit.imports().contains(internal))
      {
         unit.imports().remove(internal);
      }
      return (O) this;
   }

   // ================================================================================
   // Naming
   // ================================================================================

   @Override
   public String getCanonicalName()
   {
      String result = getName();

      JavaType<?> enclosingTypeLocal = this;
      while (enclosingTypeLocal != enclosingTypeLocal.getEnclosingType())
      {
         enclosingTypeLocal = enclosingTypeLocal.getEnclosingType();
         result = enclosingTypeLocal.getName() + "." + result;
      }

      if (!Strings.isNullOrEmpty(getPackage()))
         result = getPackage() + "." + result;

      return result;
   }

   /**
    * Call-back to allow updating of any necessary internal names with the given name.
    */
   protected abstract O updateTypeNames(String name);

   @Override
   public String getQualifiedName()
   {
      String result = getName();

      JavaType<?> enclosingTypeLocal = this;
      while (enclosingTypeLocal != enclosingTypeLocal.getEnclosingType())
      {
         enclosingTypeLocal = enclosingTypeLocal.getEnclosingType();
         result = enclosingTypeLocal.getName() + "$" + result;
      }

      if (!Strings.isNullOrEmpty(getPackage()))
         result = getPackage() + "." + result;

      return result;
   }

   // ================================================================================
   // Package
   // ================================================================================

   @Override
   public String getPackage()
   {
      PackageDeclaration pkg = unit.getPackage();
      if (pkg != null)
      {
         return pkg.getName().getFullyQualifiedName();
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setPackage(final String name)
   {
      if (unit.getPackage() == null)
      {
         unit.setPackage(unit.getAST().newPackageDeclaration());
      }
      unit.getPackage().setName(unit.getAST().newName(name));
      return (O) this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setDefaultPackage()
   {
      unit.setPackage(null);
      return (O) this;
   }

   @Override
   public boolean isDefaultPackage()
   {
      return unit.getPackage() == null;
   }

   // ================================================================================
   // String methods
   // ================================================================================
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
      Document documentLocal = new Document(this.document.get());

      try
      {
         Map<String, String> options = JDTOptions.getJDTOptions();
         TextEdit edit = unit.rewrite(documentLocal, options);
         edit.apply(documentLocal);
      }
      catch (Exception e)
      {
         throw new ParserException("Could not modify source: " + unit.toString(), e);
      }

      return documentLocal.get();
   }

   // ================================================================================
   // Visibility
   // ================================================================================

   @SuppressWarnings("unchecked")
   @Override
   public O setPackagePrivate()
   {
      modifiers.clearVisibility(getDeclaration());
      return (O) this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(getDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setPublic()
   {
      modifiers.clearVisibility(getDeclaration());
      modifiers.addModifier(getDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
      return (O) this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(getDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setPrivate()
   {
      modifiers.clearVisibility(getDeclaration());
      modifiers.addModifier(getDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
      return (O) this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(getDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setProtected()
   {
      modifiers.clearVisibility(getDeclaration());
      modifiers.addModifier(getDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
      return (O) this;
   }

   @Override
   public boolean isPackagePrivate()
   {
      return !isPublic() && !isPrivate() && !isProtected();
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @SuppressWarnings("unchecked")
   @Override
   public O setVisibility(final Visibility scope)
   {
      return (O) Visibility.set(this, scope);
   }

   // ================================================================================
   // Types
   // ================================================================================

   @Override
   public boolean isClass()
   {
      ASTNode declaration = getDeclaration();
      return (declaration instanceof TypeDeclaration)
               && !((TypeDeclaration) declaration).isInterface();

   }

   @Override
   public boolean isEnum()
   {
      ASTNode declaration = getDeclaration();
      return declaration instanceof EnumDeclaration;
   }

   @Override
   public boolean isInterface()
   {
      ASTNode declaration = getDeclaration();
      return (declaration instanceof TypeDeclaration)
               && ((TypeDeclaration) declaration).isInterface();
   }

   @Override
   public boolean isAnnotation()
   {
      return getDeclaration() instanceof AnnotationTypeDeclaration;
   }

   // ================================================================================
   // other
   // ================================================================================

   @Override
   public JavaSource<?> getEnclosingType()
   {
      return enclosingType;
   }

   @Override
   public int hashCode()
   {
      if (enclosingType == this)
      {
         return Objects.hash(document, unit);
      }
      return Objects.hash(document, enclosingType, unit);
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
      AbstractJavaSource<?> other = (AbstractJavaSource<?>) obj;
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
   public Object getInternal()
   {
      return unit;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O getOrigin()
   {
      return (O) this;
   }

   @Override
   public List<SyntaxError> getSyntaxErrors()
   {
      List<SyntaxError> result = new ArrayList<>();

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

   protected abstract ASTNode getDeclaration();
}
