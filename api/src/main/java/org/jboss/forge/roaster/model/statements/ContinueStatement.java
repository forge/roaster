package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface ContinueStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,ContinueStatement<O,T>> {

    public ContinueStatement<O,T> setTarget( String label );

}
