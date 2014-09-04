package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Wireable;
import org.jboss.forge.roaster.model.source.JavaSource;

public class StaticClassAccessorImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AccessorImpl<O,T,Name>
        implements Wireable<O,T> {

    private Name klass;

    private String name;

    private Expression<O,T> expression;

    public StaticClassAccessorImpl( String name ) {
        this.name = name;
    }

    @Override
    public Name getInternal() {
        return klass;
    }

    @Override
    public void materialize( AST ast ) {
        klass = ast.newName( name );
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        this.expression = child;
    }
}
