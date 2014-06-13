package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface LiteralBuilder<O extends JavaSource<O>, T extends ExpressionSource<O>> {

    public Literal<O, T> literal( String val );

    public Literal<O, T> literal( Number val );

    public Literal<O, T> literal( Character val );

    public Literal<O, T> literal( Boolean val );

    public Literal<O, T> zeroLiteral( String klass );

    public Literal<O, T> zeroLiteral( Class<?> klass );

    public Accessor<O, T> thisLiteral();

    public Literal<O, T> nullLiteral();

    public ClassLiteral<O, T> classLiteral( String klass );

    public ClassLiteral<O, T> classLiteral( Class<?> klass );

}
