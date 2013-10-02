/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.parser.java.ReadJavaSource;
import org.jboss.forge.parser.java.ReadMethod.Method;
import org.jboss.forge.parser.java.SourceType;
import org.jboss.forge.parser.java.ast.ModifierAccessor;
import org.jboss.forge.parser.java.util.Types;

/**
 * Represents a Java Source File
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassImpl extends AbstractGenericCapableJavaSource<JavaClass> implements JavaClass
{
   private final ModifierAccessor modifiers = new ModifierAccessor();

   public JavaClassImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaClass updateTypeNames(final String newName)
   {
      for (Method<JavaClass> m : getMethods())
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
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public JavaClass setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
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
      Object superType = getBodyDeclaration().getStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY);
      if (superType == null)
         superType = Object.class.getName();
      return resolveType(superType.toString());
   }

   @Override
   public JavaClass setSuperType(final ReadJavaSource<?> type)
   {
      return setSuperType(type.getQualifiedName());
   }

   @Override
   public JavaClass setSuperType(final Class<?> type)
   {
      if (type.isAnnotation() || type.isEnum() || type.isInterface() || type.isPrimitive())
      {
         throw new IllegalArgumentException("Super-type must be a Class type, but was [" + type.getName() + "]");
      }
      return setSuperType(type.getName());
   }

   @SuppressWarnings("unchecked")
   @Override
   public JavaClass setSuperType(final String type)
   {
      if (type == null || type.trim().isEmpty())
      {
         getBodyDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, null);
      }
      else if (Types.isGeneric(type))
      {
         String typeD = Types.stripGenerics(type);
         String sympleTypeDName = Types.toSimpleName(typeD);
         String typesGeneric = Types.getGenericsTypeParameter(type);
         
         org.eclipse.jdt.core.dom.ParameterizedType pt = body.getAST().newParameterizedType(
                     body.getAST().newSimpleType(body.getAST().newSimpleName(sympleTypeDName)));
         
         if (!hasImport(typeD) && Types.isQualified(typeD))
         {
             addImport(typeD);
         }
         
         for (String typeP : typesGeneric.split(","))
         {
            pt.typeArguments().add(body.getAST().newSimpleType(body.getAST().newSimpleName(Types.toSimpleName(typeP.trim()))));
            
            if (!hasImport(typeP) && Types.isQualified(typeP))
            {
               addImport(typeP);
            }
         }
         
         getBodyDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, pt);
      }
      else
      {
         SimpleType simpleType = body.getAST().newSimpleType(body.getAST().newSimpleName(Types.toSimpleName(type)));
         getBodyDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, simpleType);

         if (!hasImport(type) && Types.isQualified(type))
         {
            addImport(type);
         }
      }

      return this;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.CLASS;
   }
}