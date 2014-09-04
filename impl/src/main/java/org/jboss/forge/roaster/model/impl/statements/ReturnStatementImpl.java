package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ReturnStatement;

public class ReturnStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.ReturnStatement,ReturnStatement<O,T>>
        implements ReturnStatement<O,T> {

    private org.eclipse.jdt.core.dom.ReturnStatement ret;

    private Expression result;

    public ReturnStatementImpl() { }

    public ReturnStatement setReturn( Expression expression ) {
        result = expression;
        return this;
    }

    @Override
    public org.eclipse.jdt.core.dom.ReturnStatement getInternal() {
        return ret;
    }

    @Override
    public void materialize( AST ast ) {
        ret = ast.newReturnStatement();

        if ( result != null ) {
            ret.setExpression( wireAndGetExpression( result, this, ast ) );
        }
    }
}
