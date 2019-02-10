/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.TypeVariable;
import org.jboss.forge.roaster.model.source.GenericCapableSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.PropertyHolderSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;
import org.jboss.forge.roaster.model.util.Strings;

/**
 *
 * @author mbenson
 *
 * @param <O>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGenericCapableJavaSource<O extends JavaSource<O> & PropertyHolderSource<O>> extends AbstractJavaSourceMemberHolder<O>
         implements GenericCapableSource<O, O>
{

   protected AbstractGenericCapableJavaSource(JavaSource<?> enclosingType, Document document, CompilationUnit unit,
            BodyDeclaration declaration)
   {
      super(enclosingType, document, unit, declaration);
   }

   @Override
   public List<TypeVariableSource<O>> getTypeVariables()
   {
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      List<TypeVariableSource<O>> result = new ArrayList<>();
      for (TypeParameter typeParameter : typeParameters)
      {
         result.add(new TypeVariableImpl<>((O) this, typeParameter));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public TypeVariableSource<O> getTypeVariable(String name)
   {
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      for (TypeParameter typeParameter : typeParameters)
      {
         if (Strings.areEqual(name, typeParameter.getName().getIdentifier()))
         {
            return new TypeVariableImpl<>((O) this, typeParameter);
         }
      }
      return null;
   }

   @Override
   public boolean hasTypeVariable(String name)
   {
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      for (TypeParameter typeParameter : typeParameters)
      {
         if (Strings.areEqual(name, typeParameter.getName().getIdentifier()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public TypeVariableSource<O> addTypeVariable()
   {
      TypeDeclaration type = (TypeDeclaration) body;
      TypeParameter tp2 = unit.getAST().newTypeParameter();
      type.typeParameters().add(tp2);
      return new TypeVariableImpl<>((O) this, tp2);
   }

   @Override
   public TypeVariableSource<O> addTypeVariable(String name)
   {
      return addTypeVariable().setName(name);
   }

   @Override
   public O removeTypeVariable(String name)
   {
      TypeDeclaration type = (TypeDeclaration) body;
      List<TypeParameter> typeParameters = type.typeParameters();
      for (Iterator<TypeParameter> iter = typeParameters.iterator(); iter.hasNext();)
      {
         if (Strings.areEqual(name, iter.next().getName().getIdentifier()))
         {
            iter.remove();
            break;
         }
      }
      return (O) this;
   }

   @Override
   public O removeTypeVariable(TypeVariable<?> typeVariable)
   {
      return removeTypeVariable(typeVariable.getName());
   }

}
