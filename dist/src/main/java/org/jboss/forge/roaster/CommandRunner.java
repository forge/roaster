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