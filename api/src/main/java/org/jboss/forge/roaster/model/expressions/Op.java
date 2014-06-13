package org.jboss.forge.roaster.model.expressions;


public enum Op {

    TIMES( "*" ),
    DIVIDE( "/" ),
    REMAINDER( "%" ),
    PLUS( "+" ),
    MINUS( "-" ),
    LEFT_SHIFT( "<<" ),
    RIGHT_SHIFT_SIGNED( ">>" ),
    RIGHT_SHIFT_UNSIGNED( ">>>" ),
    LESS( "<" ),
    GREATER( ">" ),
    LESS_EQUALS( "<=" ),
    GREATER_EQUALS( ">=" ),
    EQUALS( "==" ),
    NOT_EQUALS( "!=" ),
    BITWISE_XOR( "^" ),
    BITWISE_AND( "&" ),
    BITWISE_OR( "|" ),
    OR( "||" ),
    AND( "&&" );

    private String op;

    Op( String o ) {
        op = o;
    }

    public String getOp() {
        return op;
    }
}
