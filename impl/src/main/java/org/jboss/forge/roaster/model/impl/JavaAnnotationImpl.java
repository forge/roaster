/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.AnnotationElement;
import org.jboss.forge.roaster.model.source.AnnotationElementSource;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JavaAnnotationImpl extends AbstractJavaSource<JavaAnnotationSource>implements JavaAnnotationSource
{

   public JavaAnnotationImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaAnnotationSource updateTypeNames(final String name)
   {
      return this;
   }

   @Override
   public AnnotationElementSource addAnnotationElement()
   {
      return add(new AnnotationElementImpl(this));
   }

   @Override
   public AnnotationElementSource addAnnotationElement(String declaration)
   {
      return add(new AnnotationElementImpl(this, declaration));
   }

   private AnnotationElementSource add(AnnotationElementSource annotationElement)
   {
      @SuppressWarnings("unchecked")
      final ListIterator<BodyDeclaration> members = getDeclaration().bodyDeclarations().listIterator();

      // skip any members before annotation elements, i.e. nested types
      while (members.hasNext())
      {
         if (members.next() instanceof AnnotationTypeMemberDeclaration)
         {
            break;
         }
      }
      // find the last annotation element
      while (members.hasNext())
      {
         if (members.next() instanceof AnnotationTypeMemberDeclaration)
         {
            continue;
         }
         // back up a step
         members.previous();
         break;
      }
      members.add((BodyDeclaration) annotationElement.getInternal());
      return annotationElement;
   }

   @Override
   public boolean hasAnnotationElement(String name)
   {
      for (AnnotationElementSource annotationElement : getAnnotationElements())
      {
         if (Strings.areEqual(name, annotationElement.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasAnnotationElement(AnnotationElement<?> annotationElement)
   {
      return getAnnotationElements().contains(annotationElement);
   }

   @Override
   public AnnotationElementSource getAnnotationElement(String name)
   {
      for (AnnotationElementSource annotationElement : getAnnotationElements())
      {
         if (Strings.areEqual(name, annotationElement.getName()))
         {
            return annotationElement;
         }
      }
      return null;
   }

   @Override
   public List<AnnotationElementSource> getAnnotationElements()
   {
      List<AnnotationElementSource> result = new ArrayList<>();
      @SuppressWarnings("unchecked")
      List<BodyDeclaration> bodyDeclarations = getDeclaration().bodyDeclarations();
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
   public JavaAnnotationSource removeAnnotationElement(AnnotationElement<?> annotationElement)
   {
      getDeclaration().bodyDeclarations().remove(annotationElement.getInternal());
      return this;
   }

}
