/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jboss.forge.parser.java.ReadAnnotation;
import org.jboss.forge.parser.java.ReadAnnotation.Annotation;
import org.jboss.forge.parser.java.ReadAnnotationTarget.AnnotationTarget;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.impl.AnnotationImpl;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class AnnotationAccessor<O extends JavaSource<O>, T>
{

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target, final ASTNode body)
   {
      return addAnnotation(target, getModifiers(body));
   }

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration)
   {
      return addAnnotation(target, variableDeclaration.modifiers());
   }

   private Annotation<O> addAnnotation(final AnnotationTarget<O, T> target, final List<?> modifiers)
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
      Annotation<O> annotation = new AnnotationImpl<O, T>(target);
      iter.add((IExtendedModifier) annotation.getInternal());
      return annotation;
   }

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target, final ASTNode body,
            final Class<?> clazz)
   {
      return addAnnotation(target, getModifiers(body), clazz.getName());
   }

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final Class<?> clazz)
   {
      return addAnnotation(target, variableDeclaration.modifiers(), clazz.getName());
   }

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target, final ASTNode body,
            final String className)
   {
      return addAnnotation(target, getModifiers(body), className);
   }

   public Annotation<O> addAnnotation(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final String className)
   {
      return addAnnotation(target, variableDeclaration.modifiers(), className);
   }

   private Annotation<O> addAnnotation(final AnnotationTarget<O, T> target, final List<?> modifiers,
            final String className)
   {
      if (!target.getOrigin().hasImport(className) && Types.isQualified(className))
      {
         target.getOrigin().addImport(className);
      }
      return addAnnotation(target, modifiers).setName(Types.toSimpleName(className));
   }

   public List<Annotation<O>> getAnnotations(final AnnotationTarget<O, T> target, final ASTNode body)
   {
      return getAnnotations(target, getModifiers(body));
   }

   public List<Annotation<O>> getAnnotations(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration)
   {
      return getAnnotations(target, variableDeclaration.modifiers());
   }

   private List<Annotation<O>> getAnnotations(final AnnotationTarget<O, T> target, final List<?> modifiers)
   {
      List<Annotation<O>> result = new ArrayList<Annotation<O>>();

      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            Annotation<O> annotation = new AnnotationImpl<O, T>(target, object);
            result.add(annotation);
         }
      }

      return Collections.unmodifiableList(result);
   }

   public <E extends AnnotationTarget<O, T>> E removeAnnotation(final E target, final ASTNode body,
            final ReadAnnotation<O> annotation)
   {
      return removeAnnotation(target, getModifiers(body), annotation);
   }

   public <E extends AnnotationTarget<O, T>> E removeAnnotation(final E target,
            final SingleVariableDeclaration variableDeclaration,
            final ReadAnnotation<O> annotation)
   {
      return removeAnnotation(target, variableDeclaration.modifiers(), annotation);
   }

   private <E extends AnnotationTarget<O, T>> E removeAnnotation(final E target, final List<?> modifiers,
            final ReadAnnotation<O> annotation)
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

   public <E extends AnnotationTarget<O, T>> boolean hasAnnotation(final E target, final ASTNode body,
            final String type)
   {
      return hasAnnotation(target, getModifiers(body), type);
   }

   public <E extends AnnotationTarget<O, T>> boolean hasAnnotation(final E target,
            final SingleVariableDeclaration variableDeclaration,
            final String type)
   {
      return hasAnnotation(target, variableDeclaration.modifiers(), type);
   }

   private <E extends AnnotationTarget<O, T>> boolean hasAnnotation(final E target, final List<?> modifiers,
            final String type)
   {
      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            Annotation<O> annotation = new AnnotationImpl<O, T>(target, object);
            String annotationType = annotation.getName();
            if (Types.areEquivalent(type, annotationType))
            {
               return true;
            }
         }
      }
      return false;
   }

   public Annotation<O> getAnnotation(final AnnotationTarget<O, T> target, final ASTNode body,
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return getAnnotation(target, getModifiers(body), type.getName());
   }

   public Annotation<O> getAnnotation(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration,
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return getAnnotation(target, variableDeclaration.modifiers(), type.getName());
   }

   public Annotation<O> getAnnotation(final AnnotationTarget<O, T> target, final ASTNode body, final String type)
   {
      return getAnnotation(target, getModifiers(body), type);
   }

   public Annotation<O> getAnnotation(final AnnotationTarget<O, T> target,
            final SingleVariableDeclaration variableDeclaration, final String type)
   {
      return getAnnotation(target, variableDeclaration.modifiers(), type);
   }

   private Annotation<O> getAnnotation(final AnnotationTarget<O, T> target, final List<?> modifiers, final String type)
   {
      List<Annotation<O>> annotations = getAnnotations(target, modifiers);
      for (Annotation<O> annotation : annotations)
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
