/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;

/**
 * Utility refactory methods for {@link JavaClass} objects
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:vreynolds@redhat.com">Vineet Reynolds</a>
 */
public class Refactory
{
   /**
    * Generates a getXXX and setXXX method for the supplied field
    * 
    * @param clazz
    * @param field
    */
   public static void createGetterAndSetter(final JavaClass clazz, final Field<JavaClass> field)
   {
      if (!clazz.hasField(field))
      {
         throw new IllegalArgumentException("Entity did not contain the given field [" + field + "]");
      }

      clazz.getMethods();

      String fieldName = field.getName();
      String methodNameSuffix = Strings.capitalize(fieldName);
      clazz.addMethod().setReturnType(field.getTypeInspector().toString()).setName("get" + methodNameSuffix)
               .setPublic()
               .setBody("return this." + fieldName + ";");

      if (!field.isFinal())
      {
         clazz.addMethod().setReturnTypeVoid().setName("set" + methodNameSuffix).setPublic()
                  .setParameters("final " + field.getTypeInspector().toString() + " " + fieldName)
                  .setBody("this." + fieldName + " = " + fieldName + ";");
      }
   }

   /**
    * Create a <i>hashCode</i> and <i>equals</i> implementation for the given class and fields
    * 
    * @deprecated Use {@link Refactory#createHashCodeAndEquals(JavaClass, Field...)} instead, since this method relies
    *             on the existence of the id field
    */
   @Deprecated
   public static void createHashCodeAndEquals(final JavaClass clazz)
   {
      final Field<?>[] fields;
      Field<JavaClass> idField = clazz.getField("id");
      // FORGE-995: Retained this for backwards compatibility
      if (idField != null)
      {
         fields = new Field[] { idField };
      }
      else
      {
         List<Field<JavaClass>> classFields = clazz.getFields();
         fields = classFields.toArray(new Field[classFields.size()]);
      }
      createHashCodeAndEquals(clazz, fields);
   }

   /**
    * Create a <i>hashCode</i> and <i>equals</i> implementation for the given class and fields
    * 
    * @param clazz class to be changed
    * @param fields fields to be used in the equals/hashCode methods
    */
   public static void createHashCodeAndEquals(final JavaClass clazz, final Field<?>... fields)
   {
      if (fields == null || fields.length < 1)
      {
         throw new IllegalArgumentException("fields cannot be null or empty.");
      }

      StringBuilder fieldEqualityChecks = new StringBuilder();
      StringBuilder hashCodeComputation = new StringBuilder();
      for (Field<?> field : fields)
      {
         String fieldName = field.getName();

         if (field.isPrimitive())
         {
            fieldEqualityChecks.append("if (").append(fieldName).append(" != that.").append(fieldName).append(") { ");
            fieldEqualityChecks.append(" return false;");
            fieldEqualityChecks.append("} ");

            hashCodeComputation.append("result = prime * result + ").append(fieldName).append(";");
         }
         else
         {
            fieldEqualityChecks.append("if (").append(fieldName).append(" != null) { ");
            fieldEqualityChecks.append(" return ").append(fieldName).append(".equals(((");
            fieldEqualityChecks.append(clazz.getName());
            fieldEqualityChecks.append(") that).").append(fieldName);
            fieldEqualityChecks.append("); } ");

            hashCodeComputation.append("result = prime * result + ((").append(fieldName).append(" == null) ? 0 : ")
                     .append(fieldName).append(".hashCode());");
         }
      }

      clazz.addMethod(
               "public boolean equals(Object that) { " +
                        "if (this == that) { return true; } " +
                        "if (that == null) { return false; } " +
                        "if (getClass() != that.getClass()) { return false; } " +
                        fieldEqualityChecks.toString() +
                        "return true; " +
                        "}")
               .addAnnotation(Override.class);

      clazz.addMethod(
               "public int hashCode() { " +
                        "final int prime = 31;" +
                        "int result = 1;" +
                        hashCodeComputation.toString() +
                        "return result; }")
               .addAnnotation(Override.class);
   }

   /**
    * Create a <i>toString</i> implementation using all the fields in this class
    * 
    * @param clazz
    */
   public static void createToStringFromFields(final JavaClass clazz)
   {
      List<Field<JavaClass>> fields = clazz.getFields();
      createToStringFromFields(clazz, fields);
   }

   /**
    * Create a <i>toString</i> implementation using the supplied fields
    * 
    * @param clazz
    * @param fields
    */
   public static void createToStringFromFields(final JavaClass clazz, final Field<JavaClass>... fields)
   {
      createToStringFromFields(clazz, Arrays.asList(fields));
   }

   /**
    * Create a <i>toString</i> implementation using the supplied fields
    * 
    * @param clazz
    * @param fields
    */
   public static void createToStringFromFields(final JavaClass clazz, final List<Field<JavaClass>> fields)
   {
      Method<JavaClass> method = clazz.addMethod().setName("toString").setReturnType(String.class)
               .setPublic();
      method.addAnnotation(Override.class);

      List<String> list = new ArrayList<String>();

      String delimeter = "\n";
      for (Field<JavaClass> field : fields)
      {
         if (clazz.hasField(field))
         {
            StringBuilder line = new StringBuilder();

            if (!field.isPrimitive())
               if (field.isType(String.class))
               {
                  line.append("if(").append(field.getName()).append(" != null && !").append(field.getName())
                           .append(".trim().isEmpty())\n");
               }
               else
               {
                  line.append("if(").append(field.getName()).append(" != null)\n");
               }

            boolean isFirst = list.isEmpty();

            line.append(" result += ").append(isFirst ? "\"" : "\", ");
            line.append(field.getName()).append(": \" + ").append(field.getName()).append(";");

            list.add(line.toString());
         }
      }

      String body = "String result = getClass().getSimpleName()+\" \";\n" +
               Strings.join(list, delimeter) + "\n" +
               "return result;";
      method.setBody(body);
   }
}
