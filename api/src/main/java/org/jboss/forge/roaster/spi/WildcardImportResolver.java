/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.spi;

import org.jboss.forge.roaster.model.JavaType;

/**
 * Responsible for providing additional import resolution functionality for situations where classes have referenced a
 * package or wild-card import.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface WildcardImportResolver
{
   public String resolve(JavaType<?> source, String type);
}
