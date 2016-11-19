package org.jboss.forge.roaster;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.jboss.forge.roaster.model.util.Formatter;

public class Bootstrap
{
   public static void main(final String[] args) throws InterruptedException, ExecutionException, IOException
   {
      final List<String> bootstrapArgs = new ArrayList<String>();
      final Properties systemProperties = System.getProperties();
      // Set system properties
      for (String arg : args)
      {
         if (arg.startsWith("-D"))
         {
            final String name;
            final String value;
            final int index = arg.indexOf("=");
            if (index == -1)
            {
               name = arg.substring(2);
               value = "true";
            }
            else
            {
               name = arg.substring(2, index);
               value = arg.substring(index + 1);
            }
            systemProperties.setProperty(name, value);
         }
         else
         {
            bootstrapArgs.add(arg);
         }
      }

      Bootstrap bootstrap = new Bootstrap();
      bootstrap.run(bootstrapArgs);
   }

   private void run(List<String> args) throws IOException
   {
      if (args.isEmpty() || args.contains("--help") || args.contains("-h"))
      {
         System.out.println(help());
         return;
      }
      boolean quiet = false;
      boolean recursive = false;
      String configFile = null;
      List<File> files = new ArrayList<File>();
      for (int i = 0; i < args.size(); i++)
      {
         String arg = args.get(i);
         if ("--config".equals(arg) || "-c".equals(arg))
         {
            configFile = args.get(++i);
            if (!new File(configFile).isFile())
            {
               System.err.println("roaster: configuration file [" + configFile + "] does not exist.");
               return;
            }
         }
         else if ("--recursive".equals(arg) || "-r".equals(arg))
         {
            recursive = true;
         }
         else if ("--quiet".equals(arg) || "-q".equals(arg))
         {
            quiet = true;
         }
         else if (new File(arg).exists())
         {
            files.add(new File(arg));
         }
         else
         {
            System.err.println("roaster: no such file: '" + arg + "'");
            System.err.println("Try 'roaster --help' for more information.");
         }
      }

      format(files, configFile, recursive, quiet);
   }

   private void format(List<File> files, String configFile, final boolean recursive, final boolean quiet)
   {
      for (File file : files)
      {
         if (file.isDirectory())
         {
            format(Arrays.asList(file.listFiles(new FileFilter()
            {
               @Override
               public boolean accept(File file)
               {
                  return recursive || file.isFile();
               }
            })), configFile, recursive, quiet);
         }
         else if (file.getName().endsWith(".java"))
         {
            if (!quiet)
               System.out.printf("Formatting %s ", file.getAbsolutePath());
            try
            {
               if (configFile != null)
               {
                  if (!quiet)
                     System.out.printf("using %s ... ", new File(configFile).getAbsolutePath());
                  Formatter.format(new File(configFile), file);
               }
               else
               {
                  if (!quiet)
                     System.out.printf("... ");
                  Formatter.format(file);
               }
            }
            catch (IOException e)
            {
               if (!quiet)
                  System.out.println("Error: " + e.getMessage());
               continue;
            }
            if (!quiet)
               System.out.println("OK!");
         }
      }
   }

   private static String help()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Usage: roaster [OPTION]... FILES ... \n");
      sb.append("The simplest command-line Java code formatter. A JBoss Forge sub-project: http://forge.jboss.org\n");
      sb.append("\n");
      sb.append("-c, --config [CONFIG_FILE]\n");
      sb.append("\t specify the path to the Eclipse code format profile (usually found at '$PROJECT/.settings/org.eclipse.jdt.core.prefs') \n");
      sb.append("\n");
      sb.append("-r, --recursive\n");
      sb.append("\t format files in found sub-directories recursively \n");
      sb.append("\n");
      sb.append("FILES... \n");
      sb.append("\t specify one or more space-separated files or directories to format \n");
      sb.append("\n");
      sb.append("-q, --quiet\n");
      sb.append("\t do not display any output \n");
      sb.append("\n");
      sb.append("-h, --help\n");
      sb.append("\t display this help and exit \n");
      return sb.toString();
   }
}