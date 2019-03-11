/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unicode source test
 * 
 * @author <a href="https://github.com/kuzukami">kuzukami.sh</a>
 */
public class UnicodeSourceTest
{
   @Test
   public void testUnicodeSource()
   {
      JavaUnit source = Roaster
               .parseUnit("public class T日本語<Kカナ>{public<T extends Kカナ> void testにほんご() {}}");
      assertEquals("T日本語", source.getGoverningType().getName());
      assertEquals("testにほんご", ((JavaClassSource) source.getGoverningType()).getMethods().get(0).getName());
      assertEquals("Kカナ", ((JavaClassSource) source.getGoverningType()).getTypeVariables().get(0).getName());
   }
}
