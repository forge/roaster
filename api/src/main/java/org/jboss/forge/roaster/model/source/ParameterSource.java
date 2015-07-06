/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Parameter;

/**
 * Represents a parameter of a {@link MethodSource}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ParameterSource<O extends JavaSource<O>> extends Parameter<O>,
         AnnotationTargetSource<O, ParameterSource<O>>, FinalCapableSource<ParameterSource<O>>
{
}