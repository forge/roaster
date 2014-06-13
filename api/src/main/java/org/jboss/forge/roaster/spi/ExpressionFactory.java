package org.jboss.forge.roaster.spi;


import org.jboss.forge.roaster.model.expressions.AccessBuilder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.ConstructorBuilder;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.expressions.LiteralBuilder;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface ExpressionFactory<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends AccessBuilder<O,T>,
            LiteralBuilder<O,T>,
        ConstructorBuilder<O,T> {

    public Variable<O,T> var( String varName );

    public OperatorExpression<O,T> operator( Op op );

    public UnaryExpression<O,T> operator( PrefixOp operator, Argument<O,UnaryExpression<O,T>> arg );

    public TernaryExpression<O,T> ternary();

    public CastExpression<O,T> cast( String klass, Expression<O,CastExpression<O,T>> arg );

    public CastExpression<O,T> cast( Class klass, Expression<O,CastExpression<O,T>> arg );

    public ParenExpression<O,T> paren( Expression<O,ParenExpression<O,T>> inner );

    public UnaryExpression<O,T> not( Argument<O,UnaryExpression<O,T>> arg );

    public AssignExpression<O,T> assign( Assignment operator );

    public DeclareExpression<O,T> declare( String klass, String name );

    public DeclareExpression<O,T> declare( Class klass, String name );

    public Accessor<O,T> classStatic( String klass );

    public Accessor<O,T> classStatic( Class klass );

    public InstanceofExpression<O,T> instanceOf( String klass, Expression<O,InstanceofExpression<O,T>> arg );

    public InstanceofExpression<O,T> instanceOf( Class klass, Expression<O,InstanceofExpression<O,T>> arg );

    public Super sup();

    public ArrayInit<O,ArrayConstructorExpression<O,T>> vec();
}
