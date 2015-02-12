/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * Help with Eclipse JDT common operations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JDTHelper
{
   /**
    * Get the name of the given {@link Type}. Return the qualified name if possible.
    */
   public static String getTypeName(final Type type)
   {
      if (type instanceof SimpleType)
      {
         return ((SimpleType) type).getName().toString();
      }
      else if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getStructuralProperty(ArrayType.ELEMENT_TYPE_PROPERTY).toString();
      }
      else if (type instanceof QualifiedType)
      {
         return ((QualifiedType) type).toString();
      }
      else if (type instanceof PrimitiveType)
      {
         return ((PrimitiveType) type).toString();
      }
      else if (type instanceof ParameterizedType)
      {
         return ((ParameterizedType) type).getType().toString();
      }
      else if (type instanceof WildcardType)
      {
         return ((WildcardType) type).getBound().toString();
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   public static List<Type> getInterfaces(final BodyDeclaration dec)
   {
      StructuralPropertyDescriptor desc;
      if (dec instanceof EnumDeclaration)
      {
         desc = EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY;
      }
      else
      {
         desc = TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY;
      }
      return (List<Type>) dec.getStructuralProperty(desc);
   }

}
