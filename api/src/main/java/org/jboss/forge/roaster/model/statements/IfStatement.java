package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface IfStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,IfStatement<O,T>>,
        BlockHolder<O, IfStatement<O, T>> {

    IfStatement<O, T> setCondition( Expression condition );

    IfStatement<O, T> setThen( Statement block );

    IfStatement<O, T> setElse( Statement block );

}
