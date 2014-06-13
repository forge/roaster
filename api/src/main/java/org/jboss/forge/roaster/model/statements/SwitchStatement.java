package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface SwitchStatement<O extends JavaSource<O>, T extends Block<O, ? extends BlockHolder<O, ?>>>
        extends Statement<O,T,SwitchStatement<O,T>> {

    SwitchStatement<O, T> addCase( Expression<O,SwitchStatement<O,T>> option );

    SwitchStatement<O, T> addDefault();

    SwitchStatement<O, T> addStatement( Statement<?,?,?> arg );

    SwitchStatement<O, T> setSwitchOn( Expression<O,SwitchStatement<O,T>> expr );

}
