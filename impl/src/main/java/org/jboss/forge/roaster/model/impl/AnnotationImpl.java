/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ValuePair;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Assert;
import org.jboss.forge.roaster.model.util.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class AnnotationImpl<O extends JavaSource<O>, T> implements AnnotationSource<O>
{
   private class Nested extends AnnotationImpl<O, T>
   {
      Nested(AnnotationImpl<O, T> owner)
      {
         super(owner.parent);
      }

      Nested(AnnotationImpl<O, T> owner, Object internal)
      {
         super(owner.parent, internal);
      }

      @Override
      protected void replace(org.eclipse.jdt.core.dom.Annotation oldNode, org.eclipse.jdt.core.dom.Annotation newNode)
      {
         if (oldNode.getParent() instanceof SingleMemberAnnotation)
         {
            ((SingleMemberAnnotation) oldNode.getParent()).setValue(newNode);
         }
         else
         {
            ((MemberValuePair) oldNode.getParent()).setValue(newNode);
         }
      }

   }

   private static final String DEFAULT_VALUE = "value";

   private AnnotationTargetSource<O, T> parent = null;
   private AST ast = null;
   private org.eclipse.jdt.core.dom.Annotation annotation;

   public enum AnnotationType
   {
      MARKER, SINGLE, NORMAL
   }

   public AnnotationImpl(final AnnotationTargetSource<O, T> parent)
   {
      this(parent, AnnotationType.MARKER);
   }

   public AnnotationImpl(final AnnotationTargetSource<O, T> parent, final Object internal)
   {
      this.parent = parent;
      ast = ((ASTNode) parent.getInternal()).getAST();
      annotation = (org.eclipse.jdt.core.dom.Annotation) internal;
   }

   public AnnotationImpl(final AnnotationTargetSource<O, T> parent, final AnnotationType type)
   {
      this(parent, createAnnotation(parent, type));
   }

   private static org.eclipse.jdt.core.dom.Annotation createAnnotation(final AnnotationTargetSource<?, ?> parent,
            final AnnotationType type)
   {
      AST ast = ((ASTNode) parent.getInternal()).getAST();
      switch (type)
      {
      case MARKER:
         return ast.newMarkerAnnotation();
      case SINGLE:
         return ast.newSingleMemberAnnotation();
      case NORMAL:
         return ast.newNormalAnnotation();
      default:
         throw new IllegalArgumentException("Unknown annotation type: " + type);
      }
   }

   @Override
   public String getName()
   {
      return annotation.getTypeName().getFullyQualifiedName();
   }

   @Override
   public String getQualifiedName()
   {
      return parent.getOrigin().resolveType(getName());
   }

   @Override
   public String getLiteralValue() throws IllegalStateException
   {
      String result = null;
      if (isSingleValue())
      {
         SingleMemberAnnotation sm = (SingleMemberAnnotation) annotation;
         result = sm.getValue().toString();
      }
      else if (isNormal())
      {
         List<ValuePair> values = getValues();
         for (ValuePair pair : values)
         {
            String name = pair.getName();
            if (DEFAULT_VALUE.equals(name))
            {
               result = pair.getLiteralValue();
               break;
            }
         }
      }
      return result;
   }

   @Override
   public String getLiteralValue(final String name)
   {
      String result = null;
      if (isNormal())
      {
         for (Object v : ((NormalAnnotation) annotation).values())
         {
            if (v instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) v;
               if (pair.getName().getFullyQualifiedName().equals(name))
               {
                  result = pair.getValue().toString();
                  break;
               }
            }
         }
      }
      else if (DEFAULT_VALUE.equals(name) && isSingleValue())
      {
         return getLiteralValue();
      }
      return result;
   }

   @Override
   public List<ValuePair> getValues()
   {
      List<ValuePair> result = new ArrayList<ValuePair>();
      if (isNormal())
      {
         for (Object v : ((NormalAnnotation) annotation).values())
         {
            if (v instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) v;
               ValuePair temp = new ValuePairImpl(pair.getName().getFullyQualifiedName(), pair.getValue().toString());
               result.add(temp);
            }
         }
      }
      else if (isSingleValue())
      {
         result.add(new ValuePairImpl(DEFAULT_VALUE, getLiteralValue()));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public String getStringValue() throws IllegalStateException
   {
      return Strings.unquote(getLiteralValue());
   }

   @Override
   public String getStringValue(final String name)
   {
      return Strings.unquote(getLiteralValue(name));
   }

   @Override
   public boolean isMarker()
   {
      return annotation.isMarkerAnnotation();
   }

   @Override
   public boolean isNormal()
   {
      return annotation.isNormalAnnotation();
   }

   @Override
   public boolean isSingleValue()
   {
      return annotation.isSingleMemberAnnotation();
   }

   @Override
   public AnnotationSource<O> removeAllValues()
   {
      convertTo(AnnotationType.MARKER);
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public AnnotationSource<O> removeValue(final String name)
   {
      if (annotation.isNormalAnnotation())
      {
         NormalAnnotation na = (NormalAnnotation) annotation;

         List<MemberValuePair> toBeRemoved = new ArrayList<MemberValuePair>();
         for (Object v : na.values())
         {
            if (v instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) v;
               if (pair.getName().toString().equals(name))
               {
                  toBeRemoved.add(pair);
               }
            }
         }
         na.values().removeAll(toBeRemoved);

         if ((getLiteralValue() != null) && (getValues().size() == 1))
         {
            convertTo(AnnotationType.SINGLE);
         }
         else if (getValues().size() == 0)
         {
            convertTo(AnnotationType.MARKER);
         }
      }
      else if (annotation.isSingleMemberAnnotation())
      {
         removeAllValues();
      }
      return this;
   }

   @Override
   public AnnotationSource<O> setName(final String className)
   {
      annotation.setTypeName(ast.newName(className));
      return this;
   }

   @Override
   public AnnotationSource<O> setLiteralValue(final String value)
   {
      Assert.notNull(value, "null not accepted");

      if (isMarker())
      {
         convertTo(AnnotationType.SINGLE);
      }

      if (isSingleValue())
      {
         SingleMemberAnnotation sa = (SingleMemberAnnotation) annotation;

         String stub = "@" + getName() + "(" + value + ") public class Stub { }";
         JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);

         SingleMemberAnnotation anno = (SingleMemberAnnotation) temp.getAnnotations().get(0).getInternal();

         Expression expression = anno.getValue();
         sa.setValue((Expression) ASTNode.copySubtree(ast, expression));
      }
      else
      {
         setLiteralValue(DEFAULT_VALUE, value);
      }

      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public AnnotationSource<O> setLiteralValue(final String name, final String value)
   {
      Assert.notNull(value, "null not accepted");

      if (!isNormal() && !DEFAULT_VALUE.equals(name))
      {
         convertTo(AnnotationType.NORMAL);
      }
      else if (!isSingleValue() && !isNormal() && DEFAULT_VALUE.equals(name))
      {
         convertTo(AnnotationType.SINGLE);
      }
      if (isSingleValue() && DEFAULT_VALUE.equals(name))
      {
         return setLiteralValue(value);
      }

      NormalAnnotation na = (NormalAnnotation) annotation;

      String stub = "@" + getName() + "(" + name + "=" + value + " ) public class Stub { }";
      JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);

      NormalAnnotation anno = (NormalAnnotation) temp.getAnnotations().get(0).getInternal();
      MemberValuePair mvp = (MemberValuePair) anno.values().get(0);

      List<MemberValuePair> values = na.values();
      ListIterator<MemberValuePair> iter = values.listIterator();
      while (iter.hasNext())
      {
         if (iter.next().getName().getIdentifier().equals(name))
         {
            iter.remove();
            break;
         }
      }
      iter.add((MemberValuePair) ASTNode.copySubtree(annotation.getAST(), mvp));

      return this;
   }

   @Override
   public AnnotationSource<O> setStringValue(final String value)
   {
      return setLiteralValue(Strings.enquote(value));
   }

   @Override
   public AnnotationSource<O> setStringValue(final String name, final String value)
   {
      return setLiteralValue(name, Strings.enquote(value));
   }

   @Override
   public <E extends Enum<E>> E getEnumValue(final Class<E> type)
   {
      String literalValue = getLiteralValue();
      return convertLiteralToEnum(type, literalValue);
   }

   @Override
   public <E extends Enum<E>> E getEnumValue(final Class<E> type, final String name)
   {
      String literalValue = getLiteralValue(name);
      return convertLiteralToEnum(type, literalValue);
   }

   private <E extends Enum<E>> E convertLiteralToEnum(final Class<E> type, String literalValue)
   {
      E[] constants = type.getEnumConstants();

      for (E inst : constants)
      {
         String[] tokens = literalValue.split("\\.");
         if (tokens.length > 1)
         {
            literalValue = tokens[tokens.length - 1];
         }

         if (inst.name().equals(literalValue))
         {
            return inst;
         }
      }
      return null;
   }

   @Override
   public AnnotationSource<O> setEnumValue(final String name, final Enum<?> value)
   {
      return setEnumArrayValue(name, value);
   }

   @Override
   public AnnotationSource<O> setEnumValue(final Enum<?>... values)
   {
      return setEnumArrayValue(values);
   }

   @Override
   public AnnotationSource<O> setEnumArrayValue(Enum<?>... values)
   {
      return setEnumArrayValue(DEFAULT_VALUE, values);
   }

   @Override
   public AnnotationSource<O> setEnumArrayValue(String name, final Enum<?>... values)
   {
      Assert.notNull(values, "null array not accepted");

      final List<String> literals = new ArrayList<String>();

      for (Enum<?> value : values)
      {
         Assert.notNull(value, "null value not accepted");
         getOrigin().addImport(value.getDeclaringClass());
         literals.add(value.getDeclaringClass().getSimpleName() + "." + value.name());
      }
      return setLiteralValue(name,
               literals.size() == 1 ? literals.get(0) : String.format("{%s}", Strings.join(literals, ",")));
   }

   /*
    * Shared interface methods.
    */
   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return annotation;
   }

   @Override
   public String toString()
   {
      return annotation.toString();
   }

   @SuppressWarnings("unchecked")
   protected void replace(org.eclipse.jdt.core.dom.Annotation oldNode, org.eclipse.jdt.core.dom.Annotation newNode)
   {
      List<IExtendedModifier> modifiers;
      ASTNode parentNode = oldNode.getParent();
      if (parentNode instanceof BodyDeclaration)
      {
         modifiers = ((BodyDeclaration) parentNode).modifiers();
      }
      else if (parentNode instanceof SingleVariableDeclaration)
      {
         modifiers = ((SingleVariableDeclaration) parentNode).modifiers();
      }
      else
      {
         throw new IllegalStateException("Cannot handle annotations attached to " + parentNode);
      }

      int pos = modifiers.indexOf(annotation);
      if (pos >= 0)
      {
         modifiers.set(pos, newNode);
      }
   }

   private void convertTo(final AnnotationType type)
   {
      String value = this.getLiteralValue();
      AnnotationImpl<O, T> na = new AnnotationImpl<O, T>(parent, type);
      na.setName(getName());
      replace(annotation, na.annotation);
      annotation = na.annotation;

      if (AnnotationType.MARKER != type && (value != null))
      {
         setLiteralValue(value);
      }
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((annotation == null) ? 0 : annotation.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      AnnotationImpl<?, ?> other = (AnnotationImpl<?, ?>) obj;
      if (annotation == null)
      {
         if (other.annotation != null)
         {
            return false;
         }
      }
      else if (!annotation.equals(other.annotation))
      {
         return false;
      }
      return true;
   }

   @Override
   public AnnotationSource<O> setAnnotationValue()
   {
      if (isMarker())
      {
         convertTo(AnnotationType.SINGLE);
      }

      if (isSingleValue())
      {
         final AnnotationSource<O> result = new Nested(this);
         ((SingleMemberAnnotation) annotation).setValue((Expression) result.getInternal());
         return result;
      }
      return setAnnotationValue(DEFAULT_VALUE);
   }

   @Override
   public AnnotationSource<O> setAnnotationValue(String name)
   {
      if (!isNormal() && DEFAULT_VALUE.equals(name))
      {
         return setAnnotationValue();
      }
      if (!isNormal())
      {
         convertTo(AnnotationType.NORMAL);
      }
      AnnotationSource<O> result = new Nested(this);

      String stub = "@" + getName() + "(" + name + "= 0 ) public class Stub { }";
      JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);

      NormalAnnotation anno = (NormalAnnotation) temp.getAnnotations().get(0).getInternal();
      MemberValuePair mvp = (MemberValuePair) anno.values().get(0);

      @SuppressWarnings("unchecked")
      List<MemberValuePair> values = ((NormalAnnotation) annotation).values();
      ListIterator<MemberValuePair> iter = values.listIterator();
      while (iter.hasNext())
      {
         if (iter.next().getName().getIdentifier().equals(name))
         {
            iter.remove();
            break;
         }
      }
      MemberValuePair mvpCopy = (MemberValuePair) ASTNode.copySubtree(annotation.getAST(), mvp);
      mvpCopy.setValue((Expression) result.getInternal());
      iter.add(mvpCopy);

      return result;
   }

   @Override
   public AnnotationSource<O> getAnnotationValue()
   {
      if (isSingleValue())
      {
         SingleMemberAnnotation single = (SingleMemberAnnotation) annotation;
         Expression value = single.getValue();
         if (value instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            return new Nested(this, value);
         }
      }
      if (isNormal())
      {
         return getAnnotationValue(DEFAULT_VALUE);
      }
      return null;
   }

   @Override
   public AnnotationSource<O> getAnnotationValue(String name)
   {
      if (isNormal())
      {
         NormalAnnotation normal = (NormalAnnotation) annotation;
         @SuppressWarnings("unchecked")
         List<MemberValuePair> values = normal.values();
         for (MemberValuePair memberValuePair : values)
         {
            if (Strings.areEqual(name, memberValuePair.getName().getIdentifier()))
            {
               return new Nested(this, memberValuePair.getValue());
            }
         }
      }
      if (isSingleValue() && DEFAULT_VALUE.equals(name))
      {
         return getAnnotationValue();
      }
      return null;
   }

   @Override
   public AnnotationSource<O>[] getAnnotationArrayValue()
   {
      return getAnnotationArrayValue(DEFAULT_VALUE);
   }
   
   @Override
   public AnnotationSource<O>[] getAnnotationArrayValue(String name)
   {
      final Expression expr = getElementValueExpression(name);
      if (expr instanceof ArrayInitializer)
      {
         final List<AnnotationSource<O>> results = new ArrayList<AnnotationSource<O>>();
         @SuppressWarnings("unchecked")
         final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
         for (Expression arrayElement : arrayElements)
         {
            final AnnotationSource<O> instance = new Nested(this, arrayElement);
            results.add(instance);
         }
         @SuppressWarnings("unchecked")
         final AnnotationSource<O>[] result = new AnnotationSource[results.size()];
         return results.toArray(result);
      }
      return null;
   }

   
   @Override
   public <E extends Enum<E>> E[] getEnumArrayValue(Class<E> type)
   {
      return getEnumArrayValue(type, DEFAULT_VALUE);
   }

   @Override
   public <E extends Enum<E>> E[] getEnumArrayValue(Class<E> type, String name)
   {
      final Expression expr = getElementValueExpression(name);
      if (expr instanceof ArrayInitializer)
      {
         final List<E> results = new ArrayList<E>();
         @SuppressWarnings("unchecked")
         final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
         for (Expression arrayElement : arrayElements)
         {
            final E instance = convertLiteralToEnum(type, arrayElement.toString());
            results.add(instance);
         }
         @SuppressWarnings("unchecked")
         final E[] result = (E[]) Array.newInstance(type, results.size());
         return results.toArray(result);
      }
      else if (expr != null)
      {
         final E instance = convertLiteralToEnum(type, expr.toString());
         if (type.isInstance(instance))
         {
            @SuppressWarnings("unchecked")
            final E[] result = (E[]) Array.newInstance(type, 1);
            result[0] = instance;
            return result;
         }
      }
      return null;
   }

   @Override
   public Class<?> getClassValue()
   {
      return getClassValue(DEFAULT_VALUE);
   }

   @Override
   public Class<?> getClassValue(String name)
   {
      final TypeLiteral typeLiteral = getElementValueExpression(name);
      return resolveTypeLiteral(typeLiteral);
   }

   @Override
   public Class<?>[] getClassArrayValue()
   {
      return getClassArrayValue(DEFAULT_VALUE);
   }

   @Override
   public Class<?>[] getClassArrayValue(String name)
   {
      final Expression expr = getElementValueExpression(name);
      if (expr instanceof ArrayInitializer)
      {
         final List<Class<?>> result = new ArrayList<Class<?>>();
         @SuppressWarnings("unchecked")
         final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
         for (Expression expression : arrayElements)
         {
            final Class<?> type = resolveTypeLiteral((TypeLiteral) expression);
            result.add(type);
         }
         return result.toArray(new Class[result.size()]);
      }
      else if (expr instanceof TypeLiteral)
      {
         return new Class[] { resolveTypeLiteral((TypeLiteral) expr) };
      }
      return null;
   }

   @Override
   public String[] getStringArrayValue()
   {
      return getStringArrayValue(DEFAULT_VALUE);
   }

   @Override
   public String[] getStringArrayValue(String name)
   {
      final List<String> result = new ArrayList<String>();
      String literalValue = getLiteralValue(name);
      // Remove {}
      if (literalValue.startsWith("{") && literalValue.endsWith("}"))
      {
         literalValue = literalValue.substring(1, literalValue.length() - 1);
      }
      for (String value : literalValue.split(","))
      {
         result.add(Strings.unquote(value));
      }
      return result.toArray(new String[result.size()]);
   }

   @Override
   public AnnotationSource<O> setClassValue(String name, Class<?> value)
   {
      Assert.notNull(value, "null not accepted");

      if (!value.isPrimitive())
      {
         getOrigin().addImport(value);
      }
      return setLiteralValue(name, value.getSimpleName() + ".class");
   }

   @Override
   public AnnotationSource<O> setClassValue(Class<?> value)
   {
      return setClassValue(DEFAULT_VALUE, value);
   }

   @Override
   public AnnotationSource<O> setClassArrayValue(Class<?>... values)
   {
      return setClassArrayValue(DEFAULT_VALUE, values);
   }

   @Override
   public AnnotationSource<O> setStringArrayValue(String[] values)
   {
      return setStringArrayValue(DEFAULT_VALUE, values);
   }

   @Override
   public AnnotationSource<O> setStringArrayValue(String name, String[] values)
   {
      Assert.notNull(values, "null array not accepted");

      final List<String> literals = new ArrayList<String>();

      for (String value : values)
      {
         Assert.notNull(value, "null value not accepted");
         literals.add(Strings.enquote(value));
      }
      return setLiteralValue(name,
               literals.size() == 1 ? literals.get(0) : String.format("{%s}", Strings.join(literals, ",")));
   }

   @Override
   public AnnotationSource<O> setClassArrayValue(String name, Class<?>... values)
   {
      Assert.notNull(values, "null array not accepted");

      final List<String> literals = new ArrayList<String>();

      for (Class<?> value : values)
      {
         Assert.notNull(value, "null value not accepted");

         if (!value.isPrimitive())
         {
            getOrigin().addImport(value);
         }
         literals.add(value.getSimpleName() + ".class");
      }
      return setLiteralValue(name,
               literals.size() == 1 ? literals.get(0) : String.format("{%s}", Strings.join(literals, ",")));
   }

   private <E extends Expression> E getElementValueExpression(String name)
   {
      if (isSingleValue() && DEFAULT_VALUE.equals(name))
      {
         @SuppressWarnings("unchecked")
         final E result = (E) ((SingleMemberAnnotation) annotation).getValue();
         return result;
      }
      if (isNormal())
      {
         for (Object v : ((NormalAnnotation) annotation).values())
         {
            if (v instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) v;
               if (pair.getName().getFullyQualifiedName().equals(name))
               {
                  @SuppressWarnings("unchecked")
                  final E result = (E) pair.getValue();
                  return result;
               }
            }
         }
      }
      return null;
   }

   private Class<?> resolveTypeLiteral(TypeLiteral typeLiteral)
   {
      final Type<O> type = new TypeImpl<O>(getOrigin(), typeLiteral.getType());
      if (type.isPrimitive())
      {
         final Class<?>[] primitiveTypes = { boolean.class, byte.class, short.class, int.class, long.class,
                  float.class, double.class };
         for (Class<?> c : primitiveTypes)
         {
            if (c.getSimpleName().equals(type.getName()))
            {
               return c;
            }
         }
         return null;
      }

      final String classname = type.getQualifiedName();

      try
      {
         return Class.forName(getOrigin().resolveType(classname));
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }

    public boolean containsKey( String name )
    {
        List<ValuePair> values = getValues();
        for (ValuePair pair : values)
        {
            if ( pair.getName().equals( name ) )
            {
                return true;
            }
        }
        return false;
    }
}
