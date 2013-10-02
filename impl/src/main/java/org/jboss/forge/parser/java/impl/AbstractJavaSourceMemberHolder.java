/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadField;
import org.jboss.forge.parser.java.ReadField.Field;
import org.jboss.forge.parser.java.ReadFieldHolder.FieldHolder;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.parser.java.ReadJavaSource;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.ReadMember.Member;
import org.jboss.forge.parser.java.ReadMethod;
import org.jboss.forge.parser.java.ReadMethod.Method;
import org.jboss.forge.parser.java.ReadMethodHolder.MethodHolder;
import org.jboss.forge.parser.java.ReadParameter;
import org.jboss.forge.parser.java.ReadParameter.Parameter;
import org.jboss.forge.parser.java.ast.MethodFinderVisitor;
import org.jboss.forge.parser.java.util.Strings;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractJavaSourceMemberHolder<O extends JavaSource<O>> extends AbstractJavaSource<O> implements
         MethodHolder<O>, FieldHolder<O>
{
   public AbstractJavaSourceMemberHolder(JavaSource<?> enclosingType, final Document document,
            final CompilationUnit unit, BodyDeclaration declaration)
   {
      super(enclosingType, document, unit, declaration);
   }

   /*
    * Field & Method modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public Field<O> addField()
   {
      Field<O> field = new FieldImpl<O>((O) this);
      addField(field);
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Field<O> addField(final String declaration)
   {
      String stub = "public class Stub { " + declaration + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Field<JavaClass>> fields = temp.getFields();
      Field<O> result = null;
      for (Field<JavaClass> stubField : fields)
      {
         Object variableDeclaration = stubField.getInternal();
         Field<O> field = new FieldImpl<O>((O) this, variableDeclaration, true);
         addField(field);
         if (result == null)
         {
            result = field;
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   private void addField(ReadField<O> field)
   {
      List<Object> bodyDeclarations = getBodyDeclaration().bodyDeclarations();
      int idx = 0;
      for (Object object : bodyDeclarations)
      {
         if (!(object instanceof FieldDeclaration))
         {
            break;
         }
         idx++;
      }
      bodyDeclarations.add(idx, ((VariableDeclarationFragment) field.getInternal()).getParent());
   }

   @Override
   public List<Member<O, ?>> getMembers()
   {
      List<Member<O, ?>> result = new ArrayList<Member<O, ?>>();

      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Field<O>> getFields()
   {
      List<Field<O>> result = new ArrayList<Field<O>>();

      List<BodyDeclaration> bodyDeclarations = getBodyDeclaration().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof FieldDeclaration)
         {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
            List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
            for (VariableDeclarationFragment fragment : fragments)
            {
               result.add(new FieldImpl<O>((O) this, fragment));
            }
         }
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<O> getField(final String name)
   {
      for (Field<O> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return field;
         }
      }
      return null;
   }

   @Override
   public boolean hasField(final String name)
   {
      for (Field<O> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final ReadField<O> field)
   {
      return getFields().contains(field);
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeField(final ReadField<O> field)
   {
      VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.getInternal();
      Iterator<Object> declarationsIterator = getBodyDeclaration().bodyDeclarations().iterator();
      while (declarationsIterator.hasNext())
      {
         Object next = declarationsIterator.next();
         if (next instanceof FieldDeclaration)
         {
            FieldDeclaration declaration = (FieldDeclaration) next;
            if (declaration.equals(fragment.getParent()))
            {
               List<VariableDeclarationFragment> fragments = declaration.fragments();
               if (fragments.contains(fragment))
               {
                  if (fragments.size() == 1)
                  {
                     declarationsIterator.remove();
                  }
                  else
                  {
                     fragments.remove(fragment);
                  }
                  break;
               }
            }
         }
      }
      return (O) this;
   }

   @Override
   public boolean hasMethod(final ReadMethod<O, ?> method)
   {
      return getMethods().contains(method);
   }

   @Override
   public boolean hasMethodSignature(final String name)
   {
      return hasMethodSignature(name, new String[] {});
   }

   @Override
   public boolean hasMethodSignature(final String name, final String... paramTypes)
   {
      return getMethod(name, paramTypes) != null;
   }

   @Override
   public boolean hasMethodSignature(final String name, Class<?>... paramTypes)
   {
      if (paramTypes == null)
      {
         paramTypes = new Class<?>[] {};
      }

      String[] types = new String[paramTypes.length];
      for (int i = 0; i < paramTypes.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return hasMethodSignature(name, types);
   }

   @Override
   public Method<O> getMethod(final String name)
   {
      for (Method<O> method : getMethods())
      {
         if (method.getName().equals(name) && (method.getParameters().size() == 0))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public Method<O> getMethod(final String name, final String... paramTypes)
   {
      for (Method<O> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            List<Parameter<O>> localParams = local.getParameters();
            if (paramTypes != null)
            {
               if (localParams.isEmpty() || (localParams.size() == paramTypes.length))
               {
                  boolean matches = true;
                  for (int i = 0; i < localParams.size(); i++)
                  {
                     Parameter<O> localParam = localParams.get(i);
                     String type = paramTypes[i];
                     if (!Types.areEquivalent(localParam.getType(), type))
                     {
                        matches = false;
                     }
                  }
                  if (matches)
                     return local;
               }
            }
         }
      }
      return null;
   }

   @Override
   public Method<O> getMethod(final String name, Class<?>... paramTypes)
   {
      if (paramTypes == null)
      {
         paramTypes = new Class<?>[] {};
      }

      String[] types = new String[paramTypes.length];
      for (int i = 0; i < paramTypes.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return getMethod(name, types);
   }

   @Override
   public boolean hasMethodSignature(final ReadMethod<?, ?> method)
   {
      for (Method<O> local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            Iterator<Parameter<O>> localParams = local.getParameters().iterator();
            for (ReadParameter<? extends ReadJavaSource<?>> methodParam : method.getParameters())
            {
               if (localParams.hasNext() && Strings.areEqual(localParams.next().getType(), methodParam.getType()))
               {
                  continue;
               }
               return false;
            }
            return !localParams.hasNext();
         }
      }
      return false;
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeMethod(final ReadMethod<O, ?> method)
   {
      getBodyDeclaration().bodyDeclarations().remove(method.getInternal());
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> addMethod()
   {
      Method<O> m = new MethodImpl<O>((O) this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> addMethod(final String method)
   {
      Method<O> m = new MethodImpl<O>((O) this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Method<O>> getMethods()
   {
      List<Method<O>> result = new ArrayList<Method<O>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<O>((O) this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }
}
