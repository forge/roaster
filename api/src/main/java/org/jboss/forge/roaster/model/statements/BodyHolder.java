package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a {@link org.jboss.forge.roaster.model.statements.Statement}
 * that is the parent of a body block
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */

public interface BodyHolder<O extends JavaSource<O>,
      P extends BlockHolder<O>,
      B extends BodyHolder<O,P,B>>
      extends BlockHolder<O>
{

   /**
    * Returns the body as a block containing other statements
    * @return the body as a block containing other statements
    */
   public BlockStatement<O,B> getBody();

   /**
    * Sets the body from a whole block
    * @param body The block containing the body statements
    * @return this <code>BodyHolder</code> itself
    */
   public B setBody(BlockSource<?,?,?> body);

   /**
    * Sets the body from a single statement, wrapping it inside a block
    * @param body The statement that will become the body
    * @return this <code>BodyHolder</code> itself
    */
   public B setBody(StatementSource<?,?,?> body);

}
