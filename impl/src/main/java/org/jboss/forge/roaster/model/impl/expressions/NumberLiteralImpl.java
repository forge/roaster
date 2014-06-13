package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class NumberLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends LiteralImpl<O,T,NumberLiteral> {

    private Number val;

    private NumberLiteral literal;

    public NumberLiteralImpl( Number val ) {
        this.val = val;
    }

    @Override
    public NumberLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newNumberLiteral( val.toString() );
    }
}
