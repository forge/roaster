package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.source.JavaSource;

public class VarArgumentImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends SimpleAccessorImpl<O,T,Name>
        implements Variable<O,T> {

    private Name var;

    private String name;

    private Expression<O,T> expression;

    public VarArgumentImpl( String name ) {
        this.name = name;
    }

    @Override
    public Name getInternal() {
        return var;
    }

    @Override
    public void materialize( AST ast ) {
        var = ast.newName( name );
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        this.expression = child;
    }
}
