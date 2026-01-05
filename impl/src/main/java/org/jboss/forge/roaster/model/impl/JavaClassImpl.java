/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.roaster.model.util.Types;

/**
 * Represents a Java Source File
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassImpl extends AbstractGenericCapableJavaSource<JavaClassSource> implements JavaClassSource
{

   public JavaClassImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaClassSource updateTypeNames(final String newName)
   {
      for (MethodSource<JavaClassSource> m : getMethods())
      {
         if (m.isConstructor())
         {
            m.setConstructor(false);
            m.setConstructor(true);
         }
      }
      return this;
   }

   /*
    * Type modifiers
    */
   @Override
   public boolean isAbstract()
   {
      return ModifierAccessor.hasModifier(getDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public JavaClassSource setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         ModifierAccessor.addModifier(getDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         ModifierAccessor.removeModifier(getDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
   }

   @Override
   public boolean isFinal()
   {
      return ModifierAccessor.hasModifier(getDeclaration(), ModifierKeyword.FINAL_KEYWORD);
   }

   @Override
   public JavaClassSource setFinal(boolean finl)
   {
      if (finl)
         ModifierAccessor.addModifier(getDeclaration(), ModifierKeyword.FINAL_KEYWORD);
      else
         ModifierAccessor.removeModifier(getDeclaration(), ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public boolean isLocalClass()
   {
      return JDTHelper.isLocalClass(getDeclaration());
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((toString() == null) ? 0 : unit.toString().hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      return (this == obj)
               || ((obj != null) && (getClass() == obj.getClass()) && this.toString().equals(obj.toString()));
   }

   @Override
   public String getSuperType()
   {
      Object superType = getDeclaration().getStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY);
      if (superType == null)
         superType = Object.class.getName();
      return resolveType(superType.toString()) + Types.getGenerics(superType.toString());
   }

   @Override
   public JavaClassSource setSuperType(final JavaType<?> type)
   {
      if (handleNullSuperType(type))
      {
         return this;
      }
      return setSuperType(type.getQualifiedName());
   }

   @Override
   public JavaClassSource setSuperType(final Class<?> type)
   {
      if (handleNullSuperType(type))
      {
         return this;
      }
      if (type.isAnnotation() || type.isEnum() || type.isInterface() || type.isPrimitive())
      {
         throw new IllegalArgumentException("Super-type must be a Class type, but was [" + type.getName() + "]");
      }
      return setSuperType(type.getName());
   }

   @Override
   public JavaClassSource extendSuperType(final Class<?> type)
   {
      setSuperType(type);
      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.addAnnotation(Override.class);
      }
      return this;
   }

   @Override
   public JavaClassSource extendSuperType(JavaClass<?> type)
   {
      setSuperType(type);
      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.addAnnotation(Override.class);
      }
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public JavaClassSource setSuperType(final String type)
   {
      if (handleNullSuperType(type))
      {
         return this;
      }

      if (Types.isGeneric(type))
      {
         String typeD = Types.stripGenerics(type);
         String simpleTypeDName = Types.toSimpleName(typeD);
         final SimpleName simpleName = body.getAST().newSimpleName(simpleTypeDName);
         Import imprt = addImport(type);
         Type parentType;
         if (imprt == null && !Types.isSimpleName(typeD))
         {
            // Conflicting import found, use qualified name for new super type
            final Name qualifier = body.getAST().newName(Types.getPackage(typeD));
            parentType = body.getAST().newNameQualifiedType(qualifier, simpleName);
         } else {
            // Same type as existing import or not qualified at all (maybe from same package)
            parentType = body.getAST().newSimpleType(simpleName);
         }
         org.eclipse.jdt.core.dom.ParameterizedType pt = body.getAST().newParameterizedType(parentType);
         for (String typeP : Types.splitGenerics(type))
         {
            Type t = TypeImpl.fromString(Types.toSimpleName(typeP.trim()), body.getAST());
            pt.typeArguments().add(t);
         }

         getDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, pt);
      }
      else
      {
         final SimpleName simpleName = body.getAST().newSimpleName(Types.toSimpleName(type));
         final Type superType;
         Import imprt = addImport(type);

         if (imprt == null && !Types.isSimpleName(type))
         {
            // Conflicting import found, use qualified name for new super type
            final Name qualifier = body.getAST().newName(Types.getPackage(type));
            superType = body.getAST().newNameQualifiedType(qualifier, simpleName);
         }
         else
         {
            // Same type as existing import or not qualified at all (maybe from same package)
            superType = body.getAST().newSimpleType(simpleName);
         }

         getDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, superType);
      }

      return this;
   }

   private boolean handleNullSuperType(String type)
   {
      if (type == null || type.trim().isEmpty())
      {
         return handleNullSuperType((Object) null);
      }
      return false;
   }

   private boolean handleNullSuperType(Object type)
   {
      if (type == null)
      {
         getDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, null);
         return true;
      }
      return false;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<InitializerSource<JavaClassSource>> getInitializers() 
   {
      List<InitializerSource<JavaClassSource>> result = new ArrayList<>();
      List<BodyDeclaration> bodyDeclarations = getDeclaration().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof Initializer) {
             Initializer initializer = (Initializer) bodyDeclaration;
             result.add(new InitializerImpl<>(this, initializer));
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public InitializerSource<JavaClassSource> addInitializer() 
   {
       InitializerSource<JavaClassSource> init = new InitializerImpl<>(this);
       getDeclaration().bodyDeclarations().add(init.getInternal());
       return init;
   }

   @Override
   @SuppressWarnings("unchecked")
   public InitializerSource<JavaClassSource> addInitializer(final String initializer) 
   {
      InitializerSource<JavaClassSource> init = new InitializerImpl<>(this, initializer);
      getDeclaration().bodyDeclarations().add(init.getInternal());
      return init;
   }

   @Override
   public JavaClassSource removeInitializer(org.jboss.forge.roaster.model.Initializer<JavaClassSource, ?> initializer) 
   {
      getDeclaration().bodyDeclarations().remove(initializer.getInternal());
      return this;
   }
}