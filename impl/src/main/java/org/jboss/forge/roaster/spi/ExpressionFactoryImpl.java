package org.jboss.forge.roaster.spi;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.expressions.AccessBuilder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.ClassLiteral;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.expressions.Literal;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.impl.NodeImpl;
import org.jboss.forge.roaster.model.impl.expressions.ArrayAccessImpl;
import org.jboss.forge.roaster.model.impl.expressions.ArrayImpl;
import org.jboss.forge.roaster.model.impl.expressions.ArrayInitImpl;
import org.jboss.forge.roaster.model.impl.expressions.AssignImpl;
import org.jboss.forge.roaster.model.impl.expressions.BooleanLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.CastImpl;
import org.jboss.forge.roaster.model.impl.expressions.CharacterLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.ClassLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.ConstructorImpl;
import org.jboss.forge.roaster.model.impl.expressions.DeclareExpressionImpl;
import org.jboss.forge.roaster.model.impl.expressions.FieldImpl;
import org.jboss.forge.roaster.model.impl.expressions.GetterImpl;
import org.jboss.forge.roaster.model.impl.expressions.InstanceofImpl;
import org.jboss.forge.roaster.model.impl.expressions.MethodInvokeImpl;
import org.jboss.forge.roaster.model.impl.expressions.NotImpl;
import org.jboss.forge.roaster.model.impl.expressions.NullLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.NumberLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.OperatorImpl;
import org.jboss.forge.roaster.model.impl.expressions.ParenImpl;
import org.jboss.forge.roaster.model.impl.expressions.SelfArgumentImpl;
import org.jboss.forge.roaster.model.impl.expressions.SetterImpl;
import org.jboss.forge.roaster.model.impl.expressions.StaticClassAccessorImpl;
import org.jboss.forge.roaster.model.impl.expressions.StringLiteralImpl;
import org.jboss.forge.roaster.model.impl.expressions.SuperImpl;
import org.jboss.forge.roaster.model.impl.expressions.TernaryImpl;
import org.jboss.forge.roaster.model.impl.expressions.UnaryImpl;
import org.jboss.forge.roaster.model.impl.expressions.VarArgumentImpl;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ExpressionFactoryImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, J extends Expression>
        extends NodeImpl<O,T,J>
        implements ExpressionFactory<O,T> {


    public ExpressionFactoryImpl() { }

    @Override
    public J getInternal() {
        throw new UnsupportedOperationException( "Method should not be called directly on this class" );
    }

    @Override
    public void materialize( AST ast ) {
        throw new UnsupportedOperationException( "Method should not be called directly on this class" );
    }

    @Override
    public Literal<O, T> literal( String val ) {
        return new StringLiteralImpl<O,T>( val );
    }

    @Override
    public Literal<O, T> literal( Number val ) {
        return new NumberLiteralImpl<O,T>( val );
    }

    @Override
    public Literal<O, T> literal( Character val ) {
        return new CharacterLiteralImpl<O,T>( val );
    }

    @Override
    public ClassLiteral<O,T> classLiteral( Class<?> klass ) {
        return classLiteral( klass.getName() );
    }

    @Override
    public ClassLiteral<O,T> classLiteral( String klass ) {
        return new ClassLiteralImpl<O,T>( klass );
    }

    @Override
    public Literal<O, T> literal( Boolean val ) {
        return new BooleanLiteralImpl<O,T>( val );
    }

    public Literal<O,T> zeroLiteral( Class klass ) {
        return zeroLiteral( klass.getName() );
    }

    public Literal<O,T> zeroLiteral( String klass ) {
        if ( boolean.class.getName().equals( klass ) ) {
            return literal( false );
        } else if ( byte.class.getName().equals( klass  ) ) {
            return literal( 0 );
        } else if ( char.class.getName().equals( klass ) ) {
            return literal( 0 );
        } else if ( double.class.getName().equals( klass  ) ) {
            return literal( 0.0 );
        } else if ( float.class.getName().equals( klass ) ) {
            return literal( 0.0f );
        } else if ( int.class.getName().equals( klass ) ) {
            return literal( 0 );
        } else if ( long.class.getName().equals( klass ) ) {
            return literal( 0L );
        } else if ( short.class.getName().equals( klass ) ) {
            return literal( 0 );
        } else {
            return nullLiteral();
        }
    }

    @Override
    public OperatorExpression<O,T> operator( Op op ) {
        return new OperatorImpl<O,T>( op );
    }

    @Override
    public UnaryExpression<O,T> operator( PrefixOp op, org.jboss.forge.roaster.model.expressions.Argument<O,UnaryExpression<O,T>> arg ) {
        return new UnaryImpl<O,T>( op, arg );
    }

    @Override
    public Literal<O,T> nullLiteral() {
        return new NullLiteralImpl<O, T>();
    }

    @Override
    public UnaryExpression<O,T> not( Argument<O,UnaryExpression<O,T>> arg ) {
        return new NotImpl<O,T>( arg );
    }

    @Override
    public Field<O,T> field( String fieldName ) {
        return new FieldImpl<O, T>( fieldName );
    }

    @Override
    public Getter<O,T> getter( String fieldName, String type ) {
        return new GetterImpl<O,T>( fieldName, type );
    }

    @Override
    public Getter<O,T> getter( String fieldName, Class type ) {
        return getter( fieldName, type.getName() );
    }

    @Override
    public Setter<O, T> setter( String fldName, String type, org.jboss.forge.roaster.model.expressions.Expression<O,Setter<O,T>> value ) {
        return new SetterImpl<O,T>( fldName, type, value );
    }

    @Override
    public Setter<O, T> setter( String fldName, Class type, org.jboss.forge.roaster.model.expressions.Expression<O,Setter<O,T>> value ) {
        return setter( fldName, type.getName(), value );
    }

    @Override
    public DeclareExpression declare( String klass, String name ) {
        return new DeclareExpressionImpl<O,T>( klass, name );
    }

    @Override
    public DeclareExpression declare( Class klass, String name ) {
        return declare( klass.getName(), name );
    }

    @Override
    public MethodExpression<O,T> invoke( String method ) {
        return new MethodInvokeImpl<O, T>( method );
    }

    @Override
    public CastExpression<O,T> cast( String klass, org.jboss.forge.roaster.model.expressions.Expression<O,CastExpression<O,T>> arg ) {
        CastExpression<O,T> expr = new CastImpl<O, T>( klass, arg );
        return expr;
    }

    @Override
    public CastExpression<O,T> cast( Class klass, org.jboss.forge.roaster.model.expressions.Expression<O,CastExpression<O,T>> arg ) {
        return cast( klass.getName(), arg );
    }

    @Override
    public ConstructorExpression<O,T> newInstance( String klass ) {
        return new ConstructorImpl<O,T>( klass );
    }

    @Override
    public ConstructorExpression<O,T> newInstance( Class<?> klass ) {
        return newInstance( klass.getName() );
    }

    @Override
    public ArrayConstructorExpression<O,T> newArray( String klass ) {
        return new ArrayImpl<O,T>( klass );
    }

    @Override
    public ArrayConstructorExpression<O,T> newArray( Class<?> klass ) {
        return newArray( klass.getName() );
    }

    @Override
    public Variable<O,T> var( String varName ) {
        return new VarArgumentImpl<O,T>( varName );
    }

    @Override
    public AssignExpression<O,T> assign( Assignment operator ) {
        return new AssignImpl<O,T>( operator );
    }

    @Override
    public Accessor<O,T> thisLiteral() {
        return new SelfArgumentImpl<O,T>();
    }

    public ParenExpression<O,T> paren( org.jboss.forge.roaster.model.expressions.Expression<O,ParenExpression<O,T>> child) {
        return new ParenImpl<O,T>( child );
    }

    @Override
    public TernaryExpression<O,T> ternary() {
        return new TernaryImpl<O,T>();
    }

    public Accessor<O,T> classStatic( String klass ) {
        return new StaticClassAccessorImpl<O,T>( klass );
    }

    public Accessor<O,T> classStatic( Class klass ) {
        return classStatic( klass.getName() );
    }

    @Override
    public InstanceofExpression<O,T> instanceOf( String klass, org.jboss.forge.roaster.model.expressions.Expression<O,InstanceofExpression<O,T>> arg ) {
        InstanceofExpression<O,T> expr = new InstanceofImpl<O, T>( klass, arg );
        return expr;
    }

    @Override
    public InstanceofExpression<O,T> instanceOf( Class klass, org.jboss.forge.roaster.model.expressions.Expression<O,InstanceofExpression<O,T>> arg ) {
        return instanceOf( klass.getName(), arg );
    }

    @Override
    public Super sup() {
        return new SuperImpl();
    }

    @Override
    public ArrayInit<O, ArrayConstructorExpression<O, T>> vec() {
        return new ArrayInitImpl<O, ArrayConstructorExpression<O, T>>();
    }

    @Override
    public AccessBuilder<O, T> itemAt( org.jboss.forge.roaster.model.expressions.Expression<O,ArrayIndexer<O,T>> index ) {
        return new ArrayAccessImpl<O,T>( index );
    }
}
