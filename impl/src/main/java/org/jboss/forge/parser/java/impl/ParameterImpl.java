/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.Parameter;
import org.jboss.forge.parser.java.Type;
import org.jboss.forge.parser.java.ast.AnnotationAccessor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParameterImpl<O extends JavaSource<O>> implements Parameter<O>
{
   private final AnnotationAccessor<O, Parameter<O>> annotations = new AnnotationAccessor<O, Parameter<O>>();
   private final O parent;
   private final SingleVariableDeclaration param;

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
   public String getType()
   {
      return param.getType().toString();
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public Type<?> getTypeInspector()
   {
      return new TypeImpl(parent, param.getType());
   }

   @Override
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, param);
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, param, clazz.getSimpleName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, param, className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
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
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, param, type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, param, type);
   }

   @Override
   public Parameter<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, param, annotation);
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
