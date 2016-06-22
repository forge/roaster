/*
 * Copyright 2012-2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;

/**
 * Types utilities
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Types
{
   private static final String BOOLEAN = "boolean";

   // [B=byte,
   // [F=float,
   // [C=char,
   // [D=double,
   // [I=int,
   // [J=long,
   // [S=short,
   // [Z=boolean
   private static final Pattern CLASS_ARRAY_PATTERN = Pattern.compile("\\[+(B|F|C|D|I|J|S|Z|L)([0-9a-zA-Z\\.\\$]*);?");

   private static final Pattern SIMPLE_ARRAY_PATTERN = Pattern
            .compile("^((.)+)(\\[\\])+$");
   private static final Pattern IDENTIFIER_PATTERN = Pattern
            .compile("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
   // private static final Pattern GENERIC_PATTERN = Pattern.compile(".*<.*>(\\[\\])*$");
   private static final Pattern WILDCARD_AWARE_TYPE_PATTERN = Pattern
            .compile("\\?|^\\s*(\\?\\s+(?:extends|super)\\s+)?([A-Za-z$_]\\S*)\\s*$");

   private static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("<|>|,");

   private static final List<String> LANG_TYPES = Arrays.asList(
            // Interfaces
            "Appendable",
            "AutoCloseable",
            "CharSequence",
            "Cloneable",
            "Comparable",
            "Iterable",
            "Readable",
            "Runnable",
            // Classes
            "Boolean",
            "Byte",
            "Character",
            "Character.Subset",
            "Character.UnicodeBlock",
            "Class",
            "ClassLoader",
            "ClassValue",
            "Compiler",
            "Double",
            "Enum",
            "Float",
            "InheritableThreadLocal",
            "Integer",
            "Long",
            "Math",
            "Number",
            "Object",
            "Package",
            "Process",
            "ProcessBuilder",
            "ProcessBuilder.Redirect",
            "Runtime",
            "RuntimePermission",
            "SecurityManager",
            "Short",
            "StackTraceElement",
            "StrictMath",
            "String",
            "StringBuffer",
            "StringBuilder",
            "System",
            "Thread",
            "ThreadGroup",
            "ThreadLocal",
            "Throwable",
            "Void",
            // Exception Types
            "AbstractMethodError",
            "AssertionError",
            "BootstrapMethodError",
            "ClassCircularityError",
            "ClassFormatError",
            "Error",
            "Exception",
            "ExceptionInInitializerError",
            "IllegalAccessError",
            "IncompatibleClassChangeError",
            "InstantiationError",
            "InternalError",
            "LinkageError",
            "NoClassDefFoundError",
            "NoSuchFieldError",
            "NoSuchMethodError",
            "OutOfMemoryError",
            "RuntimeException",
            "StackOverflowError",
            "ThreadDeath",
            "UnknownError",
            "UnsatisfiedLinkError",
            "UnsupportedClassVersionError",
            "VerifyError",
            "VirtualMachineError",
            // Errors
            "AbstractMethodError",
            "AssertionError",
            "BootstrapMethodError",
            "ClassCircularityError",
            "ClassFormatError",
            "Error",
            "ExceptionInInitializerError",
            "IllegalAccessError",
            "IncompatibleClassChangeError",
            "InstantiationError",
            "InternalError",
            "LinkageError",
            "NoClassDefFoundError",
            "NoSuchFieldError",
            "NoSuchMethodError",
            "OutOfMemoryError",
            "StackOverflowError",
            "ThreadDeath",
            "UnknownError",
            "UnsatisfiedLinkError",
            "UnsupportedClassVersionError",
            "VerifyError",
            "VirtualMachineError",
            // Annotation Types
            "Deprecated",
            "Override",
            "SafeVarargs",
            "SuppressWarnings");

   private Types() {}

   public static boolean areEquivalent(String left, String right)
   {
      if ((left == null) && (right == null))
         return true;
      if ((left == null) || (right == null))
         return false;
      if (left.equals(right))
         return true;

      left = stripGenerics(left);
      right = stripGenerics(right);

      String l = toSimpleName(left);
      String r = toSimpleName(right);

      String lp = getPackage(left);
      String rp = getPackage(right);

      if (l.equals(r))
      {
         if (!lp.isEmpty() && !rp.isEmpty())
         {
            return false;
         }
         return true;
      }

      return false;
   }

   public static String toSimpleName(final String type)
   {
      if (type == null)
      {
         return null;
      }
      String result = type;

      if (isGeneric(stripArray(type)))
      {
         result = stripGenerics(result);
      }
      String[] tokens = tokenizeClassName(result);
      if (tokens != null)
      {
         result = tokens[tokens.length - 1];
      }
      if (isGeneric(type))
      {
         final List<String> simpleParameters = new ArrayList<String>();
         for (String typeParameter : getGenericsTypeParameter(type).split(","))
         {
            String simpleType;
            typeParameter = typeParameter.trim();
            if ("?".equals(typeParameter))
            {
               simpleType = typeParameter;
            }
            else
            {
               final Matcher matcher = WILDCARD_AWARE_TYPE_PATTERN.matcher(typeParameter);
               if (!matcher.matches())
               {
                  throw new IllegalArgumentException("Cannot parse type parameter " + typeParameter);
               }
               simpleType = toSimpleName(matcher.group(2));
               if (matcher.start(1) >= 0)
               {
                  simpleType = new StringBuilder(matcher.group(1)).append(' ').append(simpleType).toString()
                           .replaceAll("\\s{2,}?", " ");
               }
            }
            simpleParameters.add(simpleType);
         }
         result += new StringBuilder("<>").insert(1, Strings.join(simpleParameters, ",")).toString();
      }
      return result;
   }

   public static String[] tokenizeClassName(final String className)
   {
      String[] result = null;
      if (className != null)
      {
         result = className.split("\\.");
      }
      return result;
   }

   public static boolean isQualified(final String className)
   {
      String[] tokens = tokenizeClassName(className);
      return (tokens != null) && (tokens.length > 1);
   }

   public static String getPackage(final String className)
   {
      if (className.indexOf(".") > -1)
      {
         return className.substring(0, className.lastIndexOf("."));
      }
      return "";
   }

   public static boolean isSimpleName(final String name)
   {
      return (name != null) && name.matches("(?i)(?![0-9])[a-z0-9$_]+");
   }

   public static boolean isJavaLang(final String type)
   {
      final String javaLang = "java.lang.";

      String check;
      if (type.startsWith(javaLang))
      {
         check = type.substring(javaLang.length());
      }
      else
      {
         check = type;
      }
      return LANG_TYPES.contains(check);
   }

   public static boolean isBasicType(String idType)
   {
      return isPrimitive(idType)
               || Arrays.asList("Boolean", "Byte", "Double", "Float", "Integer", "Long", "Short", "String").contains(
                        idType);
   }

   public static boolean isGeneric(String type)
   {
      if (type == null)
      {
         return false;
      }
      int genericStart = type.indexOf('<');
      if (genericStart < 0)
      {
         return false;
      }
      type = stripArray(type);
      if (!validateName(type.substring(0, genericStart)))
      {
         return false;
      }
      String typeArgs = type.substring(genericStart + 1, type.lastIndexOf('>'));
      StringTokenizer tok = new StringTokenizer(typeArgs, ", ");
      while (tok.hasMoreTokens())
      {
         String typeArg = tok.nextToken();
         while (incompleteGenerics(typeArg) && tok.hasMoreElements())
         {
            typeArg += tok.nextToken();
         }

         if (!validateNameWithGenerics(typeArg))
         {
            return false;
         }
      }
      return true;
   }

   private static boolean validateNameWithGenerics(String name)
   {
      return isGeneric(name) || validateName(name) || WILDCARD_AWARE_TYPE_PATTERN.matcher(name).matches();
   }

   private static boolean incompleteGenerics(String name)
   {
      final int ltCount = name.length() - name.replaceAll("<", "").length();
      final int gtCount = name.length() - name.replaceAll(">", "").length();
      return ltCount != gtCount;
   }

   private static boolean validateName(String name)
   {
      return IDENTIFIER_PATTERN.matcher(name).matches();
   }

   public static String stripGenerics(String type)
   {
      if (isClassArray(type))
      {
         type = fixClassArray(type);
      }
      if (isGeneric(type))
      {
         return type.substring(0, type.indexOf('<')) + type.substring(type.lastIndexOf('>') + 1);
      }
      else
      {
         return type;
      }
   }

   public static String fixArray(final String type, boolean stripGenerics)
   {
      final String componentType;
      final int arrayDimensions;
      if (isArray(type))
      {
         arrayDimensions = getArrayDimension(type);
         componentType = stripArray(type);
      }
      else
      {
         arrayDimensions = 0;
         componentType = type;
      }
      final StringBuilder result = new StringBuilder();
      if (isGeneric(componentType) && stripGenerics)
      {
         result.append(componentType.replaceFirst("^([^<]*)<.*?>$", "$1"));
      }
      else
      {
         result.append(componentType);
      }
      for (int i = 0; i < arrayDimensions; i++)
      {
         result.append("[]");
      }
      return result.toString();
   }

   public static String getGenerics(final String type)
   {
      if (isGeneric(type))
      {
         return new StringBuilder("<>").insert(1, getGenericsTypeParameter(type)).toString();
      }
      return "";
   }

   public static String getGenericsTypeParameter(final String type)
   {
      if (isGeneric(type))
      {
         return stripArray(type).replaceFirst("^[^<]*<(.*?)>$", "$1");
      }
      return "";
   }

   // [Bjava.lang.Boolean;
   // [Ljava.util.Vector;
   public static boolean isArray(final String type)
   {
      if (type == null)
      {
         return false;
      }
      if (CLASS_ARRAY_PATTERN.matcher(type).matches())
      {
         return true;
      }
      Matcher matcher = SIMPLE_ARRAY_PATTERN.matcher(type);
      if (matcher.find())
      {
         String candidateType = matcher.group(1);
         return validateNameWithGenerics(candidateType);
      }
      else
      {
         return false;
      }
   }

   public static String stripArray(final String type)
   {
      String result = type;
      if (isClassArray(type))
      {
         result = fixClassArray(type);
      }

      if (isArray(result))
      {
         Matcher matcher;
         matcher = SIMPLE_ARRAY_PATTERN.matcher(result);
         if (matcher.find())
         {
            int idx = result.length() - 2;
            while (idx > 1 && result.charAt(idx - 2) == '[')
            {
               idx -= 2;
            }
            result = result.substring(0, idx);
         }
         else
         {
            return result;
         }
      }
      return result;
   }

   private static boolean isClassArray(String type)
   {
      Matcher matcher = CLASS_ARRAY_PATTERN.matcher(type);
      return matcher.find();
   }

   private static String fixClassArray(String type)
   {
      Matcher matcher = CLASS_ARRAY_PATTERN.matcher(type);
      String result = type;
      if (matcher.find())
      {
         int dim = getBasicArrayDimension(type);
         switch (matcher.group(1).charAt(0))
         {
         case 'B':
            result = "byte";
            break;
         case 'F':
            result = "float";
            break;
         case 'C':
            result = "char";
            break;
         case 'D':
            result = "double";
            break;
         case 'I':
            result = "int";
            break;
         case 'J':
            result = "long";
            break;
         case 'S':
            result = "short";
            break;
         case 'Z':
            result = BOOLEAN;
            break;
         case 'L':
            result = matcher.group(2);
            break;
         default:
            throw new IllegalArgumentException("Invalid array format " + type);
         }
         for (int j = 0; j < dim; j++)
         {
            result += "[]";
         }
      }
      return result;
   }

   public static boolean isPrimitive(final String result)
   {
      return Arrays.asList("byte", "short", "int", "long", "float", "double", BOOLEAN, "char").contains(result);
   }

   /**
    * Returns the dimension of the array.
    *
    * It simply counts the "[" from the string.
    *
    * @param name an array type, e.g.: byte[] or [Ljava.lang.Boolean;
    * @return the array dimension. 0 if the type is not a valid array
    */
   public static int getArrayDimension(String name)
   {
      int count = 0;
      if (name != null)
      {
         if (isGeneric(name))
         {
            name = stripGenerics(name);
         }
         for (char c : name.toCharArray())
         {
            if (c == '[')
            {
               count++;
            }
         }
      }
      return count;
   }

   public static int getBasicArrayDimension(String name)
   {
      int count = 0;
      if (name != null)
      {
         for (char c : name.toCharArray())
         {
            if (c == '[')
            {
               count++;
            }
         }
      }
      return count;
   }

   public static <O extends JavaType<O>> String rebuildGenericNameWithArrays(String resolvedTypeName, Type<O> type)
   {
      StringBuilder resolvedType = new StringBuilder(stripArray(resolvedTypeName));
      resolvedType.append(getGenerics(type.toString()));
      for (int j = 0; j < getArrayDimension(type.getName()); j++)
      {
         resolvedType.append("[]");
      }
      return resolvedType.toString();
   }

   /**
    * Returns the default value for a given type
    * 
    * @param type
    * @return
    */
   public static String getDefaultValue(Class<?> type)
   {
      final String result;
      if (type.isPrimitive())
      {
         if (type == boolean.class)
         {
            result = "false";
         }
         else
         {
            result = "0";
         }
      }
      else
      {
         result = "null";
      }
      return result;
   }

   /**
    * Returns the default value for a given type
    * 
    * @param type
    * @return
    */
   public static String getDefaultValue(String type)
   {
      final String result;
      if (Types.isPrimitive(type))
      {
         if (BOOLEAN.equals(type) || Boolean.TYPE.getName().equals(type))
         {
            result = "false";
         }
         else
         {
            result = "0";
         }
      }
      else
      {
         result = "null";
      }
      return result;
   }

   /**
    * 
    * Returns the available generics as a String array
    * 
    * @param typeName
    * @return
    */
   public static String[] splitGenerics(String typeName)
   {
      typeName = typeName.replaceAll(" ","");
      int begin = typeName.indexOf('<');
      int end = typeName.indexOf('>');
      if (begin == -1 || end == -1)
      {
         return new String[0];
      }
      return GENERIC_TYPE_PATTERN.split(typeName.substring(begin + 1, end));
   }
}