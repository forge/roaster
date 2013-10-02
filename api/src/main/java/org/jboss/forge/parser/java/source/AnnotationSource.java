package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaType;

/**
 * Represents an annotation on some Java source element.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationSource<O extends JavaType<O>> extends Annotation<O>
{

   AnnotationSource<O> getAnnotationValue();

   AnnotationSource<O> getAnnotationValue(String name);

   AnnotationSource<O> removeValue(String name);

   AnnotationSource<O> removeAllValues();

   AnnotationSource<O> setName(String className);

   AnnotationSource<O> setEnumValue(String name, Enum<?> value);

   AnnotationSource<O> setEnumValue(Enum<?>... value);

   AnnotationSource<O> setEnumArrayValue(String name, Enum<?>... values);

   AnnotationSource<O> setEnumArrayValue(Enum<?>... values);

   AnnotationSource<O> setLiteralValue(String value);

   AnnotationSource<O> setLiteralValue(String name, String value);

   AnnotationSource<O> setStringValue(String value);

   AnnotationSource<O> setStringValue(String name, String value);

   AnnotationSource<O> setAnnotationValue();

   AnnotationSource<O> setAnnotationValue(String name);

   AnnotationSource<O> setClassValue(String name, Class<?> value);

   AnnotationSource<O> setClassValue(Class<?> value);

   AnnotationSource<O> setClassArrayValue(String name, Class<?>... values);

   AnnotationSource<O> setClassArrayValue(Class<?>... values);
}