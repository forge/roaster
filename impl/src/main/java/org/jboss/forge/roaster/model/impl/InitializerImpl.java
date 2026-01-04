package org.jboss.forge.roaster.model.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.JDTOptions;

public class InitializerImpl<O extends JavaSource<O>> implements InitializerSource<O> 
{
    private final ModifierAccessor modifiers = new ModifierAccessor();
    
    private O parent = null;
    private AST ast = null;
    private CompilationUnit cu = null;
    private final Initializer initializer;

    private void init(final O parent) 
    {
        this.parent = parent;
        cu = (CompilationUnit) parent.getInternal();
        ast = cu.getAST();
    }

    public InitializerImpl(final O parent) 
    {
        init(parent);
        initializer = ast.newInitializer();
    }

    public InitializerImpl(final O parent, final Object internal) 
    {
        init(parent);
        initializer = (Initializer) internal;
    }

    public InitializerImpl(final O parent, final String initializer) 
    {
        init(parent);
        String stub = "public class Stub { " + initializer + " }";
        JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
        Initializer newInitializer = (Initializer) temp.getInitializers().get(0).getInternal();
        this.initializer = (Initializer) ASTNode.copySubtree(cu.getAST(), newInitializer);
    }

    @Override
    public String getBody() 
    {
        Block body = initializer.getBody();
        if (body != null) 
        {
            StringBuilder result = new StringBuilder();
            List<Statement> statements = (List<Statement>) body.getStructuralProperty(Block.STATEMENTS_PROPERTY);
            for (Statement statement : statements) {
                result.append(statement).append(" ");
            }
            return result.toString().trim();
        }
        return null;
    }

    @Override
    public InitializerSource<O> setBody(String body) 
    {
        if (body == null) {
            initializer.setBody(null);
            return this;
        }
        List<Problem> problems = Roaster.validateSnippet(body);
        if (problems.size() > 0) {
            throw new ParserException(problems);
        }
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setSource(body.toCharArray());
        parser.setCompilerOptions(JDTOptions.getJDTOptions());
        parser.setKind(ASTParser.K_STATEMENTS);
        Block block = (Block) parser.createAST(null);
        block = (Block) ASTNode.copySubtree(initializer.getAST(), block);
        initializer.setBody(block);
        return this;
    }

    @Override
    public boolean isStatic()
    {
        return modifiers.hasModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
    }
    
    @Override
    public InitializerSource<O> setStatic(boolean statc) 
    {
        if (statc)
           modifiers.addModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
        else
           modifiers.removeModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
        return this;
    }

    @Override
    public Object getInternal() 
    {
        return initializer;
    }

    @Override
    public O getOrigin() 
    {
        return parent;
    }

    @Override
    public boolean hasJavaDoc() 
    {
        return initializer.getJavadoc() != null;
    }

    @Override
    public InitializerSource<O> removeJavaDoc() 
    {
        initializer.setJavadoc(null);
        return this;
    }

    @Override
    public JavaDocSource<InitializerSource<O>> getJavaDoc() 
    {
        Javadoc javadoc = initializer.getJavadoc();
        if (javadoc == null) 
        {
            javadoc = initializer.getAST().newJavadoc();
            initializer.setJavadoc(javadoc);
        }
        return new JavaDocImpl<>(this, javadoc);
    }

    @Override
    public int getStartPosition() 
    {
        return initializer.getStartPosition();
    }

    @Override
    public int getEndPosition() 
    {
        int startPosition = getStartPosition();
        return (startPosition == -1) ? -1 : startPosition + initializer.getLength();
    }

    @Override
    public int getLineNumber() 
    {
        return cu.getLineNumber(getStartPosition());
    }

    @Override
    public int getColumnNumber() 
    {
        return cu.getColumnNumber(getStartPosition());
    }

}
