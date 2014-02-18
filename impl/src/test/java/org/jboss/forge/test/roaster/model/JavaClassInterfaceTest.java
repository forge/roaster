/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.test.roaster.model.common.InterfacedTestBase;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassInterfaceTest extends InterfacedTestBase<JavaClassSource>
{
   @Override
   protected JavaClassSource getSource()
   {
      return Roaster.parse(JavaClassSource.class, "public class MockClass {}");
   }
}
