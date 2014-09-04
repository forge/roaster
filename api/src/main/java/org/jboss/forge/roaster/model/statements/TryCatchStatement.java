package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface TryCatchStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,TryCatchStatement<O,T>>,
        BlockHolder<O, TryCatchStatement<O, T>> {

    TryCatchStatement<O, T> addCatch( DeclareExpression declaration, Statement block );

    TryCatchStatement<O, T> setFinally( Statement block );

    TryCatchStatement<O, T> setBody( Statement block );

}
