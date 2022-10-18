/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.RecordDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaRecordComponent;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Java Source File containing a Record Type.
 */
public class JavaRecordImpl extends AbstractJavaSource<JavaRecordSource> implements JavaRecordSource
{
   public JavaRecordImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            RecordDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaRecordImpl updateTypeNames(String name)
   {
      return this;
   }

   @Override
   protected RecordDeclaration getDeclaration()
   {
      return (RecordDeclaration) super.getDeclaration();
   }

   @Override public List<MemberSource<JavaRecordSource, ?>> getMembers()
   {
      List<MemberSource<JavaRecordSource, ?>> result = new ArrayList<>();

      result.addAll(getMethods());

      return result;
   }

   @Override public List<MethodSource<JavaRecordSource>> getMethods()
   {
      List<MethodSource<JavaRecordSource>> result = new ArrayList<>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<>(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override public MethodSource<JavaRecordSource> addMethod()
   {
      var m = new MethodImpl<>(this);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override public MethodSource<JavaRecordSource> addMethod(String method)
   {
      var m = new MethodImpl<>(this, method);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override public MethodSource<JavaRecordSource> addMethod(java.lang.reflect.Method method)
   {
      var m = new MethodImpl<>(this, method);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override public MethodSource<JavaRecordSource> addMethod(Method<?, ?> method)
   {
      var m = new MethodImpl<>(this, method.toString());
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public JavaRecordSource removeMethod(Method<JavaRecordSource, ?> method)
   {
      getDeclaration().bodyDeclarations().remove(method.getInternal());
      return this;
   }

   @Override
   public List<JavaRecordComponentSource> getRecordComponents()
   {
      var list = new ArrayList<JavaRecordComponentSource>();
      for (Object internal : getDeclaration().recordComponents()) {
         list.add(new JavaRecordComponentImpl(this, internal));
      }
      return list;
   }

   @Override public JavaRecordComponentSource addRecordComponent(String type, String name)
   {
      var recordComponents = getDeclaration().recordComponents();

      String resolvedType = type;
      if (getOrigin().requiresImport(type))
      {
         Type<?> innerType = new TypeImpl<>(getOrigin(), null, type);
         Import imprt = getOrigin().addImport(innerType);
         resolvedType = imprt != null ? Types.rebuildGenericNameWithArrays(imprt.getSimpleName(), innerType)
                  : Types.toSimpleName(type);
      }

      String stub = "public record JavaRecord( " + Types.toResolvedType(resolvedType, this) + " " + name + " ) {}";
      JavaRecordSource temp = Roaster.parse(JavaRecordSource.class, stub);
      JavaRecordComponent recordComponent = temp.getRecordComponents().get(0);
      SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) recordComponent.getInternal();
      SingleVariableDeclaration copy = (SingleVariableDeclaration) ASTNode.copySubtree(getDeclaration().getAST(),
               singleVariableDeclaration);
      recordComponents.add(copy);
      return new JavaRecordComponentImpl(this, copy);
   }

   @Override public JavaRecordComponentSource addRecordComponent(Class<?> type, String name)
   {
      return addRecordComponent(type.getName(), name);
   }

   @Override
   public JavaRecordSource removeRecordComponent(String name)
   {
      var recordComponentsItr = getDeclaration().recordComponents().iterator();
      while (recordComponentsItr.hasNext()) {
         SingleVariableDeclaration svd = (SingleVariableDeclaration) recordComponentsItr.next();
         if (name.equals(svd.getName().toString())) {
            recordComponentsItr.remove();
            break;
         }
      }
      return this;
   }

   @Override
   public JavaRecordSource removeRecordComponent(JavaRecordComponent recordComponent)
   {
      getDeclaration().recordComponents().remove(recordComponent.getInternal());
      return this;
   }
}
