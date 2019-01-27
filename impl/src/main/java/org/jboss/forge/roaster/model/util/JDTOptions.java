package org.jboss.forge.roaster.model.util;

import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

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
   private static Hashtable<String, String> OPTIONS = JavaCore.getOptions();

   static
   {
      OPTIONS.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
      OPTIONS.put(JavaCore.CORE_ENCODING, StandardCharsets.UTF_8.name());
      OPTIONS.put(JavaCore.COMPILER_COMPLIANCE, CompilerOptions.VERSION_1_8);
      OPTIONS.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, CompilerOptions.VERSION_1_8);
   }

   private JDTOptions()
   {
      // Util class
   }

   public static Hashtable<String, String> getJDTOptions()
   {
      return OPTIONS;
   }

   public static String getOption(String option)
   {
      return OPTIONS.get(option);
   }
}