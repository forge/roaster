package org.jboss.forge.roaster.model.expressions;


public enum Assignment {

    ASSIGN( "=" ),
    DIVIDE_ASSIGN( "/=" ),
    PLUS_ASSIGN( "+=" ),
    MINUS_ASSIGN( "-=" ),
    TIMES_ASSIGN( "*=" ),
    REMAINDER_ASSIGN( "%=" ),

    LEFT_SHIFT_ASSIGN( "<<=" ),
    RIGHT_SHIFT_ASSIGN( ">>=" ),
    RIGHT_SHIFT_UNSIGNED_ASSIGN( ">>>=" ),
    BITWISE_XOR_ASSIGN( "^=" ),
    BITWISE_AND_ASSIGN( "&=" ),
    BITWISE_OR_ASSIGN( "|=" );

    private String op;

    Assignment( String o ) {
        op = o;
    }

    public String getOp() {
        return op;
    }
}
