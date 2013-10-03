package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.AnnotationTarget;

/**
 * Represents a Java source element that may carry annotations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationTargetSource<O extends JavaSource<O>, T> extends AnnotationTarget<O>
{
   @Override
   public List<AnnotationSource<O>> getAnnotations();

   @Override
   public AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   @Override
   public AnnotationSource<O> getAnnotation(final String type);

   /**
    * Add a new annotation instance to this {@link T}. (Note that an import statement must be added manually if
    * required.)
    */
   public AnnotationSource<O> addAnnotation();

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link Class} as the annotation type. Attempt
    * to add an import statement to this object's {@link O} if required.
    */
   public AnnotationSource<O> addAnnotation(Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link String} className as the annotation
    * type. Attempt to add an import statement to this object's {@link O} if required. (Note that the given className
    * must be fully-qualified in order to properly import required classes)
    */
   public AnnotationSource<O> addAnnotation(final String className);

   public T removeAnnotation(Annotation<O> annotation);
}