package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Literal;
import org.jboss.forge.roaster.model.source.JavaSource;

public class StringLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AccessorImpl<O,T,StringLiteral>
        implements Literal<O,T> {

    private String val;

    private StringLiteral literal;

    public StringLiteralImpl( String val ) {
        this.val = val;
    }

    @Override
    public StringLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newStringLiteral();
        literal.setLiteralValue( val );
    }
}
