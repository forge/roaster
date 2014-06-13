package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jboss.forge.roaster.model.expressions.ClassLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ClassLiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AccessorImpl<O,T,TypeLiteral>
        implements ClassLiteral<O,T> {

    private String val;

    private TypeLiteral literal;

    public ClassLiteralImpl( String val ) {
        this.val = val;
    }

    @Override
    public TypeLiteral getInternal() {
        return literal;
    }

    @Override
    public void materialize( AST ast ) {
        literal = ast.newTypeLiteral();
        literal.setType( JDTHelper.getType( val, ast ) );
    }

}
