/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
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
import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Types;

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
      if (dec instanceof EnumDeclaration) {
         desc = EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY;
      } else {
         desc = TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY;
      }
      return (List<Type>) dec.getStructuralProperty(desc);
   }

   public static Type getType( Class klass, AST ast )
   {
      return getType( klass.getName(), ast );
   }

   public static Type getType( String klass, AST ast )
   {
      if ( Types.isPrimitive( klass ) ) {
          return ast.newPrimitiveType( PrimitiveType.toCode( klass ) );
      } else if ( Types.isQualified( klass ) ) {
          return ast.newQualifiedType( ast.newSimpleType( ast.newName( Types.getPackage( klass ) ) ), ast.newSimpleName( Types.toSimpleName( klass ) ) );
      } else if ( Types.isSimpleName( klass ) ) {
          return ast.newSimpleType( ast.newName( klass ) );
      } else if ( Types.isGeneric( klass ) ) {
          String base = Types.stripGenerics( klass );
          ParameterizedType param = ast.newParameterizedType( getType( base, ast ) );
          param.typeArguments().add( Types.getGenerics( klass ) );
      } else if ( Types.isArray( klass ) ) {
          String base = Types.stripArray( klass );
          return ast.newArrayType( getType( base, ast ), Types.getArrayDimension( klass ) );
      }
      return null;
   }


    public static String getter( String fieldName, String type ) {
        if ( boolean.class.getName().equals( type ) ) {
            return "is" + capitalize( fieldName );
        }
        return "get" + capitalize( fieldName );
    }

    public static String setter( String fieldName, String type ) {
        return "set" + capitalize( fieldName );
    }

    public static String capitalize( String fieldName ) {
        return fieldName.substring( 0, 1 ).toUpperCase() + fieldName.substring( 1 );
    }

}
