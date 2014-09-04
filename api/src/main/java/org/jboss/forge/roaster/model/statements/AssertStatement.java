package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface AssertStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,AssertStatement<O,T>> {

    AssertStatement<O, T> setAssertion( Expression<O,AssertStatement<O,T>> expression );

    AssertStatement<O, T> setMessage( Expression<O,AssertStatement<O,T>> msg );

}
