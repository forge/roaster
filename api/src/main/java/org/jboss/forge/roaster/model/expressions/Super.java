package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface Super<O extends JavaSource<O>,T extends ExpressionSource<O>>
        extends AccessBuilder<O,T> {

}
