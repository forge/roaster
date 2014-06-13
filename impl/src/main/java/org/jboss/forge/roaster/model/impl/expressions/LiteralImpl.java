package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Literal;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class LiteralImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, L extends Expression>
        extends    ArgumentImpl<O,T,L>
        implements Literal<O,T> {

    public LiteralImpl() {}

}
