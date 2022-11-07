/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Property;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.source.PropertyHolderSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class AbstractJavaSourceMemberHolder<O extends JavaSource<O> & PropertyHolderSource<O>> extends AbstractJavaSource<O>
         implements InterfaceCapableSource<O>, PropertyHolderSource<O>
{
   private static final Pattern GET_SET_PATTERN = Pattern.compile("^[gs]et.+$");

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
      FieldSource<O> field = new FieldImpl<>((O) this);
      addField(field);
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public FieldSource<O> addField(final String declaration)
   {
      String stub = "public class Stub { " + declaration + " }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<FieldSource<JavaClassSource>> fields = temp.getFields();
      FieldSource<O> result = null;
      for (FieldSource<JavaClassSource> stubField : fields)
      {
         Object variableDeclaration = stubField.getInternal();
         FieldSource<O> field = new FieldImpl<>((O) this, variableDeclaration, true);
         addField(field);
         if (result == null)
         {
            result = field;
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   protected void addField(Field<O> field)
   {
      List<Object> bodyDeclarations = getDeclaration().bodyDeclarations();
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
      List<MemberSource<O, ?>> result = new ArrayList<>();

      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<FieldSource<O>> getFields()
   {
      List<FieldSource<O>> result = new ArrayList<>();

      List<BodyDeclaration> bodyDeclarations = getDeclaration().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof FieldDeclaration)
         {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
            List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
            for (VariableDeclarationFragment fragment : fragments)
            {
               result.add(new FieldImpl<>((O) this, fragment));
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
      Iterator<Object> declarationsIterator = getDeclaration().bodyDeclarations().iterator();
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
   @SuppressWarnings("unchecked")
   public O removeMethod(final Method<O, ?> method)
   {
      getDeclaration().bodyDeclarations().remove(method.getInternal());
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod()
   {
      MethodSource<O> m = new MethodImpl<>((O) this, document);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(final String method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method, document);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(java.lang.reflect.Method method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method, document);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(Method<?, ?> method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method.toString(), document);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<MethodSource<O>> getMethods()
   {
      List<MethodSource<O>> result = new ArrayList<>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<>((O) this, methodDeclaration, document));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public List<String> getInterfaces()
   {
      List<String> result = new ArrayList<>();
      List<Type> superTypes = JDTHelper.getInterfaces(getDeclaration());
      for (Type type : superTypes)
      {
         String name = JDTHelper.getTypeName(type);
         String rawName = Types.stripGenerics(name);
         if (Types.isSimpleName(rawName))
         {
            String pkg;
            Import imprt = getImport(rawName);
            if (imprt == null)
            {
               imprt = getImport(resolveType(rawName));
            }
            if (imprt != null)
            {
               pkg = imprt.getPackage();
            }
            else
            {
               pkg = this.getPackage();
            }
            if (!StringUtils.isEmpty(pkg))
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
      if (!hasInterface(type))
      {
         String typeName;
         if (addImport(type) != null)
         {
            typeName = Types.toSimpleName(type);
         }
         else
         {
            typeName = type;
         }
         AbstractTypeDeclaration declaration = getDeclaration();
         AST ast = getDeclaration().getAST();
         SimpleType simpleType = ast.newSimpleType(ast.newName(typeName));
         JDTHelper.getInterfaces(declaration).add(simpleType);
      }
      return (O) this;
   }


   @Override
   public O implementInterface(Class<?> type)
   {
      O obj = addInterface(type);
      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.setPublic().addAnnotation(Override.class);
      }
      return obj;
   }

   @Override
   public O implementInterface(JavaInterface<?> type)
   {
      O obj = addInterface(type);

      if (type instanceof JavaInterfaceSource)
      {
         Set<Import> usedImports = new HashSet<>();

         JavaInterfaceSource interfaceSource = (JavaInterfaceSource) type;
         for (MethodSource<JavaInterfaceSource> method : interfaceSource.getMethods())
         {
            if (method.isDefault())
            {
               // Do not add default implementations
               continue;
            }

            if (!method.isReturnTypeVoid())
            {
               usedImports.add(interfaceSource.getImport(method.getReturnType().getQualifiedName()));
            }

            for (ParameterSource<JavaInterfaceSource> parameter : method.getParameters())
            {
               usedImports.add(interfaceSource.getImport(parameter.getType().getQualifiedName()));
            }
         }

         for (Import imprt : interfaceSource.getImports())
         {
            if (usedImports.contains(imprt))
            {
               addImport(imprt);
            }
         }
      }

      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.setPublic().addAnnotation(Override.class);
      }
      return obj;
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

   @SuppressWarnings("unchecked")
   @Override
   public O removeInterface(final String type)
   {
      List<Type> interfaces = JDTHelper.getInterfaces(getDeclaration());
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
   public final boolean hasProperty(String name)
   {
      return getProperty(name) != null;
   }

   @Override
   public final boolean hasProperty(Property<O> property)
   {
      return getProperties().contains(property);
   }

   @Override
   public final PropertySource<O> addProperty(String type, String name)
   {
      if (hasProperty(name))
      {
         throw new IllegalStateException("Cannot create existing property " + name);
      }

      O origin = getOrigin();
      if (origin.requiresImport(type))
      {
         origin.addImport(type);
      }
      for (String genericType : Types.splitGenerics(type))
      {
         if (origin.requiresImport(genericType))
         {
            origin.addImport(genericType);
         }
      }
      final org.jboss.forge.roaster.model.Type<O> typeObject = new TypeImpl<>(getOrigin(), null,
               Types.toSimpleName(type));
      final PropertySource<O> result = new PropertyImpl<O>(name, getOrigin())
      {
         @Override
         public org.jboss.forge.roaster.model.Type<O> getType()
         {
            final org.jboss.forge.roaster.model.Type<O> result = super.getType();
            return result == null ? typeObject : result;
         }
      };

      if (!isInterface())
      {
         result.createField();
      }
      result.setAccessible(true);
      result.setMutable(!isEnum());

      return getProperty(name);
   }

   @Override
   public PropertySource<O> addProperty(Class<?> type, String name)
   {
      return addProperty(type.getCanonicalName(), name);
   }

   @Override
   public PropertySource<O> addProperty(JavaType<?> type, String name)
   {
      return addProperty(type.getQualifiedName(), name);
   }

   @Override
   public final AbstractJavaSourceMemberHolder<O> removeProperty(Property<O> property)
   {
      if (hasProperty(property))
      {
         getProperty(property.getName()).setMutable(false).setAccessible(false).removeField();
      }
      return this;
   }

   @Override
   public final PropertySource<O> getProperty(String name)
   {
      Objects.requireNonNull(name, "name is null");
      final PropertyImpl<O> result = new PropertyImpl<>(name, getOrigin());
      return result.isValid() ? result : null;
   }

   @Override
   public final List<PropertySource<O>> getProperties()
   {
      final Set<String> propertyNames = new LinkedHashSet<>();
      for (MethodSource<O> method : getMethods())
      {
         if (isAccessor(method) || isMutator(method))
         {
            propertyNames.add(extractPropertyName(method));
         }
      }
      for (FieldSource<O> field : getFields())
      {
         if (!field.isStatic())
         {
            propertyNames.add(field.getName());
         }
      }
      final List<PropertySource<O>> result = new ArrayList<>(propertyNames.size());
      for (String name : propertyNames)
      {
         result.add(new PropertyImpl<>(name, getOrigin()));
      }
      return result;
   }

   @Override
   public List<PropertySource<O>> getProperties(Class<?> type)
   {
      final Set<String> propertyNames = new LinkedHashSet<>();
      for (MethodSource<O> method : getMethods())
      {
         if ((isAccessor(method) || isMutator(method))
                  && method.getReturnType().getQualifiedName().equals(type.getCanonicalName()))
         {
            propertyNames.add(extractPropertyName(method));
         }
      }
      for (FieldSource<O> field : getFields())
      {
         if (!field.isStatic() && field.getType().getQualifiedName().equals(type.getCanonicalName()))
         {
            propertyNames.add(field.getName());
         }
      }
      final List<PropertySource<O>> result = new ArrayList<>(propertyNames.size());
      for (String name : propertyNames)
      {
         result.add(new PropertyImpl<>(name, getOrigin()));
      }
      return result;
   }

   private boolean isAccessor(Method<O, ?> method)
   {
      return extractPropertyName(method) != null && method.getParameters().isEmpty() && !method.isReturnTypeVoid();
   }

   private boolean isMutator(Method<O, ?> method)
   {
      return extractPropertyName(method) != null && method.getParameters().size() == 1 && method.isReturnTypeVoid();
   }

   private String extractPropertyName(Method<O, ?> method)
   {
      if (GET_SET_PATTERN.matcher(method.getName()).matches())
      {
         return StringUtils.uncapitalize(method.getName().substring(3));
      }
      return null;
   }
}
