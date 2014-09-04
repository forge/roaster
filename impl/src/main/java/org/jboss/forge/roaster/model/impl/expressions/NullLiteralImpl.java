package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class NullLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends LiteralImpl<O,T,NullLiteral> {

    private NullLiteral literal;

    public NullLiteralImpl() {}

    @Override
    public NullLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newNullLiteral();
    }
}
