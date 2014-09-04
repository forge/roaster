package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface AssignStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,AssignStatement<O,T>> {

    public AssignStatement<O, T> setLeft( Accessor left );

    public AssignStatement<O, T> setRight( Expression right );

}
