package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface Statement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>, S extends Statement<O,T,S>>
        extends Origin<T>,
        ExpressionSource<O> {

    public BlockStatement wrap();

    public S setLabel( String label );

    public String getLabel();

    public boolean hasLabel();
}
