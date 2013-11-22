/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.Import;
import org.jboss.forge.parser.java.source.JavaInterfaceSource;
import org.jboss.forge.parser.java.source.MemberSource;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaInterfaceTest
{
   @Test
   public void testCanParseInterface() throws Exception
   {
      InputStream stream = JavaInterfaceTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockInterface.java");
      JavaInterfaceSource javaClass = JavaParser.parse(JavaInterfaceSource.class, stream);
      String name = javaClass.getName();
      assertEquals("MockInterface", name);
   }

   @Test
   public void testCanParseBigInterface() throws Exception
   {
      InputStream stream = JavaInterfaceTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/BigInterface.java");
      JavaInterfaceSource javaClass = JavaParser.parse(JavaInterfaceSource.class, stream);
      String name = javaClass.getName();
      assertEquals("BigInterface", name);
      List<MemberSource<JavaInterfaceSource, ?>> members = javaClass.getMembers();
      assertFalse(members.isEmpty());
   }

   @Test
   public void testImportJavaSource() throws Exception
   {
      JavaInterfaceSource foo = JavaParser.parse(JavaInterfaceSource.class, "package org.jboss.forge; public interface Foo{}");
      JavaInterfaceSource bar = JavaParser.parse(JavaInterfaceSource.class, "package org.jboss.forge; public interface Bar{}");

      assertFalse(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));

      Import importBar = foo.addImport(bar);
      assertTrue(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));

      assertEquals("org.jboss.forge.Bar", importBar.getQualifiedName());
      assertEquals(importBar, foo.getImport(bar));

      foo.removeImport(bar);
      assertFalse(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));
   }

   @Test
   public void testImportImport() throws Exception
   {
      JavaInterfaceSource foo = JavaParser.parse(JavaInterfaceSource.class, "public interface Foo{}");
      Import i = foo.addImport(getClass());

      foo.removeImport(getClass());
      Import i2 = foo.addImport(i);
      assertNotSame(i, i2);
      assertEquals(i.getQualifiedName(), i2.getQualifiedName());
   }
}
