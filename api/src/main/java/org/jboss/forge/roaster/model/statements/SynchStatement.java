package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface SynchStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,SynchStatement<O,T>>,
        BlockHolder<O, SynchStatement<O, T>> {

    SynchStatement<O, T> setSynchOn( Expression expr );

    SynchStatement<O, T> setBody( Statement block );

}
