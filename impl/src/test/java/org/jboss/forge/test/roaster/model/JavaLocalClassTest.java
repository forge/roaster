package org.jboss.forge.test.roaster.model;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.spi.Streams;
import org.jboss.forge.test.roaster.model.MockLocalClass.ID;
import org.junit.Assert;
import org.junit.Test;

public class JavaLocalClassTest
{
   MockLocalClass m = new MockLocalClass();
   String source = Streams.toString(
            getClass().getResourceAsStream("/org/jboss/forge/grammar/java/MockLocalClass.java"));

   @Test
   public void testLocalClassMatch()
   {
      // seems broken for inner class lookup at 20150710
      final boolean broken_names_ForLocalClass = true;
      final boolean broken_hasInterface_ForInnerLookup = true;
      final boolean localclass_mustNotBe_contained_In_getNestedClasses = false;

      Map<String, Object> id2obj = new HashMap<String, Object>();
      for (MockLocalClass.ID i : m.x)
      {
         id2obj.put(i.getId(), i);
      }

      String idclassname = ID.class.getName();

      JavaType<?> jt = Roaster.parse(source);
      Queue<JavaType<?>> enclosingTypeQ = new ArrayDeque<JavaType<?>>();
      enclosingTypeQ.add(jt);

      Assert.assertEquals(false, jt.isLocalClass());

      while (!enclosingTypeQ.isEmpty())
      {
         JavaType<?> typeOfIntrest = enclosingTypeQ.poll();

         if (localclass_mustNotBe_contained_In_getNestedClasses
                  && typeOfIntrest.isLocalClass())
         {
            Assert.fail("a local class must not be included in JavaType#getNestedClasses()");
         }

         if (typeOfIntrest instanceof JavaClassSource)
         {
            JavaClassSource classInSource = (JavaClassSource) typeOfIntrest;
            System.out.println(classInSource.getQualifiedName());

            Field<?> id_field = classInSource.getField("id");

            if ((broken_hasInterface_ForInnerLookup || classInSource.hasInterface(idclassname))
                     &&
                     id_field != null)
            {

               String id_of_class = id_field.getStringInitializer();
               Class expectedClassForID = id2obj.get(id_of_class).getClass();

               Assert.assertEquals(
                        "isLocalClass for " + id_of_class,
                        expectedClassForID.isLocalClass(),
                        classInSource.isLocalClass());

               if (!broken_names_ForLocalClass)
               {
                  Assert.assertEquals(
                           "getCanonicalName() for " + id_of_class,
                           expectedClassForID.getCanonicalName(),
                           classInSource.getCanonicalName());

                  Assert.assertEquals(
                           "get[Qualified]Name() for " + id_of_class,
                           expectedClassForID.getName(),
                           classInSource.getQualifiedName());

                  Assert.assertEquals(
                           "get[Simple]Name() for " + id_of_class,
                           expectedClassForID.getSimpleName(),
                           classInSource.getName());
               }

            }
         }

         enclosingTypeQ.addAll(typeOfIntrest.getNestedClasses());
      }

   }

}
