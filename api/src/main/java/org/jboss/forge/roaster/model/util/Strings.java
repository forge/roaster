/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * String utilities.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public class Strings
{
   /**
    * Capitalize the given String: "input" -> "Input"
    */
   public static String capitalize(final String input)
   {
      if ((input == null) || (input.length() == 0))
      {
         return input;
      }
      return input.substring(0, 1).toUpperCase() + input.substring(1);
   }

   public static String unquote(final String value)
   {
      String result = null;
      if (value != null)
      {
         result = value.replaceAll("\"(.*)\"", "$1");
      }
      return StringEscapeUtils.unescapeJava(result);
   }

   public static String enquote(final String value)
   {
      String result = null;
      if (value != null)
      {
         result = "\"" + StringEscapeUtils.escapeJava(value) + "\"";
      }
      return result;
   }

   public static String join(final Collection<?> collection, final String delimiter)
   {
      StringBuilder buffer = new StringBuilder();
      Iterator<?> iter = collection.iterator();
      while (iter.hasNext())
      {
         buffer.append(iter.next());
         if (iter.hasNext())
         {
            buffer.append(delimiter);
         }
      }
      return buffer.toString();
   }

   public static boolean isNullOrEmpty(final String string)
   {
      return (string == null) || string.isEmpty();
   }

   public static boolean isBlank(final String string)
   {
      return string == null || string.trim().isEmpty();
   }

   public static boolean isTrue(final String value)
   {
      return value == null ? false : "true".equalsIgnoreCase(value.trim());
   }

   public static boolean areEqual(final String left, final String right)
   {
      if ((left == null) && (right == null))
      {
         return true;
      }
      else if ((left == null) || (right == null))
      {
         return false;
      }
      return left.equals(right);
   }

   public static boolean areEqualTrimmed(final String left, final String right)
   {
      if ((left != null) && (right != null))
      {
         return left.trim().equals(right.trim());
      }
      return areEqual(left, right);
   }

   public static String stripQuotes(String value)
   {
      if ((value != null) && ((value.startsWith("'") && value.endsWith("'"))
               || (value.startsWith("\"") && value.endsWith("\"")))
               && (value.length() > 2))
      {
         value = value.substring(1, value.length() - 2);
      }
      return value;
   }

   public static String uncapitalize(final String input)
   {
      if ((input == null) || (input.length() == 0))
      {
         return input;
      }
      return input.substring(0, 1).toLowerCase() + input.substring(1);
   }

   /**
    * <p>
    * Find the Levenshtein distance between two Strings.
    * </p>
    *
    * <p>
    * This is the number of changes needed to change one String into another, where each change is a single character
    * modification (deletion, insertion or substitution).
    * </p>
    *
    * <p>
    * The previous implementation of the Levenshtein distance algorithm was from <a
    * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
    * </p>
    *
    * <p>
    * Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError which can occur when my Java
    * implementation is used with very large strings.<br>
    * This implementation of the Levenshtein distance algorithm is from <a
    * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
    * </p>
    *
    * <pre>
    * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
    * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
    * StringUtils.getLevenshteinDistance("","")               = 0
    * StringUtils.getLevenshteinDistance("","a")              = 1
    * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
    * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
    * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
    * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
    * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
    * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
    * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
    * </pre>
    *
    * @param s the first String, must not be null
    * @param t the second String, must not be null
    * @return result distance
    * @throws IllegalArgumentException if either String input {@code null}
    */
   public static int getLevenshteinDistance(CharSequence s, CharSequence t)
   {
      if (s == null || t == null)
      {
         throw new IllegalArgumentException("Strings must not be null");
      }

      /*
       * The difference between this impl. and the previous is that, rather than creating and retaining a matrix of size
       * s.length() + 1 by t.length() + 1, we maintain two single-dimensional arrays of length s.length() + 1. The
       * first, d, is the 'current working' distance array that maintains the newest distance cost counts as we iterate
       * through the characters of String s. Each time we increment the index of String t we are comparing, d is copied
       * to p, the second int[]. Doing so allows us to retain the previous cost counts as required by the algorithm
       * (taking the minimum of the cost count to the left, up one, and diagonally up and to the left of the current
       * cost count being calculated). (Note that the arrays aren't really copied anymore, just switched...this is
       * clearly much better than cloning an array or doing a System.arraycopy() each time through the outer loop.)
       * 
       * Effectively, the difference between the two implementations is this one does not cause an out of memory
       * condition when calculating the LD over two very large strings.
       */

      int n = s.length(); // length of s
      int m = t.length(); // length of t

      if (n == 0)
      {
         return m;
      }
      else if (m == 0)
      {
         return n;
      }

      if (n > m)
      {
         // swap the input strings to consume less memory
         CharSequence tmp = s;
         s = t;
         t = tmp;
         n = m;
         m = t.length();
      }

      int p[] = new int[n + 1]; // 'previous' cost array, horizontally
      int d[] = new int[n + 1]; // cost array, horizontally
      int _d[]; // placeholder to assist in swapping p and d

      // indexes into strings s and t
      int i; // iterates through s
      int j; // iterates through t

      char t_j; // jth character of t

      int cost; // cost

      for (i = 0; i <= n; i++)
      {
         p[i] = i;
      }

      for (j = 1; j <= m; j++)
      {
         t_j = t.charAt(j - 1);
         d[0] = j;

         for (i = 1; i <= n; i++)
         {
            cost = s.charAt(i - 1) == t_j ? 0 : 1;
            // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
            d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
         }

         // copy current distance counts to 'previous row' distance counts
         _d = p;
         p = d;
         d = _d;
      }

      // our last action in the above loop was to switch d and p, so p now
      // actually has the most recent cost counts
      return p[n];
   }

   /**
    * <p>
    * Find the Levenshtein distance between two Strings if it's less than or equal to a given threshold.
    * </p>
    *
    * <p>
    * This is the number of changes needed to change one String into another, where each change is a single character
    * modification (deletion, insertion or substitution).
    * </p>
    *
    * <p>
    * This implementation follows from Algorithms on Strings, Trees and Sequences by Dan Gusfield and Chas Emerick's
    * implementation of the Levenshtein distance algorithm from <a
    * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
    * </p>
    *
    * <pre>
    * StringUtils.getLevenshteinDistance(null, *, *)             = IllegalArgumentException
    * StringUtils.getLevenshteinDistance(*, null, *)             = IllegalArgumentException
    * StringUtils.getLevenshteinDistance(*, *, -1)               = IllegalArgumentException
    * StringUtils.getLevenshteinDistance("","", 0)               = 0
    * StringUtils.getLevenshteinDistance("aaapppp", "", 8)       = 7
    * StringUtils.getLevenshteinDistance("aaapppp", "", 7)       = 7
    * StringUtils.getLevenshteinDistance("aaapppp", "", 6))      = -1
    * StringUtils.getLevenshteinDistance("elephant", "hippo", 7) = 7
    * StringUtils.getLevenshteinDistance("elephant", "hippo", 6) = -1
    * StringUtils.getLevenshteinDistance("hippo", "elephant", 7) = 7
    * StringUtils.getLevenshteinDistance("hippo", "elephant", 6) = -1
    * </pre>
    *
    * @param s the first String, must not be null
    * @param t the second String, must not be null
    * @param threshold the target threshold, must not be negative
    * @return result distance, or {@code -1} if the distance would be greater than the threshold
    * @throws IllegalArgumentException if either String input {@code null} or negative threshold
    */
   public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold)
   {
      if (s == null || t == null)
      {
         throw new IllegalArgumentException("Strings must not be null");
      }
      if (threshold < 0)
      {
         throw new IllegalArgumentException("Threshold must not be negative");
      }

      /*
       * This implementation only computes the distance if it's less than or equal to the threshold value, returning -1
       * if it's greater. The advantage is performance: unbounded distance is O(nm), but a bound of k allows us to
       * reduce it to O(km) time by only computing a diagonal stripe of width 2k + 1 of the cost table. It is also
       * possible to use this to compute the unbounded Levenshtein distance by starting the threshold at 1 and doubling
       * each time until the distance is found; this is O(dm), where d is the distance.
       * 
       * One subtlety comes from needing to ignore entries on the border of our stripe eg. p[] = |#|#|#|* d[] = *|#|#|#|
       * We must ignore the entry to the left of the leftmost member We must ignore the entry above the rightmost member
       * 
       * Another subtlety comes from our stripe running off the matrix if the strings aren't of the same size. Since
       * string s is always swapped to be the shorter of the two, the stripe will always run off to the upper right
       * instead of the lower left of the matrix.
       * 
       * As a concrete example, suppose s is of length 5, t is of length 7, and our threshold is 1. In this case we're
       * going to walk a stripe of length 3. The matrix would look like so:
       * 
       * 1 2 3 4 5 1 |#|#| | | | 2 |#|#|#| | | 3 | |#|#|#| | 4 | | |#|#|#| 5 | | | |#|#| 6 | | | | |#| 7 | | | | | |
       * 
       * Note how the stripe leads off the table as there is no possible way to turn a string of length 5 into one of
       * length 7 in edit distance of 1.
       * 
       * Additionally, this implementation decreases memory usage by using two single-dimensional arrays and swapping
       * them back and forth instead of allocating an entire n by m matrix. This requires a few minor changes, such as
       * immediately returning when it's detected that the stripe has run off the matrix and initially filling the
       * arrays with large values so that entries we don't compute are ignored.
       * 
       * See Algorithms on Strings, Trees and Sequences by Dan Gusfield for some discussion.
       */

      int n = s.length(); // length of s
      int m = t.length(); // length of t

      // if one string is empty, the edit distance is necessarily the length of the other
      if (n == 0)
      {
         return m <= threshold ? m : -1;
      }
      else if (m == 0)
      {
         return n <= threshold ? n : -1;
      }

      if (n > m)
      {
         // swap the two strings to consume less memory
         CharSequence tmp = s;
         s = t;
         t = tmp;
         n = m;
         m = t.length();
      }

      int p[] = new int[n + 1]; // 'previous' cost array, horizontally
      int d[] = new int[n + 1]; // cost array, horizontally
      int _d[]; // placeholder to assist in swapping p and d

      // fill in starting table values
      int boundary = Math.min(n, threshold) + 1;
      for (int i = 0; i < boundary; i++)
      {
         p[i] = i;
      }
      // these fills ensure that the value above the rightmost entry of our
      // stripe will be ignored in following loop iterations
      Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
      Arrays.fill(d, Integer.MAX_VALUE);

      // iterates through t
      for (int j = 1; j <= m; j++)
      {
         char t_j = t.charAt(j - 1); // jth character of t
         d[0] = j;

         // compute stripe indices, constrain to array size
         int min = Math.max(1, j - threshold);
         int max = Math.min(n, j + threshold);

         // the stripe may lead off of the table if s and t are of different sizes
         if (min > max)
         {
            return -1;
         }

         // ignore entry left of leftmost
         if (min > 1)
         {
            d[min - 1] = Integer.MAX_VALUE;
         }

         // iterates through [min, max] in s
         for (int i = min; i <= max; i++)
         {
            if (s.charAt(i - 1) == t_j)
            {
               // diagonally left and up
               d[i] = p[i - 1];
            }
            else
            {
               // 1 + minimum of cell to the left, to the top, diagonally left and up
               d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
            }
         }

         // copy current distance counts to 'previous row' distance counts
         _d = p;
         p = d;
         d = _d;
      }

      // if p[n] is greater than the threshold, there's no guarantee on it being the correct
      // distance
      if (p[n] <= threshold)
      {
         return p[n];
      }
      else
      {
         return -1;
      }
   }

   @Deprecated
   public static int countNumberOfOccurences(String text, String toMatch)
   {
      return countNumberOfOccurrences(text, toMatch);
   }

   public static int countNumberOfOccurrences(String text, String toMatch)
   {
      int count = 0;
      if (toMatch == null || toMatch.length() < 1)
      {
         return count;
      }

      String wholeText = text;
      int idx = 0;
      while ((idx = wholeText.indexOf(toMatch, idx)) != -1)
      {
         count++;
         idx = idx + toMatch.length();
      }
      return count;
   }
}
