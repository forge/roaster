package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaInterface;

/**
 * Represents a Java {@code interface} source file as an in-memory modifiable element. See {@link JavaParser} for
 * various options in generating {@link JavaInterfaceSource} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaInterfaceSource extends JavaInterface<JavaInterfaceSource>,
         JavaSource<JavaInterfaceSource>,
         FieldHolderSource<JavaInterfaceSource>,
         MethodHolderSource<JavaInterfaceSource>,
         GenericCapableSource<JavaInterfaceSource>,
         InterfaceCapableSource<JavaInterfaceSource>
{
}