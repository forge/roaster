/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
*/
package org.jboss.forge.roaster;

import java.util.List;

/**
 * Defines a runner for a certain command.
 * 
 * @author Kai Mueller
 */
public interface CommandRunner
{
   /**
    * Starts the evaluation and execution of a command.
    * 
    * @param args the command line arguments as a {@link List}
    */
   public void run(List<String> args);
}