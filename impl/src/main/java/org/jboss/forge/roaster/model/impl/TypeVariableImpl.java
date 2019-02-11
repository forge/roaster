/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;

/**
 * 
 * @author mbenson
 * 
 */
public class TypeVariableImpl<O extends JavaSource<O>> implements TypeVariableSource<O>
{
   private final O origin;
   private final TypeParameter internal;

   public TypeVariableImpl(O origin, TypeParameter internal)
   {
      super();
      requireNonNull(origin, "null origin");
      this.origin = origin;
      requireNonNull(internal, "null internal representation");
      this.internal = internal;
   }

   @Override
   public List<Type<O>> getBounds()
   {
      @SuppressWarnings("unchecked")
      List<org.eclipse.jdt.core.dom.Type> typeBounds = internal.typeBounds();
      if (typeBounds.isEmpty())
      {
         return Collections.emptyList();
      }
      final List<Type<O>> result = new ArrayList<>(typeBounds.size());

      for (org.eclipse.jdt.core.dom.Type type : typeBounds)
      {
         result.add(new TypeImpl<>(origin, type));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public String getName()
   {
      return internal.getName().getIdentifier();
   }

   @Override
   public Object getInternal()
   {
      return internal;
   }

   @Override
   public O getOrigin()
   {
      return origin;
   }

   @Override
   public TypeVariableSource<O> setName(String name)
   {
      internal.setName(internal.getAST().newSimpleName(name));
      return this;
   }

   @Override
   public TypeVariableSource<O> setBounds(JavaType<?>... bounds)
   {
      final String[] names;
      if (bounds == null)
      {
         names = new String[0];
      }
      else
      {
         names = new String[bounds.length];
         int i = 0;
         for (JavaType<?> t : bounds)
         {
            names[i++] = origin.addImport(t).getSimpleName();
         }
      }
      return setBounds(names);
   }

   @Override
   public TypeVariableSource<O> setBounds(Class<?>... bounds)
   {
      final String[] names;
      if (bounds == null)
      {
         names = new String[0];
      }
      else
      {
         names = new String[bounds.length];
         int i = 0;
         for (Class<?> cls : bounds)
         {
            names[i++] = origin.addImport(cls).getSimpleName();
         }
      }
      return setBounds(names);
   }

   @SuppressWarnings("unchecked")
   @Override
   public TypeVariableSource<O> setBounds(String... bounds)
   {
      internal.typeBounds().clear();
      for (String s : bounds)
      {
         org.eclipse.jdt.core.dom.Type copy = (org.eclipse.jdt.core.dom.Type) ASTNode.copySubtree(internal.getAST(),
                  parseTypeBound(s));
         internal.typeBounds().add(copy);
      }
      return this;
   }

   @Override
   public TypeVariableSource<O> removeBounds()
   {
      internal.typeBounds().clear();
      return this;
   }

   private org.eclipse.jdt.core.dom.Type parseTypeBound(String bound)
   {
      String stub = "public class Stub<T extends " + bound + "> {}";
      JavaClassSource temp = Roaster.parse(JavaClassSource.class, stub);
      TypeParameter v = (TypeParameter) temp.getTypeVariables().get(0).getInternal();
      return (org.eclipse.jdt.core.dom.Type) v.typeBounds().get(0);
   }
}
