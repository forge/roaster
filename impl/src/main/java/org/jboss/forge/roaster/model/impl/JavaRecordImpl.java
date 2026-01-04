/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.RecordDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.JavaRecordComponent;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
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
public class JavaRecordImpl extends AbstractInterfaceCapableJavaSource<JavaRecordSource> implements JavaRecordSource
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

   @Override
   @SuppressWarnings("unchecked")
   public List<InitializerSource<JavaRecordSource>> getInitializers() 
   {
      List<InitializerSource<JavaRecordSource>> result = new ArrayList<>();
      List<BodyDeclaration> bodyDeclarations = getDeclaration().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof Initializer) {
             Initializer initializer = (Initializer) bodyDeclaration;
             result.add(new InitializerImpl<>(this, initializer));
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public InitializerSource<JavaRecordSource> addInitializer() 
   {
       InitializerSource<JavaRecordSource> init = new InitializerImpl<>(this);
       getDeclaration().bodyDeclarations().add(init.getInternal());
       return init;
   }

   @Override
   @SuppressWarnings("unchecked")
   public InitializerSource<JavaRecordSource> addInitializer(final String initializer) 
   {
      InitializerSource<JavaRecordSource> init = new InitializerImpl<>(this, initializer);
      getDeclaration().bodyDeclarations().add(init.getInternal());
      return init;
   }

   @Override
   public JavaRecordSource removeInitializer(org.jboss.forge.roaster.model.Initializer<JavaRecordSource, ?> initializer) 
   {
      getDeclaration().bodyDeclarations().remove(initializer.getInternal());
      return this;
   }
}
