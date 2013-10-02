/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.java.ReadAnnotationElement;
import org.jboss.forge.parser.java.ReadAnnotationElement.AnnotationElement;
import org.jboss.forge.parser.java.ReadJavaAnnotation.JavaAnnotation;
import org.jboss.forge.parser.java.SourceType;
import org.jboss.forge.parser.java.util.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JavaAnnotationImpl extends AbstractJavaSource<JavaAnnotation> implements JavaAnnotation
{

   public JavaAnnotationImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaAnnotation updateTypeNames(final String name)
   {
      return this;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.ANNOTATION;
   }

   @Override
   public AnnotationElement addAnnotationElement()
   {
      return add(new AnnotationElementImpl(this));
   }

   @Override
   public AnnotationElement addAnnotationElement(String declaration)
   {
      return add(new AnnotationElementImpl(this, declaration));
   }

   private AnnotationElement add(AnnotationElement annotationElement)
   {
      @SuppressWarnings("unchecked")
      final ListIterator<BodyDeclaration> members = getBodyDeclaration().bodyDeclarations().listIterator();

      //skip any members before annotation elements, i.e. nested types
      while (members.hasNext())
      {
         if (members.next() instanceof AnnotationTypeMemberDeclaration)
         {
            break;
         }
      }
      //find the last annotation element
      while (members.hasNext())
      {
         if (members.next() instanceof AnnotationTypeMemberDeclaration)
         {
            continue;
         }
         //back up a step
         members.previous();
         break;
      }
      members.add((BodyDeclaration) annotationElement.getInternal());
      return annotationElement;
   }

   @Override
   public boolean hasAnnotationElement(String name)
   {
      for (AnnotationElement annotationElement : getAnnotationElements())
      {
         if (Strings.areEqual(name, annotationElement.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasAnnotationElement(ReadAnnotationElement<?> annotationElement)
   {
      return getAnnotationElements().contains(annotationElement);
   }

   @Override
   public AnnotationElement getAnnotationElement(String name)
   {
      for (AnnotationElement annotationElement : getAnnotationElements())
      {
         if (Strings.areEqual(name, annotationElement.getName()))
         {
            return annotationElement;
         }
      }
      return null;
   }

   @Override
   public List<AnnotationElement> getAnnotationElements()
   {
      List<AnnotationElement> result = new ArrayList<AnnotationElement>();
      @SuppressWarnings("unchecked")
      List<BodyDeclaration> bodyDeclarations = getBodyDeclaration().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof AnnotationTypeMemberDeclaration)
         {
            result.add(new AnnotationElementImpl(this, bodyDeclaration));
         }
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   public JavaAnnotation removeAnnotationElement(ReadAnnotationElement<?> annotationElement)
   {
      getBodyDeclaration().bodyDeclarations().remove(annotationElement.getInternal());
      return this;
   }

}
