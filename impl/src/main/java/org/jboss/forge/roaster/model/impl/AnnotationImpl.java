/*
 * Copyright 2012-2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jboss.forge.roaster.Roaster;
import org.eclipse.jdt.core.dom.Annotation;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ValuePair;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Types;

import static java.util.Objects.requireNonNull;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class AnnotationImpl<O extends JavaSource<O>, T> implements AnnotationSource<O>
{
   private static final String MISSING = "MISSING";
   private static final String DEFAULT_VALUE = "value";

   private AST ast;
   private Annotation annotation;
   private AnnotationTargetSource<O, T> parent;

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
      protected void replace(Annotation oldNode, Annotation newNode)
      {
         if (oldNode.getParent() instanceof SingleMemberAnnotation)
         {
            ((SingleMemberAnnotation) oldNode.getParent()).setValue(newNode);
         }
         else if (oldNode.getParent() instanceof MemberValuePair)
         {
            ((MemberValuePair) oldNode.getParent()).setValue(newNode);
         }
         else if (oldNode.getParent() instanceof ArrayInitializer)
         {
            @SuppressWarnings("unchecked")
            final List<Annotation> expressions = ((ArrayInitializer) oldNode
                     .getParent()).expressions();
            expressions.set(expressions.indexOf(oldNode), newNode);
         }
      }
   }

   private static enum AnnotationType
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
      this.ast = ((ASTNode) parent.getInternal()).getAST();
      this.annotation = (Annotation) requireNonNull(internal);
   }

   public AnnotationImpl(final AnnotationTargetSource<O, T> parent, final AnnotationType type)
   {
      this(parent, createAnnotation(parent, type));
   }

   private static Annotation createAnnotation(final AnnotationTargetSource<?, ?> parent,
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
      Name name = annotation.getTypeName();
      if (name.getFullyQualifiedName().equals(MISSING))
      {
         return MISSING;
      }
      if (name.isSimpleName())
      {
         return name.getFullyQualifiedName();
      }
      return Types.toSimpleName(name.getFullyQualifiedName());
   }

   @Override
   public String getQualifiedName()
   {
      Name name = annotation.getTypeName();
      if (name.getFullyQualifiedName().equals(MISSING))
      {
         return MISSING;
      }
      if (name.isSimpleName())
      {
         return parent.getOrigin().resolveType(name.getFullyQualifiedName());
      }
      return name.getFullyQualifiedName();
   }

   @Override
   public String getLiteralValue()
   {
      if (isSingleValue())
      {
         Expression value = ((SingleMemberAnnotation) annotation).getValue();
         if (value instanceof TypeLiteral)
         {
            return resolveTypeLiteralName((TypeLiteral) value);
         }
         return value.toString();
      }
      if (isNormal())
      {
         for (ValuePair pair : getValues())
         {
            if (DEFAULT_VALUE.equals(pair.getName()))
            {
               return pair.getLiteralValue();
            }
         }
      }
      return null;
   }

   @Override
   public String getLiteralValue(final String name)
   {
      if (DEFAULT_VALUE.equals(name) && isSingleValue())
      {
         return getLiteralValue();
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
                  Expression value = pair.getValue();
                  if (value instanceof TypeLiteral)
                  {
                     return resolveTypeLiteralName((TypeLiteral) value);
                  }
                  return value.toString();
               }
            }
         }
      }
      return null;
   }

   @Override
   public List<ValuePair> getValues()
   {
      List<ValuePair> result = new ArrayList<>();
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
         NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;

         List<MemberValuePair> toRemove = new ArrayList<>();
         for (Object annotationValue : normalAnnotation.values())
         {
            if (annotationValue instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) annotationValue;
               if (pair.getName().toString().equals(name))
               {
                  toRemove.add(pair);
               }
            }
         }
         normalAnnotation.values().removeAll(toRemove);

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

   @SuppressWarnings("unchecked")
   @Override
   public AnnotationSource<O> setLiteralValue(final String value)
   {
      requireNonNull(value);

      if (isMarker())
      {
         convertTo(AnnotationType.SINGLE);
      }

      if (isSingleValue())
      {
         String stub = "@" + getName() + "(" + value + ") public class Stub { }";
         JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);

         Object internal = temp.getAnnotations().get(0).getInternal();
         if (internal instanceof SingleMemberAnnotation)
         {
            SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
            Expression expression = ((SingleMemberAnnotation) internal).getValue();
            singleMemberAnnotation.setValue((Expression) ASTNode.copySubtree(ast, expression));
         }
         else if (internal instanceof NormalAnnotation)
         {
            NormalAnnotation internalNormalAnnotation = (NormalAnnotation) internal;
            convertTo(AnnotationType.NORMAL);
            NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;

            for (MemberValuePair pair : (List<MemberValuePair>) internalNormalAnnotation.values())
            {
               normalAnnotation.values().add(ASTNode.copySubtree(annotation.getAST(), pair));
            }
         }
         else
         {
            throw new IllegalArgumentException(
                     "Type " + internal.getClass().getName() + " cannot be handled in this method");
         }
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
      requireNonNull(value);

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

      NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;

      String stub = "@" + getName() + "(" + name + "=" + value + " ) public class Stub { }";
      JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);

      NormalAnnotation anno = (NormalAnnotation) temp.getAnnotations().get(0).getInternal();
      MemberValuePair pair = (MemberValuePair) anno.values().get(0);

      List<MemberValuePair> values = normalAnnotation.values();
      ListIterator<MemberValuePair> iter = values.listIterator();
      while (iter.hasNext())
      {
         if (iter.next().getName().getIdentifier().equals(name))
         {
            iter.remove();
            break;
         }
      }
      iter.add((MemberValuePair) ASTNode.copySubtree(annotation.getAST(), pair));

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
      final List<String> literals = new ArrayList<>();

      for (Enum<?> value : requireNonNull(values))
      {
         Import imprt = getOrigin().addImport(requireNonNull(value).getDeclaringClass());
         if (imprt == null)
         {
            literals.add(value.getDeclaringClass().getCanonicalName() + "." + value.name());
         }
         else
         {
            literals.add(value.getDeclaringClass().getSimpleName() + "." + value.name());
         }
      }
      return setArrayLiteralValue(name, literals);
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
   protected void replace(Annotation oldNode, Annotation newNode)
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

      int annotationIndex = modifiers.indexOf(annotation);
      if (annotationIndex >= 0)
      {
         modifiers.set(annotationIndex, newNode);
      }
   }

   private void convertTo(final AnnotationType type)
   {
      if (isMarker() && type != AnnotationType.MARKER || isSingleValue() && type != AnnotationType.SINGLE || isNormal()
               && type != AnnotationType.NORMAL)
      {
         String value = this.getLiteralValue();
         AnnotationImpl<O, T> newAnnotation = new AnnotationImpl<>(parent, type);
         if (getOrigin().getImport(getQualifiedName()) != null)
         {
            newAnnotation.setName(getName());
         }
         else
         {
            newAnnotation.setName(getQualifiedName());
         }
         replace(annotation, newAnnotation.annotation);
         annotation = newAnnotation.annotation;

         if (AnnotationType.MARKER != type && (value != null))
         {
            setLiteralValue(value);
         }
      }
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(annotation);
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (!(obj instanceof AnnotationImpl<?, ?>))
      {
         return false;
      }

      AnnotationImpl<?, ?> other = (AnnotationImpl<?, ?>) obj;
      return annotation.equals(other.annotation);
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
      MemberValuePair pair = (MemberValuePair) anno.values().get(0);

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
      MemberValuePair mvpCopy = (MemberValuePair) ASTNode.copySubtree(annotation.getAST(), pair);
      mvpCopy.setValue((Expression) result.getInternal());
      iter.add(mvpCopy);

      return result;
   }

   @Override
   public AnnotationSource<O> addAnnotationValue()
   {
      if (isNormal())
      {
         return addAnnotationValue(DEFAULT_VALUE);
      }
      if (isMarker())
      {
         convertTo(AnnotationType.SINGLE);
      }
      Expression expr = ((SingleMemberAnnotation) annotation).getValue();
      if (expr instanceof Annotation)
      {
         // wrap a single annotation value:
         ArrayInitializer arrayInit = ast.newArrayInitializer();
         {
            @SuppressWarnings({ "unused", "unchecked" })
            boolean junk = arrayInit.expressions().add(ASTNode.copySubtree(ast, expr));
         }
         ((SingleMemberAnnotation) annotation).setValue(arrayInit);
         expr = arrayInit;
      }
      if (expr instanceof ArrayInitializer)
      {
         // append to an annotation array:
         final Annotation arrayElement = createAnnotation(parent, AnnotationType.MARKER);
         if (((ArrayInitializer) expr).expressions().isEmpty())
         {
            ((SingleMemberAnnotation) annotation).setValue(arrayElement);
         }
         else
         {
            @SuppressWarnings({ "unchecked", "unused" })
            boolean junk = ((ArrayInitializer) expr).expressions().add(arrayElement);
         }
         return new Nested(this, arrayElement);
      }
      // overwrite with a single annotation value:
      return setAnnotationValue();
   }

   @Override
   public AnnotationSource<O> addAnnotationValue(String name)
   {
      if (!isNormal())
      {
         if (DEFAULT_VALUE.equals(name))
         {
            return addAnnotationValue();
         }
         convertTo(AnnotationType.NORMAL);
      }
      MemberValuePair memberValuePair = null;

      for (Object value : ((NormalAnnotation) annotation).values())
      {
         if (value instanceof MemberValuePair
                  && Objects.equals(name, ((MemberValuePair) value).getName().getIdentifier()))
         {
            memberValuePair = (MemberValuePair) value;
            break;
         }
      }
      if (memberValuePair != null)
      {
         Expression expr = memberValuePair.getValue();
         if (expr instanceof Annotation)
         {
            // wrap a single annotation value:
            ArrayInitializer arrayInit = ast.newArrayInitializer();
            {
               @SuppressWarnings({ "unchecked", "unused" })
               boolean junk = arrayInit.expressions().add(ASTNode.copySubtree(ast, expr));
            }
            memberValuePair.setValue(arrayInit);
            expr = arrayInit;
         }
         if (expr instanceof ArrayInitializer)
         {
            // append to an annotation array:
            final Annotation arrayElement = createAnnotation(parent, AnnotationType.MARKER);
            if (((ArrayInitializer) expr).expressions().isEmpty())
            {
               memberValuePair.setValue(arrayElement);
            }
            else
            {
               @SuppressWarnings({ "unchecked", "unused" })
               boolean junk = ((ArrayInitializer) expr).expressions().add(arrayElement);
            }
            return new Nested(this, arrayElement);
         }
      }
      // overwrite with a single annotation value:
      return setAnnotationValue(name);
   }

   @Override
   public AnnotationSource<O> addAnnotationValue(Class<? extends java.lang.annotation.Annotation> type)
   {
      String typeToUse = getOrigin().addImport(type) != null ? type.getCanonicalName() : type.getSimpleName();
      return addAnnotationValue().setName(typeToUse);
   }

   @Override
   public AnnotationSource<O> addAnnotationValue(String name, Class<? extends java.lang.annotation.Annotation> type)
   {
      String typeToUse = getOrigin().addImport(type) != null ? type.getCanonicalName() : type.getSimpleName();
      return addAnnotationValue(name).setName(typeToUse);
   }

   @Override
   public AnnotationSource<O> removeAnnotationValue(org.jboss.forge.roaster.model.Annotation<O> element)
   {
      requireNonNull(element, "Cannot remove null element");

      if (isSingleValue())
      {
         if (element.getInternal().equals(((SingleMemberAnnotation) annotation).getValue()))
         {
            convertTo(AnnotationType.MARKER);
         }
         else if (((SingleMemberAnnotation) annotation).getValue() instanceof ArrayInitializer)
         {
            final ArrayInitializer arrayInit = (ArrayInitializer) ((SingleMemberAnnotation) annotation).getValue();
            if (arrayInit.expressions().remove(element.getInternal()))
            {
               if (arrayInit.expressions().isEmpty())
               {
                  convertTo(AnnotationType.MARKER);
               }
               else if (arrayInit.expressions().size() == 1)
               {
                  ((SingleMemberAnnotation) annotation).setValue((Expression) ASTNode.copySubtree(ast,
                           (ASTNode) arrayInit.expressions().get(0)));
               }
            }
         }
         return this;
      }
      return removeAnnotationValue(DEFAULT_VALUE, element);
   }

   @Override
   public AnnotationSource<O> removeAnnotationValue(String name, org.jboss.forge.roaster.model.Annotation<O> element)
   {
      requireNonNull(element, "Cannot remove null element");

      if (isSingleValue() && Objects.equals(name, DEFAULT_VALUE))
      {
         return removeAnnotationValue(element);
      }
      if (isNormal())
      {
         final Set<String> identifiers = new HashSet<>();
         for (@SuppressWarnings("unchecked")
         Iterator<Object> values = ((NormalAnnotation) annotation).values().iterator(); values.hasNext();)
         {
            final Object value = values.next();
            if (value instanceof MemberValuePair)
            {
               final String identifier = ((MemberValuePair) value).getName().getIdentifier();
               identifiers.add(identifier);
               if (Objects.equals(name, identifier))
               {
                  Expression expr = ((MemberValuePair) value).getValue();
                  if (element.getInternal().equals(expr))
                  {
                     // remove entire annotation element for inlined single-element array:
                     values.remove();
                     identifiers.remove(identifier);
                     continue;
                  }
                  if (expr instanceof ArrayInitializer)
                  {
                     final ArrayInitializer arrayInit = (ArrayInitializer) expr;

                     // remove element:
                     arrayInit.expressions().remove(element.getInternal());

                     if (arrayInit.expressions().isEmpty())
                     {
                        // remove empty array:
                        values.remove();
                        identifiers.remove(identifier);
                     }
                     else if (arrayInit.expressions().size() == 1)
                     {
                        // promote single-element array:
                        ((MemberValuePair) value).setValue((Expression) ASTNode.copySubtree(ast, (ASTNode) arrayInit
                                 .expressions().get(0)));
                     }
                  }
               }
            }
         }
         // finally, reduce to simplest equivalent annotation type:
         if (identifiers.isEmpty())
         {
            convertTo(AnnotationType.MARKER);
         }
         else if (identifiers.equals(Collections.singleton(DEFAULT_VALUE)))
         {
            convertTo(AnnotationType.SINGLE);
         }
      }
      return this;
   }

   @Override
   public AnnotationSource<O> getAnnotationValue()
   {
      if (isSingleValue())
      {
         SingleMemberAnnotation single = (SingleMemberAnnotation) annotation;
         Expression value = single.getValue();
         if (value instanceof Annotation)
         {
            return new Nested(this, value);
         }
         if (value instanceof ArrayInitializer && ((ArrayInitializer) value).expressions().size() == 1)
         {
            value = (Expression) ((ArrayInitializer) value).expressions().get(0);
            if (value instanceof Annotation)
            {
               return new Nested(this, value);
            }
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
            if (Objects.equals(name, memberValuePair.getName().getIdentifier()))
            {
               Expression value = memberValuePair.getValue();
               if (value instanceof Annotation)
               {
                  return new Nested(this, value);
               }
               if (value instanceof ArrayInitializer && ((ArrayInitializer) value).expressions().size() == 1)
               {
                  value = (Expression) ((ArrayInitializer) value).expressions().get(0);
                  if (value instanceof Annotation)
                  {
                     return new Nested(this, value);
                  }
               }
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
   @SuppressWarnings("unchecked")
   public AnnotationSource<O>[] getAnnotationArrayValue(String name)
   {
      final Expression expr = getElementValueExpression(name);
      if (expr instanceof ArrayInitializer)
      {
         final List<AnnotationSource<O>> results = new ArrayList<>();
         final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
         for (Expression arrayElement : arrayElements)
         {
            results.add(new Nested(this, arrayElement));
         }
         return results.toArray(new AnnotationSource[results.size()]);
      }
      final AnnotationSource<O> annotationValue = getAnnotationValue(name);
      if (annotationValue != null)
      {
         return new AnnotationSource[] { annotationValue };
      }
      return null;
   }

   @Override
   public <E extends Enum<E>> E[] getEnumArrayValue(Class<E> type)
   {
      return getEnumArrayValue(type, DEFAULT_VALUE);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <E extends Enum<E>> E[] getEnumArrayValue(Class<E> type, String name)
   {
      final Expression expr = getElementValueExpression(name);
      if (expr instanceof ArrayInitializer)
      {
         final List<E> results = new ArrayList<>();
         final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
         for (Expression arrayElement : arrayElements)
         {
            results.add(convertLiteralToEnum(type, arrayElement.toString()));
         }
         return results.toArray((E[]) Array.newInstance(type, results.size()));
      }
      else if (expr != null)
      {
         final E instance = convertLiteralToEnum(type, expr.toString());
         if (type.isInstance(instance))
         {
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
         final List<Class<?>> result = new ArrayList<>();
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
      final List<String> result = new ArrayList<>();
      String literalValue = getLiteralValue(name);

      if (literalValue.startsWith("{") && literalValue.endsWith("}"))
      {
         literalValue = literalValue.substring(1, literalValue.length() - 1);
      }
      if (!StringUtils.isEmpty(literalValue))
      {
         for (String value : literalValue.split(","))
         {
            result.add(Strings.unquote(value));
         }
      }
      return result.toArray(new String[result.size()]);
   }

   @Override
   public AnnotationSource<O> setClassValue(String name, Class<?> value)
   {
      getOrigin().addImport(value);
      return setLiteralValue(name, Types.toResolvedType(value.getCanonicalName(), getOrigin()) + ".class");
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
      final List<String> literals = new ArrayList<>();

      for (String value : requireNonNull(values))
      {
         literals.add(Strings.enquote(requireNonNull(value)));
      }
      return setArrayLiteralValue(name, literals);
   }

   @Override
   public AnnotationSource<O> setClassArrayValue(String name, Class<?>... values)
   {
      final List<String> literals = new ArrayList<>();

      for (Class<?> value : requireNonNull(values))
      {
         getOrigin().addImport(value);
         literals.add(Types.toResolvedType(value.getCanonicalName(), getOrigin()) + ".class");
      }
      return setArrayLiteralValue(name, literals);
   }

   private AnnotationSource<O> setArrayLiteralValue(String name, final List<String> literals)
   {
      String value = literals.size() == 1 ? literals.get(0) : String.format("{%s}", String.join(",", literals));
      return setLiteralValue(name, value);
   }

   @SuppressWarnings("unchecked")
   private <E extends Expression> E getElementValueExpression(String name)
   {
      if (isSingleValue() && DEFAULT_VALUE.equals(name))
      {
         return (E) ((SingleMemberAnnotation) annotation).getValue();
      }
      if (isNormal())
      {
         for (Object annotationValue : ((NormalAnnotation) annotation).values())
         {
            if (annotationValue instanceof MemberValuePair)
            {
               MemberValuePair pair = (MemberValuePair) annotationValue;
               if (pair.getName().getFullyQualifiedName().equals(name))
               {
                  return (E) pair.getValue();
               }
            }
         }
      }
      return null;
   }

   private String resolveTypeLiteralName(TypeLiteral typeLiteral)
   {
      final Type<O> type = new TypeImpl<>(getOrigin(), typeLiteral.getType());
      if (Types.isPrimitive(type.getName()))
      {
         return type.getName();
      }
      return type.getQualifiedName();
   }

   private Class<?> resolveTypeLiteral(TypeLiteral typeLiteral)
   {
      final Type<O> type = new TypeImpl<>(getOrigin(), typeLiteral.getType());
      if (Types.isPrimitive(type.getName()))
      {
         return Types.toPrimitive(type.getName());
      }

      try
      {
         return Class.forName(type.getQualifiedName());
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }

   @Override
   public boolean isTypeElementDefined(String name)
   {
      return getValues().stream().filter(pair -> pair.getName().equals(name)).findAny().isPresent();
   }
}