package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Member;


/**
 * Represents a {@link JavaSource} member. (I.e.: a {@link FieldSource} or {@link MethodSource})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MemberSource<O extends JavaSource<O>, T> extends Member<O>, AnnotationTargetSource<O, T>,
         VisibilityScopedSource<T>, NamedSource<T>
{

   public T setFinal(boolean finl);

   public T setStatic(boolean statc);
}