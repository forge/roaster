package org.jboss.forge.roaster.model.util;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * Global util class to get the current JDT parser options. The following options are guaranteed to be included:
 * <ul>
 * <li>{@link JavaCore#COMPILER_SOURCE}</li>
 * <li>{@link JavaCore#CORE_ENCODING}</li>
 * <li>{@link JavaCore#COMPILER_COMPLIANCE}</li>
 * <li>{@link JavaCore#COMPILER_CODEGEN_TARGET_PLATFORM}</li>
 * </ul>
 * 
 * @author Kai Mueller
 *
 */
public class JDTOptions
{
   private static final Hashtable<String, String> OPTIONS = JavaCore.getOptions();

   static
   {
      OPTIONS.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
      OPTIONS.put(JavaCore.CORE_ENCODING, StandardCharsets.UTF_8.name());
      OPTIONS.put(JavaCore.COMPILER_COMPLIANCE, CompilerOptions.VERSION_11);
      OPTIONS.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, CompilerOptions.VERSION_11);
   }

   private JDTOptions()
   {
      // Util class
   }

   /**
    * Get all globally defined JDT options.
    * 
    * @return a map of all globally defined options as a unmodifiable map
    * @see Collections#unmodifiableMap
    */
   public static Map<String, String> getJDTOptions()
   {
      return Collections.unmodifiableMap(OPTIONS);
   }

   /**
    * Get a certain JDT option.
    * 
    * @param option the option
    * @return the value of the option or {@code null} if the option is not defined
    */
   public static String getOption(String option)
   {
      return OPTIONS.get(option);
   }
}