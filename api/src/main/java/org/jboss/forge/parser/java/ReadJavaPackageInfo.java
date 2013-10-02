/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

/**
 * Represents a Java Package Info.
 */
public interface ReadJavaPackageInfo<O extends ReadJavaPackageInfo<O>> extends ReadJavaSource<O>
{
   public interface JavaPackageInfo extends ReadJavaPackageInfo<JavaPackageInfo>, JavaSource<JavaPackageInfo>
   {
   }
}