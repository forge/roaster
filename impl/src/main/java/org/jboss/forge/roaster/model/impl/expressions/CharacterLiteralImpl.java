package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class CharacterLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends LiteralImpl<O,T,CharacterLiteral> {

    private Character val;

    private CharacterLiteral literal;

    public CharacterLiteralImpl( Character val ) {
        this.val = val;
    }

    @Override
    public CharacterLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newCharacterLiteral();
        literal.setCharValue( val );
    }
}
