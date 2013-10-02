/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadParameter<O extends ReadJavaSource<O>> extends ReadAnnotationTarget<O>
{
   String getName();

   String getType();

   Type<?> getTypeInspector();

   public interface Parameter<O extends JavaSource<O>> extends ReadParameter<O>, AnnotationTarget<O, Parameter<O>>
   {
   }
}
