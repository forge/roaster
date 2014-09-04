package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OperatorImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,InfixExpression>
        implements OperatorExpression<O,T> {

    private InfixExpression expr;

    private Op operator;
    private List<Argument<O,OperatorExpression<O,T>>> argumentList = Collections.EMPTY_LIST;

    public OperatorImpl( Op op ) {
        this.operator = op;
    }

    @Override
    public OperatorExpression<O, T> addArgument( Argument<O,OperatorExpression<O,T>> arg ) {
        if ( argumentList.isEmpty() ) {
            argumentList = new LinkedList<Argument<O, OperatorExpression<O, T>>>();
        }
        argumentList.add( arg );
        return this;
    }

    @Override
    public InfixExpression getInternal() {
        return expr;
    }

    @Override
    public void materialize( AST ast ) {
        expr = ast.newInfixExpression();

        expr.setOperator( InfixExpression.Operator.toOperator( this.operator.getOp() ) );

        for ( Argument<O,OperatorExpression<O,T>> arg : argumentList ) {
            wireArg( arg, ast );
        }
    }

    protected void wireArg( Argument<O,OperatorExpression<O,T>> arg, AST ast ) {
        Expression child = wireAndGetExpression( arg, this, ast );

        if ( child.getNodeType() == ASTNode.INFIX_EXPRESSION ) {
            if ( ! expr.getOperator().equals( ((InfixExpression) child).getOperator() ) ) {
                ParenthesizedExpression par = ast.newParenthesizedExpression();
                par.setExpression( child );
                child = par;
            }
        }

        InfixExpression infix = ((InfixExpression) expr);
        if (  "MISSING".equals( infix.getLeftOperand().toString() ) ) {
            infix.setLeftOperand( child );
        } else if ( "MISSING".equals( infix.getRightOperand().toString() ) ) {
            infix.setRightOperand( child );
        } else {
            org.eclipse.jdt.core.dom.Expression prev = infix.getRightOperand();
            InfixExpression more = ast.newInfixExpression();
            more.setOperator( infix.getOperator() );
            infix.setRightOperand( more );
            prev.delete();
            more.setLeftOperand( prev );
            more.setRightOperand( child );
        }
    }
}
