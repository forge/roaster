/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.io.Serializable;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.SerialVersionUIDComputer;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class SerialVersionUIDComputerTest
{

   @Test
   public void testSerialVersionUID1()
   {
      JavaClassSource type = Roaster.create(JavaClassSource.class).setName("Foo").addInterface(Serializable.class);
      type.addMethod().setName("foo");
      type.addField().setType(String.class).setName("bar");
      long result = 8297639620252560729L;
      Assert.assertEquals(result, SerialVersionUIDComputer.compute(type));
      Assert.assertEquals(result, SerialVersionUIDComputer.compute(type));
   }

   @Test
   public void testSerialVersionUID2()
   {
      JavaClassSource type = Roaster.create(JavaClassSource.class).setName("Bar").addInterface(Serializable.class);
      type.addMethod().setName("foo");
      type.addField().setType(String.class).setName("bar");
      long result = 6931690417956417438L;
      Assert.assertEquals(result, SerialVersionUIDComputer.compute(type));
      Assert.assertEquals(result, SerialVersionUIDComputer.compute(type));
   }
}