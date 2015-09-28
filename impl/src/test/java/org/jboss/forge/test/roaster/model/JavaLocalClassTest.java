package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Assert;
import org.junit.Test;

public class JavaLocalClassTest
{
   @Test
   public void testLocalClassMatch()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class,
               getClass().getResourceAsStream("/org/jboss/forge/grammar/java/MockLocalClass.java"));
      Assert.assertFalse(source.isLocalClass());
      List<JavaSource<?>> nestedTypes = source.getNestedTypes();
      Assert.assertThat(nestedTypes.size(), equalTo(17));
      Assert.assertThat(nestedTypes.get(0), instanceOf(JavaInterface.class));
      Assert.assertThat(nestedTypes.subList(1, 17),
               everyItem(allOf(CoreMatchers.<JavaSource<?>> instanceOf(JavaClass.class), new IsLocalMatcher())));
   }

   private class IsLocalMatcher extends BaseMatcher<JavaSource<?>>
   {
      @Override
      public boolean matches(Object item)
      {
         if (item instanceof JavaClass)
         {
            return ((JavaClass<?>) item).isLocalClass();
         }
         return false;
      }

      @Override
      public void describeTo(Description description)
      {
         description.appendText("a local class");
      }

      @Override
      public void describeMismatch(Object item, Description description)
      {
         description.appendValue(item).appendText("is not a local class");
      }
   }
}
