package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface DeclareStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O, T,DeclareStatement<O,T>> {

    public DeclareStatement<O, T> setVariable( Class klass, String name );

    public DeclareStatement<O, T> setVariable( String klass, String name );

    public DeclareStatement<O, T> setInitExpression( Expression init );

    public DeclareStatement<O, T> setDefaultInitExpression();

}
