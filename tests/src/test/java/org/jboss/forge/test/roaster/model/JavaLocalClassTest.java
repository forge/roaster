package org.jboss.forge.test.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JavaLocalClassTest
{
   @Test
   public void testLocalClassMatch()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class,
               getClass().getResourceAsStream("/org/jboss/forge/grammar/java/MockLocalClass.java"));
      assertFalse(source.isLocalClass());
      List<JavaSource<?>> nestedTypes = source.getNestedTypes();
      assertEquals(17, nestedTypes.size());
      assertThat(nestedTypes.get(0)).isInstanceOf(JavaInterface.class);
      assertThat(nestedTypes.subList(1, 17))
               .allMatch(c -> c instanceof JavaClass)
               .allMatch(c -> ((JavaClass<?>) c).isLocalClass());
   }
}
