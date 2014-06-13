package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ConstructorImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends    ArgumentImpl<O,T,ClassInstanceCreation>
    implements ConstructorExpression<O,T> {

    private ClassInstanceCreation constr;

    private String type;
    private List<Argument<O,ConstructorExpression<O,T>>> arguments = Collections.EMPTY_LIST;

    public ConstructorImpl( String type ) {
        this.type = type;
    }

    @Override
    public ConstructorExpression<O,T> addArgument( Argument<O,ConstructorExpression<O,T>> arg ) {
        if ( arguments.isEmpty() ) {
            arguments = new LinkedList<Argument<O, ConstructorExpression<O, T>>>();
        }
        arguments.add( arg );
        return this;
    }

    @Override
    public ClassInstanceCreation getInternal() {
        return constr;
    }

    @Override
    public void materialize( AST ast ) {
        constr = ast.newClassInstanceCreation();
        constr.setType( JDTHelper.getType( type, ast ) );

        for ( Argument<O,ConstructorExpression<O,T>> arg : arguments ) {
            constr.arguments().add( wireAndGetExpression( arg, this, ast ) );
        }
    }
}

