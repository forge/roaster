package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.jboss.forge.roaster.model.expressions.BareArgument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class PostFixImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,PostfixExpression> {

    private PostfixExpression post;

    private String op;
    private BareArgument<O,T> arg;

    public PostFixImpl( String op, BareArgument<O,T> arg ) {
        this.op = op;
        this.arg = arg;
    }

    @Override
    public PostfixExpression getInternal() {
        return post;
    }

    @Override
    public void materialize( AST ast ) {
        post = ast.newPostfixExpression();
        post.setOperator( PostfixExpression.Operator.toOperator( op ) );

        if ( arg != null ) {
            post.setOperand( wireAndGetExpression( arg, this, ast ) );
        }
    }
}
