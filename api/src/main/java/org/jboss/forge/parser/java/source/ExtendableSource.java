package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Extendable;
import org.jboss.forge.parser.java.JavaType;

/**
 * Represents a {@link JavaSource} that can extend other types (Java inheritance and interfaces).
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ExtendableSource<O extends JavaType<O>> extends Extendable<O>
{
   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(JavaType<?> type);

   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(Class<?> type);

   /**
    * Set this type's super class.
    * <p>
    * <strong>For example:</strong><br/>
    * In the case of " <code>public class Foo extends Bar {}</code>" - <code>Foo</code> is the base type, and
    * <code>Bar</code> is the super class.)
    * <p>
    * Attempt to add an import statement to this object's {@link O} if required. (Note that the given className must
    * be fully-qualified in order to properly import required classes)
    */
   public O setSuperType(String type);
}