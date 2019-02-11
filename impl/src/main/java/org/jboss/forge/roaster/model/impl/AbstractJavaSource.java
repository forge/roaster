/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.StaticCapableSource;
import org.jboss.forge.roaster.model.source.TypeHolderSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.spi.JavaParserImpl;

/**
 * Represents a Java Source File which allow types
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@SuppressWarnings("unchecked")

public abstract class AbstractJavaSource<O extends JavaSource<O>> extends JavaSourceImpl<O> implements
         TypeHolderSource<O>, StaticCapableSource<O>
{
   protected final BodyDeclaration body;
   private final ModifierAccessor modifiers = new ModifierAccessor();

   protected AbstractJavaSource(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit);
      this.body = body;
   }

   /*
    * Interfaced Methods
    */

   @Override
   public List<JavaSource<?>> getNestedTypes()
   {
      List<AbstractTypeDeclaration> declarations = getNestedDeclarations(body);

      List<JavaSource<?>> result = new ArrayList<>();
      for (AbstractTypeDeclaration declaration : declarations)
      {
         result.add(JavaParserImpl.getJavaSource(this, document, unit, declaration));
      }
      return result;
   }

   @Override
   public boolean hasNestedType(JavaType<?> type)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getQualifiedName(), type.getQualifiedName())
                  || Strings.areEqual(nested.getName(), type.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasNestedType(String name)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getName(), name) || Strings.areEqual(nested.getQualifiedName(), name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasNestedType(Class<?> type)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getName(), type.getSimpleName())
                  || Strings.areEqual(nested.getQualifiedName(), type.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public JavaSource<?> getNestedType(String name)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getName(), name) || Strings.areEqual(nested.getQualifiedName(), name))
         {
            return nested;
         }
      }
      return null;
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(NESTED_TYPE type)
   {
      if (type instanceof AbstractJavaSource)
      {
         List<Object> bodyDeclarations = getDeclaration().bodyDeclarations();
         BodyDeclaration nestedBody = ((AbstractJavaSource<?>) type).body;
         bodyDeclarations.add(ASTNode.copySubtree(unit.getAST(), nestedBody));
      }
      else
      {
         throw new IllegalArgumentException("type must be an AbstractJavaSource instance");
      }
      return (NESTED_TYPE) getNestedType(type.getName());
   }

   @Override
   public O removeNestedType(JavaSource<?> type)
   {
      if (type instanceof AbstractJavaSource)
      {
         BodyDeclaration bodyDeclaration = ((AbstractJavaSource<?>) type).body;
         List<Object> bodyDeclarations = getDeclaration().bodyDeclarations();
         bodyDeclarations.remove(bodyDeclaration);
      }
      return (O) this;
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(Class<NESTED_TYPE> type)
   {
      JavaSource<?> nestedType = Roaster.create(type);
      return (NESTED_TYPE) addNestedType(nestedType);
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(String declaration)
   {
      JavaSource<?> nestedType = Roaster.parse(JavaSource.class, declaration);
      return (NESTED_TYPE) addNestedType(nestedType);
   }

   @Override
   public boolean isStatic()
   {
      return modifiers.hasModifier(getDeclaration(), ModifierKeyword.STATIC_KEYWORD);
   }

   @Override
   public O setStatic(boolean _static)
   {
      if (_static)
      {
         modifiers.addModifier(getDeclaration(), ModifierKeyword.STATIC_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(getDeclaration(), ModifierKeyword.STATIC_KEYWORD);
      }
      return (O) this;
   }

   private List<AbstractTypeDeclaration> getNestedDeclarations(BodyDeclaration body)
   {
      TypeDeclarationFinderVisitor typeDeclarationFinder = new TypeDeclarationFinderVisitor();
      body.accept(typeDeclarationFinder);
      List<AbstractTypeDeclaration> declarations = typeDeclarationFinder.getTypeDeclarations();

      List<AbstractTypeDeclaration> result = new ArrayList<>(declarations);
      if (!declarations.isEmpty())
      {
         // We don't want to return the current class' declaration.
         result.remove(declarations.remove(0));
         for (AbstractTypeDeclaration declaration : declarations)
         {
            result.removeAll(getNestedDeclarations(declaration));
         }
      }

      return result;
   }

   @Override
   protected AbstractTypeDeclaration getDeclaration()
   {
      if (body instanceof AbstractTypeDeclaration)
         return (AbstractTypeDeclaration) body;
      throw new ParserException("Source body was not of the expected type.");
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + Objects.hash(body);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AbstractJavaSource<?> other = (AbstractJavaSource<?>) obj;
      if (body == null)
      {
         if (other.body != null)
            return false;
      }
      else if (!body.equals(other.body))
         return false;
      return super.equals(obj);
   }

   @SuppressWarnings("rawtypes")
   @Override
   public O setName(final String name)
   {
      AbstractTypeDeclaration typeDeclaration = getDeclaration();
      TypeImpl<O> type = new TypeImpl(this, null, name);

      typeDeclaration.setName(unit.getAST().newSimpleName(type.getName()));
      if (typeDeclaration instanceof TypeDeclaration)
      {
         TypeDeclaration td = (TypeDeclaration) typeDeclaration;
         for (Type<?> arg : type.getTypeArguments())
         {
            TypeParameter typeParameter = unit.getAST().newTypeParameter();
            typeParameter.setName(unit.getAST().newSimpleName(arg.getName()));
            td.typeParameters().add(typeParameter);
         }
      }
      return updateTypeNames(name);
   }

   @Override
   public String getName()
   {
      return getDeclaration().getName().getIdentifier();
   }

   @Override
   protected Javadoc getJDTJavaDoc()
   {
      return body.getJavadoc();
   }

   @Override
   protected void setJDTJavaDoc(Javadoc javaDoc)
   {
      body.setJavadoc(javaDoc);
   }
}