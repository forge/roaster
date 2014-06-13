package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.impl.statements.JdtStatementWrapper;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.statements.InvokeStatement;
import org.jboss.forge.roaster.model.statements.ReturnStatement;
import org.jboss.forge.roaster.model.statements.Statement;
import org.junit.Test;

import java.util.List;

import static org.jboss.forge.roaster.model.expressions.Expressions.classStatic;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newInvoke;
import static org.jboss.forge.roaster.model.statements.Statements.newReturn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModifyMethodBodyTest
{

   @Test
   public void testReturnArg() throws Exception
   {
      String target = "return x;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public String echo( String x )");

      method.setBody(newReturn().setReturn(var("x")));

      assertEquals(target, method.getBody().trim());

      BlockSource body = method.getBodyAsBlock();
      assertEquals(1, body.getStatements().size());
      assertTrue(body.getStatements().get(0) instanceof ReturnStatement);
   }

   @Test
   public void testModifySource()
   {
      String source = "public class Bean {\n" +
                      "public String echo(String x) {\n" +
                      "  return x;\n" +
                      "}\n" +
                      "}";

      JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, source);
      List<MethodSource<JavaClassSource>> methods = javaClassSource.getMethods();
      assertEquals(1, methods.size());
      MethodSource<JavaClassSource> method = methods.get(0);

      BlockSource body = method.getBodyAsBlock();
      body.addStatement(0, newInvoke().setInvocationTarget(classStatic(System.class).field("out"))
            .setMethodName("println")
            .addArgument(var("x")));

      List<Statement> statements = body.getStatements();
      assertEquals(2, statements.size());
      assertEquals("x", ((Variable) ((ReturnStatement) statements.get(1)).getReturn()).getName());
      assertTrue(statements.get(0) instanceof InvokeStatement);

      assertTrue(((JdtStatementWrapper) body).getInternal().toString().contains("System.out.println"));
   }
}
