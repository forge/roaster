package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public abstract class ExpressionImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, X extends org.eclipse.jdt.core.dom.Expression>
    extends ExpressionFactoryImpl<O,T,X>
    implements Expression<O,T>,
               JdtExpressionWrapper<O,T,X>  {

    private ExpressionSource parent;

    public ExpressionImpl() { }

    public ExpressionSource getParent() {
        return parent;
    }

    public void wireParent( ExpressionSource parent ) {
        this.parent = parent;
    }
}
