/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaInterfaceSource;
import org.jboss.forge.test.parser.java.common.InterfacedTestBase;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaInterfaceInterfaceTest extends InterfacedTestBase<JavaInterfaceSource>
{
   @Override
   protected JavaInterfaceSource getSource()
   {
      return JavaParser.parse(JavaInterfaceSource.class, "public interface MockInterface {}");
   }
}
