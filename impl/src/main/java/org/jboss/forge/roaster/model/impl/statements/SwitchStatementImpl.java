package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SwitchStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, SwitchStatement, org.jboss.forge.roaster.model.statements.SwitchStatement<O,T>>
        implements org.jboss.forge.roaster.model.statements.SwitchStatement<O,T> {

    private SwitchStatement opts;

    private Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O,T>> expression;
    private List<Statement<?,?,?>> statements = Collections.EMPTY_LIST;

    public SwitchStatementImpl() { }


    @Override
    public SwitchStatement getInternal() {
        return opts;
    }

    @Override
    public void materialize( AST ast ) {
        opts = ast.newSwitchStatement();
        opts.setExpression( wireAndGetExpression( expression, this, ast ) );

        for ( Statement<?,?,?> stat : statements ) {
            if ( SwitchMockStatement.class.isInstance( stat ) ) {
                SwitchMockStatement mock = (SwitchMockStatement) stat;
                SwitchCase opt = ast.newSwitchCase();
                if ( mock.getOption() == null ) {
                    opt.setExpression( null );
                } else {
                    opt.setExpression( wireAndGetExpression( mock.getOption(), this, ast ) );
                }
                opts.statements().add( opt );
            } else {
                opts.statements().add( wireAndGetStatement( stat, this, ast ) );
            }
        }
    }

    @Override
    public org.jboss.forge.roaster.model.statements.SwitchStatement<O, T> addCase( Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O, T>> option ) {
        if ( statements.isEmpty() ) {
            statements = new LinkedList<Statement<?,?,?>>(  );
        }
        statements.add( new SwitchMockStatement( option ) );
        return this;
    }

    @Override
    public org.jboss.forge.roaster.model.statements.SwitchStatement<O, T> addDefault() {
        statements.add(  new SwitchMockStatement( null ) );
        return this;
    }

    @Override
    public org.jboss.forge.roaster.model.statements.SwitchStatement<O, T> addStatement( Statement<?, ?, ?> arg ) {
        if ( statements.isEmpty() ) {
            statements = new LinkedList<Statement<?,?,?>>(  );
        }
        statements.add( arg );
        return this;
    }

    @Override
    public org.jboss.forge.roaster.model.statements.SwitchStatement<O, T> setSwitchOn( Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O, T>> expr ) {
        this.expression = expr;
        return this;
    }

    private class SwitchMockStatement extends ExpressionStatementImpl<O,T> {
        private Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O, T>> option;

        public SwitchMockStatement( Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O, T>> option ) {
            this.option = option;
        }

        public Expression<O, org.jboss.forge.roaster.model.statements.SwitchStatement<O, T>> getOption() {
            return option;
        }
    }
}


