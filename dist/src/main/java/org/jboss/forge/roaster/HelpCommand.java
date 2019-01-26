/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
*/
package org.jboss.forge.roaster;

import java.util.List;

/**
 * Runner for the 'help' command. This command will print the usage of the this and all other commands to
 * {@link System#out}.
 */
public class HelpCommand implements CommandRunner
{

   @Override
   public void run(List<String> args)
   {
      System.out.println(help());
   }

   private static String help()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Usage: roaster [OPTION]... FILES ... \n");
      sb.append("The simplest command-line Java code formatter. A JBoss Forge sub-project: http://forge.jboss.org\n");
      sb.append("\n");
      sb.append("-c, --config [CONFIG_FILE]\n");
      sb.append(
               "\t specify the path to the Eclipse code format profile (usually found at '$PROJECT/.settings/org.eclipse.jdt.core.prefs') \n");
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