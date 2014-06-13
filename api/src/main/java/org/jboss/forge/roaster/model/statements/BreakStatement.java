package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface BreakStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,BreakStatement<O,T>> {

    public BreakStatement<O,T> setTarget( String label );

}
