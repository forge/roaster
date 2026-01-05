/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.RecordDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Initializer;
import org.jboss.forge.roaster.model.JavaRecordComponent;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.InitializerAccessor;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.util.Types;

import java.util.ArrayList;
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
   public List<InitializerSource<JavaRecordSource>> getInitializers() 
   {
      return InitializerAccessor.getInitializers(this, getDeclaration());
   }
   
   @Override
   public boolean hasInitializer(Initializer<JavaRecordSource, ?> initializer)
   {
      return InitializerAccessor.hasInitializer(getDeclaration(), initializer);
   }

   @Override
   public InitializerSource<JavaRecordSource> addInitializer() 
   {
      return InitializerAccessor.addInitializer(this, getDeclaration());
   }

   @Override
   public InitializerSource<JavaRecordSource> addInitializer(final String initializer) 
   {
      return InitializerAccessor.addInitializer(this, getDeclaration(), initializer);
   }

   @Override
   public JavaRecordSource removeInitializer(org.jboss.forge.roaster.model.Initializer<JavaRecordSource, ?> initializer) 
   {
      InitializerAccessor.removeInitializer(getDeclaration(), initializer);
      return this;
   }
}
