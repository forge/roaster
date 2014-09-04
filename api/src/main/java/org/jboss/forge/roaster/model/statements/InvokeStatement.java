package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface InvokeStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,InvokeStatement<O,T>> {

    public InvokeStatement<O,T> setMethod( String method );

    public InvokeStatement<O,T> setTarget( Accessor target );

    public InvokeStatement<O,T> addArgument( Argument argument );

}
