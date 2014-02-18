/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.impl.AnnotationImpl;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class AnnotationAccessor<O extends JavaSource<O>, T>
{

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target, final ASTNode body)
   {
      return addAnnotation(target, getModifiers(body));
   }

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration)
   {
      return addAnnotation(target, variableDeclaration.modifiers());
   }

   private AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target, final List<?> modifiers)
   {
      @SuppressWarnings("unchecked")
      ListIterator<IExtendedModifier> iter = (ListIterator<IExtendedModifier>) modifiers.listIterator();
      while (iter.hasNext() && iter.next().isAnnotation())
         ;

      // the effect of this is to back up only if the last encountered modifier is _not_ an annotation:
      if (iter.hasPrevious() && iter.previous().isAnnotation())
      {
         iter.next();
      }
      AnnotationSource<O> annotation = new AnnotationImpl<O, T>(target);
      iter.add((IExtendedModifier) annotation.getInternal());
      return annotation;
   }

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target, final ASTNode body,
            final Class<?> clazz)
   {
      return addAnnotation(target, getModifiers(body), clazz.getName());
   }

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final Class<?> clazz)
   {
      return addAnnotation(target, variableDeclaration.modifiers(), clazz.getName());
   }

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target, final ASTNode body,
            final String className)
   {
      return addAnnotation(target, getModifiers(body), className);
   }

   public AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final String className)
   {
      return addAnnotation(target, variableDeclaration.modifiers(), className);
   }

   private AnnotationSource<O> addAnnotation(final AnnotationTargetSource<O, T> target, final List<?> modifiers,
            final String className)
   {
      if (!target.getOrigin().hasImport(className) && Types.isQualified(className))
      {
         target.getOrigin().addImport(className);
      }
      return addAnnotation(target, modifiers).setName(Types.toSimpleName(className));
   }

   public List<AnnotationSource<O>> getAnnotations(final AnnotationTargetSource<O, T> target, final ASTNode body)
   {
      return getAnnotations(target, getModifiers(body));
   }

   public List<AnnotationSource<O>> getAnnotations(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration)
   {
      return getAnnotations(target, variableDeclaration.modifiers());
   }

   private List<AnnotationSource<O>> getAnnotations(final AnnotationTargetSource<O, T> target, final List<?> modifiers)
   {
      List<AnnotationSource<O>> result = new ArrayList<AnnotationSource<O>>();

      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            AnnotationSource<O> annotation = new AnnotationImpl<O, T>(target, object);
            result.add(annotation);
         }
      }

      return Collections.unmodifiableList(result);
   }

   public <E extends AnnotationTargetSource<O, T>> E removeAnnotation(final E target, final ASTNode body,
            final Annotation<O> annotation)
   {
      return removeAnnotation(target, getModifiers(body), annotation);
   }

   public <E extends AnnotationTargetSource<O, T>> E removeAnnotation(final E target,
            final SingleVariableDeclaration variableDeclaration,
            final Annotation<O> annotation)
   {
      return removeAnnotation(target, variableDeclaration.modifiers(), annotation);
   }

   private <E extends AnnotationTargetSource<O, T>> E removeAnnotation(final E target, final List<?> modifiers,
            final Annotation<O> annotation)
   {
      for (Object object : modifiers)
      {
         if (object.equals(annotation.getInternal()))
         {
            modifiers.remove(object);
            break;
         }
      }
      return target;
   }

   public <E extends AnnotationTargetSource<O, T>> boolean hasAnnotation(final E target, final ASTNode body,
            final String type)
   {
      return hasAnnotation(target, getModifiers(body), type);
   }

   public <E extends AnnotationTargetSource<O, T>> boolean hasAnnotation(final E target,
            final SingleVariableDeclaration variableDeclaration,
            final String type)
   {
      return hasAnnotation(target, variableDeclaration.modifiers(), type);
   }

   private <E extends AnnotationTargetSource<O, T>> boolean hasAnnotation(final E target, final List<?> modifiers,
            final String type)
   {
      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            AnnotationSource<O> annotation = new AnnotationImpl<O, T>(target, object);
            String annotationType = annotation.getName();
            if (Types.areEquivalent(type, annotationType))
            {
               return true;
            }
         }
      }
      return false;
   }

   public AnnotationSource<O> getAnnotation(final AnnotationTargetSource<O, T> target, final ASTNode body,
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return getAnnotation(target, getModifiers(body), type.getName());
   }

   public AnnotationSource<O> getAnnotation(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return getAnnotation(target, variableDeclaration.modifiers(), type.getName());
   }

   public AnnotationSource<O> getAnnotation(final AnnotationTargetSource<O, T> target, final ASTNode body, final String type)
   {
      return getAnnotation(target, getModifiers(body), type);
   }

   public AnnotationSource<O> getAnnotation(final AnnotationTargetSource<O, T> target,
            final SingleVariableDeclaration variableDeclaration, final String type)
   {
      return getAnnotation(target, variableDeclaration.modifiers(), type);
   }

   private AnnotationSource<O> getAnnotation(final AnnotationTargetSource<O, T> target, final List<?> modifiers, final String type)
   {
      List<AnnotationSource<O>> annotations = getAnnotations(target, modifiers);
      for (AnnotationSource<O> annotation : annotations)
      {
         if (Types.areEquivalent(type, annotation.getName()))
         {
            return annotation;
         }
      }
      return null;
   }

   private List<?> getModifiers(final ASTNode body)
   {
      if (body instanceof BodyDeclaration)
      {
         return ((BodyDeclaration) body).modifiers();
      }
      else if (body instanceof PackageDeclaration)
      {
         return ((PackageDeclaration) body).annotations();
      }
      return Collections.emptyList();
   }
}
