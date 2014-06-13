package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface ForEachStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,ForEachStatement<O,T>>,
        BlockHolder<O, ForEachStatement<O, T>> {

    ForEachStatement<O, T> setIterator( String klass, String name );

    ForEachStatement<O, T> setIterator( Class klass, String name );

    ForEachStatement<O, T> setSource( Expression expr );

    ForEachStatement<O, T> setBody( Statement block );

}
