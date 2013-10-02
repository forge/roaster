package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Parameter;

/**
 * Represents a parameter of a {@link MethodSource}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ParameterSource<O extends JavaSource<O>> extends Parameter<O>, AnnotationTargetSource<O, ParameterSource<O>>
{
}