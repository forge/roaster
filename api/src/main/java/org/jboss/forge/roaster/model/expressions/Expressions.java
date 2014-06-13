package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.spi.ExpressionFactory;

import java.util.ServiceLoader;

public abstract class Expressions {

    protected static ExpressionFactory factory;

    protected static ExpressionFactory getFactory() {
        synchronized ( Expressions.class ) {
            ServiceLoader<ExpressionFactory> sl = ServiceLoader.load( ExpressionFactory.class, Expressions.class.getClassLoader() );
            if ( sl.iterator().hasNext() ) {
                factory = sl.iterator().next();
            } else {
                throw new IllegalStateException( "No ExpressionFactory implementation available, unable to continue" );
            }
        }
        return factory;
    }

    public static Literal literal( String val ) {
        return getFactory().literal( val );
    }

    public static Literal literal( Number val ) {
        return getFactory().literal( val );
    }

    public static Literal literal( Character val ) {
        return getFactory().literal( val );
    }

    public static Literal literal( Boolean val ) {
        return getFactory().literal( val );
    }

    public static Argument zeroLiteral( String klass ) {
        return getFactory().zeroLiteral( klass );
    }

    public static Literal zeroLiteral( Class<?> klass ) {
        return getFactory().zeroLiteral( klass );
    }

    public static Accessor thisLiteral() {
        return getFactory().thisLiteral();
    }

    public static Literal nullLiteral() {
        return getFactory().nullLiteral();
    }

    public static ClassLiteral classLiteral( String klass ) {
        return getFactory().classLiteral( klass );
    }

    public static ClassLiteral classLiteral( Class<?> klass ) {
        return getFactory().classLiteral( klass );
    }

    public static Accessor classStatic( String klass ) {
        return getFactory().classStatic( klass );
    }

    public static Accessor classStatic( Class<?> klass ) {
        return getFactory().classStatic( klass );
    }

    public static ConstructorExpression newInstance( String klass ) {
        return getFactory().newInstance( klass );
    }

    public static ConstructorExpression newInstance( Class<?> klass ) {
        return getFactory().newInstance( klass );
    }

    public static ArrayConstructorExpression newArray( String klass ) {
        return getFactory().newArray( klass );
    }

    public static ArrayConstructorExpression newArray( Class<?> klass ) {
        return getFactory().newArray( klass );
    }

    public static ArrayInit vec() {
        return getFactory().vec();
    }

    public static Variable var( String variable ) {
        return getFactory().var( variable );
    }

    public static OperatorExpression operator( Op operator ) {
        return getFactory().operator( operator );
    }

    public static UnaryExpression operator( PrefixOp operator, Argument arg ) {
        return getFactory().operator( operator, arg );
    }

    public static TernaryExpression ternary() {
        return getFactory().ternary();
    }

    public static Argument cast( String klass, Expression expression ) {
        return getFactory().cast( klass, expression );
    }

    public static Argument cast( Class klass, Expression expression ) {
        return getFactory().cast( klass, expression );
    }

    public static ParenExpression paren( Expression inner ) {
        return getFactory().paren( inner );
    }

    public static UnaryExpression not( Argument inner ) {
        return getFactory().not( inner );
    }

    public static AssignExpression assign( Assignment operator ) {
        return getFactory().assign( operator );
    }

    public static DeclareExpression declare( String klass, String name ) {
        return getFactory().declare( klass, name );
    }

    public static DeclareExpression declare( Class klass, String name ) {
        return getFactory().declare( klass, name );
    }

    public static MethodExpression invoke( String method ) {
        return getFactory().invoke( method );
    }

    public static Field field( String field ) {
        return getFactory().field( field );
    }

    public static Getter getter( String field, String klass ) {
        return getFactory().getter( field, klass );
    }

    public static Getter getter( String field, Class klass ) {
        return getFactory().getter( field, klass );
    }

    public static Super sup() {
        return getFactory().sup();
    }

    public static InstanceofExpression instanceOf( String klass, Expression expression ) {
        return getFactory().instanceOf( klass, expression );
    }

    public static InstanceofExpression instanceOf( Class klass, Expression expression ) {
        return getFactory().instanceOf( klass, expression );
    }

}
