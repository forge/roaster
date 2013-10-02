/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.java.ReadGenericCapable.GenericCapable;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * 
 * @author mbenson
 *
 * @param <O>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGenericCapableJavaSource<O extends JavaSource<O>> extends AbstractJavaSourceMemberHolder<O>
         implements GenericCapable<O>
{

   public AbstractGenericCapableJavaSource(JavaSource<?> enclosingType, Document document, CompilationUnit unit,
            BodyDeclaration declaration)
   {
      super(enclosingType, document, unit, declaration);
   }

   @Override
   public List<String> getGenericTypes()
   {
      List<String> result = new ArrayList<String>();
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      if (typeParameters != null)
      {
         for (TypeParameter typeParameter : typeParameters)
         {
            result.add(typeParameter.getName().getIdentifier());
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public O addGenericType(String genericType)
   {
      TypeDeclaration type = (TypeDeclaration) body;
      TypeParameter tp2 = unit.getAST().newTypeParameter();
      tp2.setName(unit.getAST().newSimpleName(genericType));
      type.typeParameters().add(tp2);
      return (O) this;
   }

   @Override
   public O removeGenericType(String genericType)
   {
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      if (typeParameters != null)
      {
         Iterator<TypeParameter> it = typeParameters.iterator();
         while (it.hasNext())
         {
            TypeParameter typeParameter = it.next();
            if (typeParameter.getName().getIdentifier().equals(genericType))
            {
               it.remove();
            }
         }
      }
      return (O) this;
   }

}
