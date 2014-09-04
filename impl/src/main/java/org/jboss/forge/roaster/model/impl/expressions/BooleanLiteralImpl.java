package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class BooleanLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends LiteralImpl<O,T,BooleanLiteral> {

    private boolean val;

    private BooleanLiteral literal;

    public BooleanLiteralImpl( boolean val ) {
        this.val = val;
    }

    @Override
    public BooleanLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newBooleanLiteral( val );
    }
}
