/*
 * Copyright 2012-2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;

/**
 * Util classes for java types.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Types
{

   private static final Pattern SIMPLE_NAME_PATTERN = Pattern.compile("(?i)(?![0-9])[a-z0-9$_]+");
   private static final List<String> PRIMITIVE_TYPES = Arrays.asList("Boolean", "Byte", "Double", "Float", "Integer",
            "Long", "Short", "String");
   // [B=byte, [F=float, [Z=boolean, [C=char, [D=double, [I=int, [J=long, [S=short,
   private static final Pattern CLASS_ARRAY_PATTERN = Pattern.compile("\\[+(B|F|C|D|I|J|S|Z|L)([0-9a-zA-Z\\.\\$]*);?");
   //pattern to split java identifiers
   private static final Pattern JAVA_SEPARATOR_PATTERN = Pattern.compile("\\.");
   private static final Pattern SIMPLE_ARRAY_PATTERN = Pattern.compile("^((.)+)(\\[\\])+$");
   private static final Pattern IDENTIFIER_PATTERN = Pattern
            .compile("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
   private static final Pattern WILDCARD_AWARE_TYPE_PATTERN = Pattern
            .compile("\\?|^\\s*(\\?\\s+(?:extends|super)\\s+)?([A-Za-z$_]\\S*)\\s*$");
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

   private Types()
   {
      throw new IllegalAccessError("Utility class");
   }

   /**
    * Checks if two type names are equal after removing the generic part. The method returns true one of the following
    * conditions is matched:
    * <ul>
    * <li>Both types are {@code null}</li>
    * <li>Both types are equal in their String representation, so that {@link String#equals(Object)} returns
    * {@code true}</li>
    * <li>Both types have the same simple name and one the following is matched:
    * <ul>
    * <li>Both types are a simple names</li>
    * <li>Only one type is a full qualified name</li>
    * <li>Both types are full qualified and their package part is equal, so that {@link String#equals(Object)} returns
    * {@code true}</li>
    * </ul>
    * </ul>
    * 
    * @param left the first type (maybe {@code null})
    * @param right the second type (maybe {@code null})
    * @return {@code true} if the above conditions are fulfilled, {@code false} otherwise
    * @throws NullPointerException if one of the arguments is{@code null}
    */
   public static boolean areEquivalent(String left, String right)
   {
      if (left.equals(requireNonNull(right)))
         return true;

      String leftName = stripGenerics(toSimpleName(left));
      String rightName = stripGenerics(toSimpleName(right));

      String leftPackage = getPackage(left);
      String rightPackage = getPackage(right);

      if (leftName.equals(rightName))
      {
         if (!leftPackage.isEmpty() && !rightPackage.isEmpty())
         {
            return leftPackage.equals(rightPackage);
         }
         return !(!leftPackage.isEmpty() && !rightPackage.isEmpty());
      }

      return false;
   }

   /**
    * Calculates the simple name of the given type including all generics.
    * 
    * @param type the type to convert
    * @return the simple name
    * @throws NullPointerException if the type is {@code null}
    * @throws IllegalArgumentException if the generic part couldn't be parsed
    */
   public static String toSimpleName(final String type)
   {
      String result = requireNonNull(type);

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
         final List<String> simpleParameters = new ArrayList<>();
         StringTokenizer tok = new StringTokenizer(getGenericsTypeParameter(type), ",");
         while (tok.hasMoreTokens())
         {
            String typeParameter = tok.nextToken();
            while (incompleteGenerics(typeParameter) && tok.hasMoreElements())
            {
               typeParameter += ',' + tok.nextToken();
            }

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
         final String generics = new StringBuilder("<>").insert(1, Strings.join(simpleParameters, ",")).toString();
         if (isArray(result))
         {
            result = new StringBuilder(result).insert(result.indexOf("["), generics).toString();
         }
         else
         {
            result += generics;
         }
      }
      return result;
   }

   /**
    * Splits the given class name into is parts which are separated by a '.'.
    * 
    * @param className the class name to tokenize
    * @return a string array with the parts
    * @throws NullPointerException if the class name is {@code null}
    */
   public static String[] tokenizeClassName(final String className)
   {
      return JAVA_SEPARATOR_PATTERN.split(requireNonNull(className));
   }

   /**
    * Checks if the given type name is qualified, that means that it contains at least one '.'.
    * 
    * @param typeName the type name to check
    * @return {@code true}, if the type is qualified, {@code false} otherwise
    * @throws NullPointerException if the type name is {@code null}
    */
   public static boolean isQualified(final String typeName)
   {
      return typeName.contains(".");
   }

   /**
    * Extracts the package part of a given type name. The package is the part which is the substring before the last
    * occurrence of '.'.
    * 
    * @param typeName the type name to extract the package from
    * @return the package part or an empty string, if no package coudn't be extracted
    * @throws NullPointerException if the type name is {@code null}
    */
   public static String getPackage(final String typeName)
   {
      if (typeName.contains("."))
      {
         return typeName.substring(0, typeName.lastIndexOf("."));
      }
      return "";
   }

   /**
    * Checks if the given name is a simple name, so not full qualified. In the case the name is no valid java
    * identifier, {@code false} is returned.
    * 
    * @param name the name to check
    * @return {@code true}, if the name is simple, {@code false} otherwise or if the name is no valid java identifier
    * @throws NullPointerException if the name is {@code null}
    */
   public static boolean isSimpleName(final String name)
   {
      return SIMPLE_NAME_PATTERN.matcher(requireNonNull(name)).matches();
   }

   /**
    * Checks if the given type is part of the {@code java.lang} package. For this, the type is first transformed to a
    * simple type.
    * <p>
    * The consequence is that the following will return {@code true}: {@code isJavaLang("test.String")}, because
    * {@code String} is part of {@code java.lang}.
    * </p>
    * 
    * @param type the type to check
    * @return {@code true}, if the simple name of the type is part of {@code java.lang}, {@code false} otherwise
    * @throws NullPointerException if the type name is {@code null}
    */
   public static boolean isJavaLang(final String type)
   {
      return LANG_TYPES.contains(toSimpleName(requireNonNull(type)));
   }

   /**
    * Checks if the give type is a basic type, so it's either primitive or one of the primitive wrapper classes.
    * 
    * @param type the type to check
    * @return {@code true} if this type is basic, {@code false} otherwise
    * @throws NullPointerException if the type name is {@code null}
    */
   public static boolean isBasicType(String type)
   {
      return isPrimitive(type) || PRIMITIVE_TYPES.contains(requireNonNull(type));
   }

   /**
    * Checks if the given type is generic.
    * 
    * @param type the type to check
    * @return {@code true} if this type is generic, {@code false} otherwise
    * @throws NullPointerException if the given type is {@code null}
    */
   public static boolean isGeneric(String type)
   {
      // in java '<' is a illegal character in names and is only used in identifiers for generics
      return type.contains("<");
   }

   /**
    * Checks if the generics are valid of this given type.
    * 
    * @param type the type to check
    * @return {@code true}, if the generics are valid, {@code false} otherwise
    * @throws NullPointerException if the given type is {@code null}
    */
   public static boolean validateGenerics(String type)
   {
      int genericStart = type.indexOf('<');
      if (genericStart < 0)
      {
         return false;
      }
      String typeWithoutArray = stripArray(type);
      if (!validateName(typeWithoutArray.substring(0, genericStart)))
      {
         return false;
      }
      String typeArgs = typeWithoutArray.substring(genericStart + 1, typeWithoutArray.lastIndexOf('>'));
      StringTokenizer tok = new StringTokenizer(typeArgs, ", ");
      while (tok.hasMoreTokens())
      {
         String typeArg = tok.nextToken();
         while (incompleteGenerics(typeArg) && tok.hasMoreElements())
         {
            typeArg += ',' + tok.nextToken();
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

   /**
    * Removes the generics part of a given type. More specific, the content between the first occurrence of '<' and the
    * last occurrence if '>' is removed.
    * 
    * @param type the type where the generics should be removed
    * @throws NullPointerException if the given type is {@code null}
    * @return the type without generics
    */
   public static String stripGenerics(String type)
   {
      String typeToString = requireNonNull(type);
      if (isClassArray(typeToString))
      {
         typeToString = fixClassArray(typeToString);
      }
      if (isGeneric(typeToString))
      {
         return typeToString.substring(0, typeToString.indexOf('<'))
                  + typeToString.substring(typeToString.lastIndexOf('>') + 1).trim();
      }
      return typeToString;
   }

   /**
    * Returns the generic part of a given type. For example {@code getGenerics("A<B>")} returns {@code "<B>"}.
    * 
    * @param type the type to get the generics from
    * @return the generics from the given type, or an empty string, if the type has not generics
    * @throws NullPointerException if the given type is {@code null}
    */
   public static String getGenerics(final String type)
   {
      if (isGeneric(type))
      {
         return new StringBuilder("<").append(getGenericsTypeParameter(type)).append(">").toString();
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

   /**
    * Checks if the given type is an array.
    * 
    * @param type the type to check
    * @return {@code true}, if the type is an array, {@code false} otherwise
    * @throws NullPointerException if the given type is {@code null}
    */
   public static boolean isArray(final String type)
   {
      if (type.charAt(0) == '[' && CLASS_ARRAY_PATTERN.matcher(requireNonNull(type)).matches())
      {
         return true;
      }
      if (!type.endsWith("]"))
      {
         return false;
      }
      Matcher matcher = SIMPLE_ARRAY_PATTERN.matcher(type);
      if (matcher.find())
      {
         String candidateType = matcher.group(1);
         return validateNameWithGenerics(candidateType);
      }
      return false;
   }

   /**
    * Strips the array from a given type.
    * 
    * @param type the type to remove the array from
    * @return the type without an array
    * @throws NullPointerException if the given type is {@code null}
    */
   public static String stripArray(final String type)
   {
      String result = requireNonNull(type);
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
      if (type == null || type.length() == 0 || type.charAt(0) != '[')
      {
         return false;
      }
      Matcher matcher = CLASS_ARRAY_PATTERN.matcher(type);
      return matcher.find();
   }

   private static String fixClassArray(String type)
   {
      Matcher matcher = CLASS_ARRAY_PATTERN.matcher(type);
      String result = type;
      if (matcher.find())
      {
         int dim = getArrayDimension(type, true);
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
            result = "boolean";
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

   /**
    * Checks if the given type is primitive according to the Java Language Specification.
    * 
    * @param type the type to check
    * @return {@code true} if this type is primitive, {@code false} otherwise
    * @throws NullPointerException if the given type is {@code null}
    */
   public static boolean isPrimitive(final String type)
   {
      return Arrays.asList("byte", "short", "int", "long", "float", "double", "boolean", "char").contains(requireNonNull(type));
   }

   private static int getArrayDimension(String name, boolean isBasic)
   {
      int count = 0;
      String rawName = isBasic ? requireNonNull(name) : stripGenerics(name);

      for (char c : rawName.toCharArray())
      {
         if (c == '[')
         {
            count++;
         }
      }
      return count;
   }

   /**
    * Returns the dimension of the array.
    *
    * It simply counts the "[" from the string.
    *
    * @param name an array type, e.g.: byte[] or [Ljava.lang.Boolean;
    * @return the array dimension. 0 if the type is not a valid array
    * @throws NullPointerException if the given name is {@code null}
    */
   public static int getArrayDimension(String name)
   {
      return getArrayDimension(name, false);
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
    * Returns the default value for a given class according to the Java Language Specification.
    * 
    * @param clazz the class of the type
    * @return the default value
    * @throws NullPointerException if the given class is {@code null}
    */
   public static String getDefaultValue(Class<?> clazz)
   {
      return getDefaultValue(clazz.getName());
   }

   /**
    * Returns the default value for a given type according to the Java Language Specification.
    * 
    * @param type the type
    * @return the default value
    * @throws NullPointerException if the given type is {@code null}
    */
   public static String getDefaultValue(String type)
   {
      if (isPrimitive(type))
      {
         if (type.equals(boolean.class.getName()))
         {
            return "false";
         }
         else if (type.equals(float.class.getName()) || type.equals(double.class.getName()))
         {
            return "0.0";
         }
         else
         {
            return "0";
         }
      }
      return "null";
   }

   /**
    * 
    * Returns the available generics as a String array. Only the first level is split. For example
    * {@code splitGenerics("Foo<Bar<A>, Bar<B>>")} returns an array with {@code Bar<A>} and {@code Bar<B>}.
    * 
    * @param typeName the generic type to split
    * @return an array with the generic parts (maybe empty but never {@code null})
    * @throws NullPointerException if the given type is {@code null}
    */
   public static String[] splitGenerics(String typeName)
   {
      String workingString = typeName.replaceAll("\\s", "");
      int begin = workingString.indexOf('<');
      int end = workingString.lastIndexOf('>');

      if (begin == -1 || end == -1)
      {
         return new String[0];
      }

      workingString = workingString.substring(begin + 1, end);
      int depth = 0;
      final StringBuilder currentPart = new StringBuilder();
      final List<String> genericParts = new ArrayList<>();
      for (int currentIndex = 0; currentIndex < workingString.length(); currentIndex++)
      {
         char currentChar = workingString.charAt(currentIndex);
         if (currentChar == ',' && depth == 0)
         {
            genericParts.add(currentPart.toString());
            currentPart.setLength(0);
            continue;
         }
         else if (currentChar == '<')
         {
            depth++;

         }
         else if (currentChar == '>')
         {
            depth--;
         }
         currentPart.append(currentChar);
      }

      if (currentPart.length() != 0)
      {
         genericParts.add(currentPart.toString());
      }

      return genericParts.toArray(new String[genericParts.size()]);
   }
}