package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface BlockStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,BlockStatement<O,T>>,
        Block<O, T> {

    BlockStatement<O,T> addStatement( Statement<?,?,?> statement );

    BlockStatement<O, T> addStatement( Expression<?,?> statement );

}
