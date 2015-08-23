/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.statements.StatementSource;

import java.util.List;

public interface BlockSource<O extends JavaSource<O>,
                             P extends BlockHolder<O>,
                             S extends BlockSource<O,P,S>>
        extends Block<O,P> {

        List<StatementSource<O,S,?>> getStatements();

        BlockSource<O,P,S> addStatement( StatementSource<?,?,?> statement );

        BlockSource<O,P,S> addStatement( int index, StatementSource<?,?,?> statement );

        BlockSource<O,P,S> addStatement( ExpressionSource<?,?,?> statement );

        BlockSource<O,P,S> addStatement( int index, ExpressionSource<?,?,?> statement );

        BlockSource<O,P,S> removeStatement( StatementSource<O,S,?> statement );

        BlockSource<O,P,S> removeStatement( int index );
}
