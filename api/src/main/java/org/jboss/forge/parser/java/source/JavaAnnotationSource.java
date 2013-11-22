package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.AnnotationElement;
import org.jboss.forge.parser.java.JavaAnnotation;

/**
 * Represents a Java {@code @interface} annotation source file as an in-memory modifiable element. See
 * {@link JavaParser} for various options in generating {@link JavaAnnotationSource} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaAnnotationSource extends JavaAnnotation<JavaAnnotationSource>, JavaSource<JavaAnnotationSource>
{
   /**
    * Get the {@link AnnotationElementSource} with the given name and return it, otherwise, return null.
    */
   @Override
   public AnnotationElementSource getAnnotationElement(String name);

   /**
    * Get a list of all {@link AnnotationElementSource}s declared by this {@link JavaAnnotation}, or return an empty
    * list if no {@link AnnotationElementSource}s are declared.
    */
   @Override
   public List<AnnotationElementSource> getAnnotationElements();

   /**
    * Add a new Java {@link AnnotationElementSource} to this {@link JavaAnnotationSource} instance. This will be a stub until
    * further modified.
    */
   public AnnotationElementSource addAnnotationElement();

   /**
    * Add a new {@link AnnotationElementSource} declaration to this {@link JavaAnnotationSource} instance, using the given
    * {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>AnnotationElement e = javaClass.addAnnotationElement("String newAnnotationElement();");</code>
    */
   public AnnotationElementSource addAnnotationElement(final String declaration);

   /**
    * Remove the given {@link AnnotationElement} from this {@link JavaAnnotationSource} instance, if it exists;
    * otherwise, do nothing.
    */
   public JavaAnnotationSource removeAnnotationElement(final AnnotationElement<?> annotationElement);
}