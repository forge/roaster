/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.ParameterSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParameterImpl<O extends JavaSource<O>> implements ParameterSource<O>
{
   private final AnnotationAccessor<O, ParameterSource<O>> annotations = new AnnotationAccessor<>();
   private final O parent;
   private final SingleVariableDeclaration param;
   private final ModifierAccessor modifiers = new ModifierAccessor();

   public ParameterImpl(final O parent, final Object internal)
   {
      this.parent = parent;
      this.param = (SingleVariableDeclaration) internal;
   }

   @Override
   public String toString()
   {
      return param.toString();
   }

   @Override
   public String getName()
   {
      SimpleName name = param.getName();
      if (name != null)
      {
         return name.toString();
      }
      return "";
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public Type<O> getType()
   {
      return new TypeImpl(parent, param.getType());
   }

   @Override
   public AnnotationSource<O> addAnnotation()
   {
      return annotations.addAnnotation(this, param);
   }

   @Override
   public AnnotationSource<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, param, clazz.getSimpleName());
   }

   @Override
   public AnnotationSource<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, param, className);
   }

   @Override
   public List<AnnotationSource<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, param);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, param, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, param, type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, param, type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, param, type);
   }

   @Override
   public ParameterSource<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, param, annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(param);
   }

   @Override
   public boolean isFinal()
   {
      return modifiers.hasModifier(param, ModifierKeyword.FINAL_KEYWORD);
   }

   @Override
   public ParameterSource<O> setFinal(boolean finl)
   {
      if (finl)
         modifiers.addModifier(param, ModifierKeyword.FINAL_KEYWORD);
      else
         modifiers.removeModifier(param, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public ParameterSource<O> setVarArgs(boolean variableArity)
   {
      param.setVarargs(variableArity);
      return this;
   }

   @Override
   public boolean isVarArgs()
   {
      return param.isVarargs();
   }

   @Override
   public Object getInternal()
   {
      return param;
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }
}