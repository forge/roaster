package org.jboss.forge.roaster.model.expressions;


public enum PrefixOp {

    INC( "++" ),
    DEC( "--" ),
    PLUS( "+" ),
    MINUS( "-" ),
    NOT( "!" ),
    NEG( "~" );

    private String op;

    PrefixOp( String o ) {
        op = o;
    }

    public String getOp() {
        return op;
    }
}
