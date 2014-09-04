package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;


public abstract class ArgumentImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, X extends Expression>
    extends ExpressionImpl<O,T,X>
    implements Argument<O,T> {

    public ArgumentImpl() { }

}

