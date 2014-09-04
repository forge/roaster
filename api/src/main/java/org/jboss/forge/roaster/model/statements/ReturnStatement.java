package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface ReturnStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,ReturnStatement<O,T>> {

    public ReturnStatement<O,T> setReturn( Expression expr );

}
