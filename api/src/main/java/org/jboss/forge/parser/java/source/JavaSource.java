package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.JavaType;

/**
 * Represents a Java type in source form.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaSource<T extends JavaSource<T>> extends JavaType<T>,
         PackagedSource<T>,
         Importer<T>,
         NamedSource<T>,
         VisibilityScopedSource<T>,
         AnnotationTargetSource<T, T>
{
   @Override
   public JavaSource<?> getEnclosingType();

   /**
    * Return a list containing {@link JavaSource} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaSource} instances will result in modification of the
    * contents contained by <code>this</code> the parent instance.
    */
   public List<JavaSource<?>> getNestedClasses();

}