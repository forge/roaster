/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
*/
package org.jboss.forge.roaster;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.forge.roaster.model.util.Formatter;

/**
 * Command runner for the formatting.
 */
public class FormatterCommand implements CommandRunner
{
   private boolean quiet = false;
   private boolean recursive = false;
   private File configFile = null;

   @Override
   public void run(List<String> arguments)
   {

      List<File> files = new ArrayList<>();
      for (int i = 0; i < arguments.size(); i++)
      {
         String argument = arguments.get(i);
         if ("--config".equals(argument) || "-c".equals(argument))
         {
            configFile = new File(arguments.get(++i));
            if (!configFile.isFile())
            {
               System.err.println("roaster: configuration file [" + configFile + "] does not exist.");
               return;
            }
         }
         else if ("--recursive".equals(argument) || "-r".equals(argument))
         {
            recursive = true;
         }
         else if ("--quiet".equals(argument) || "-q".equals(argument))
         {
            quiet = true;
         }
         else if (new File(argument).exists())
         {
            files.add(new File(argument));
         }
         else
         {
            System.err.println("roaster: no such file: '" + argument + "'");
            System.err.println("Try 'roaster --help' for more information.");
         }
      }

      format(files);
   }

   private void format(List<File> files)
   {
      for (File file : files)
      {
         if (file.isDirectory())
         {
            format(Arrays.asList(file.listFiles(new RecursiveFileFilter(recursive))));
         }
         else if (file.getName().endsWith(".java"))
         {
            formatJavaFile(file);
         }
      }
   }

   private void formatJavaFile(File file)
   {
      print("Formatting %s ", file.getAbsolutePath());
      try
      {
         if (configFile != null)
         {
            print("using %s ... ", configFile.getAbsolutePath());
            Formatter.format(configFile, file);
         }
         else
         {
            println("... ");
            Formatter.format(file);
         }
      }
      catch (IOException e)
      {
         println("Error: %s", e.getMessage());
         return;
      }
      println("OK!");
   }

   private void println(String text, Object... params)
   {
      if (!quiet)
         System.out.println(String.format(text, params));
   }

   private void print(String text, Object... params)
   {
      if (!quiet)
         System.out.print(String.format(text, params));
   }

   private final class RecursiveFileFilter implements FileFilter
   {
      private final boolean recursive;

      private RecursiveFileFilter(boolean recursive)
      {
         this.recursive = recursive;
      }

      @Override
      public boolean accept(File file)
      {
         return recursive || file.isFile();
      }
   }
}