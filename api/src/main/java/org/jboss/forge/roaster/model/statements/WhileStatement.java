package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface WhileStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,WhileStatement<O,T>>,
        BlockHolder<O,WhileStatement<O,T>> {

    WhileStatement<O,T> setCondition( Expression expr );

    WhileStatement<O,T> setBody( Statement block );

}
