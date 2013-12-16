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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaInterface;
import org.jboss.forge.parser.java.JavaType;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.Parameter;
import org.jboss.forge.parser.java.ast.MethodFinderVisitor;
import org.jboss.forge.parser.java.source.FieldHolderSource;
import org.jboss.forge.parser.java.source.FieldSource;
import org.jboss.forge.parser.java.source.Import;
import org.jboss.forge.parser.java.source.InterfaceCapableSource;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.jboss.forge.parser.java.source.JavaSource;
import org.jboss.forge.parser.java.source.MemberSource;
import org.jboss.forge.parser.java.source.MethodHolderSource;
import org.jboss.forge.parser.java.source.MethodSource;
import org.jboss.forge.parser.java.source.ParameterSource;
import org.jboss.forge.parser.java.util.Strings;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractJavaSourceMemberHolder<O extends JavaSource<O>> extends AbstractJavaSource<O>
         implements InterfaceCapableSource<O>,
         MethodHolderSource<O>, FieldHolderSource<O>
{
   protected AbstractJavaSourceMemberHolder(JavaSource<?> enclosingType, final Document document,
            final CompilationUnit unit, BodyDeclaration declaration)
   {
      super(enclosingType, document, unit, declaration);
   }

   /*
    * Field & Method modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public FieldSource<O> addField()
   {
      FieldSource<O> field = new FieldImpl<O>((O) this);
      addField(field);
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public FieldSource<O> addField(final String declaration)
   {
      String stub = "public class Stub { " + declaration + " }";
      JavaClassSource temp = (JavaClassSource) JavaParser.parse(stub);
      List<FieldSource<JavaClassSource>> fields = temp.getFields();
      FieldSource<O> result = null;
      for (FieldSource<JavaClassSource> stubField : fields)
      {
         Object variableDeclaration = stubField.getInternal();
         FieldSource<O> field = new FieldImpl<O>((O) this, variableDeclaration, true);
         addField(field);
         if (result == null)
         {
            result = field;
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   private void addField(Field<O> field)
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
   public List<MemberSource<O, ?>> getMembers()
   {
      List<MemberSource<O, ?>> result = new ArrayList<MemberSource<O, ?>>();

      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<FieldSource<O>> getFields()
   {
      List<FieldSource<O>> result = new ArrayList<FieldSource<O>>();

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
   public FieldSource<O> getField(final String name)
   {
      for (FieldSource<O> field : getFields())
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
      for (FieldSource<O> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final Field<O> field)
   {
      return getFields().contains(field);
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeField(final Field<O> field)
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
   public boolean hasMethod(final Method<O, ?> method)
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
   public MethodSource<O> getMethod(final String name)
   {
      for (MethodSource<O> method : getMethods())
      {
         if (method.getName().equals(name) && (method.getParameters().size() == 0))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public MethodSource<O> getMethod(final String name, final String... paramTypes)
   {
      for (MethodSource<O> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            List<ParameterSource<O>> localParams = local.getParameters();
            if (paramTypes != null)
            {
               if (localParams.isEmpty() || (localParams.size() == paramTypes.length))
               {
                  boolean matches = true;
                  for (int i = 0; i < localParams.size(); i++)
                  {
                     ParameterSource<O> localParam = localParams.get(i);
                     String type = paramTypes[i];
                     if (!Types.areEquivalent(localParam.getType().getName(), type))
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
   public MethodSource<O> getMethod(final String name, Class<?>... paramTypes)
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
   public boolean hasMethodSignature(final Method<?, ?> method)
   {
      for (MethodSource<O> local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            Iterator<ParameterSource<O>> localParams = local.getParameters().iterator();
            for (Parameter<? extends JavaType<?>> methodParam : method.getParameters())
            {
               if (localParams.hasNext() && Strings.areEqual(localParams.next().getType().getName(), methodParam.getType().getName()))
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
   public O removeMethod(final Method<O, ?> method)
   {
      getBodyDeclaration().bodyDeclarations().remove(method.getInternal());
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod()
   {
      MethodSource<O> m = new MethodImpl<O>((O) this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(final String method)
   {
      MethodSource<O> m = new MethodImpl<O>((O) this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<MethodSource<O>> getMethods()
   {
      List<MethodSource<O>> result = new ArrayList<MethodSource<O>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<O>((O) this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public List<String> getInterfaces()
   {
      List<String> result = new ArrayList<String>();
      List<Type> superTypes = JDTHelper.getInterfaces(getBodyDeclaration());
      for (Type type : superTypes)
      {
         String name = JDTHelper.getTypeName(type);
         if (Types.isSimpleName(name) && this.hasImport(name))
         {
            Import imprt = this.getImport(name);
            String pkg = imprt.getPackage();
            if (!Strings.isNullOrEmpty(pkg))
            {
               name = pkg + "." + name;
            }
         }
         result.add(name);
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O addInterface(final String type)
   {
      if (!this.hasInterface(type))
      {
         Type interfaceType = JDTHelper.getInterfaces(
                  JavaParser.parse(JavaInterfaceImpl.class,
                           "public interface Mock extends " + Types.toSimpleName(type)
                                    + " {}").getBodyDeclaration()).get(0);
   
         if (this.hasInterface(Types.toSimpleName(type)) || this.hasImport(Types.toSimpleName(type)))
         {
            interfaceType = JDTHelper.getInterfaces(JavaParser.parse(JavaInterfaceImpl.class,
                     "public interface Mock extends " + type + " {}").getBodyDeclaration()).get(0);
         }
   
         this.addImport(type);
   
         ASTNode node = ASTNode.copySubtree(unit.getAST(), interfaceType);
         JDTHelper.getInterfaces(getBodyDeclaration()).add((Type) node);
      }
      return (O) this;
   }

   @Override
   public O addInterface(final Class<?> type)
   {
      return addInterface(type.getName());
   }

   @Override
   public O addInterface(final JavaInterface<?> type)
   {
      return addInterface(type.getQualifiedName());
   }

   @Override
   public boolean hasInterface(final String type)
   {
      for (String name : getInterfaces())
      {
         if (Types.areEquivalent(name, type))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasInterface(final Class<?> type)
   {
      return hasInterface(type.getName());
   }

   @Override
   public boolean hasInterface(final JavaInterface<?> type)
   {
      return hasInterface(type.getQualifiedName());
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeInterface(final String type)
   {
      List<Type> interfaces = JDTHelper.getInterfaces(getBodyDeclaration());
      for (Type i : interfaces)
      {
         if (Types.areEquivalent(i.toString(), type))
         {
            interfaces.remove(i);
            break;
         }
      }
      return (O) this;
   }

   @Override
   public O removeInterface(final Class<?> type)
   {
      return removeInterface(type.getName());
   }

   @Override
   public O removeInterface(final JavaInterface<?> type)
   {
      return removeInterface(type.getQualifiedName());
   }
}
