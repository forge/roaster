/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.BooleanLiteral;
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.CharacterLiteral;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.NullLiteral;
import org.jboss.forge.roaster.model.expressions.NumberLiteral;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.expressions.PostFixExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.expressions.StringLiteral;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.expressions.ThisLiteral;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.impl.expressions.StringLiteralImpl;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.statements.AssertStatement;
import org.jboss.forge.roaster.model.statements.AssignmentStatement;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.BreakStatement;
import org.jboss.forge.roaster.model.statements.ContinueStatement;
import org.jboss.forge.roaster.model.statements.DeclareStatement;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.ExpressionStatement;
import org.jboss.forge.roaster.model.statements.ForEachStatement;
import org.jboss.forge.roaster.model.statements.ForStatement;
import org.jboss.forge.roaster.model.statements.IfStatement;
import org.jboss.forge.roaster.model.statements.InvokeStatement;
import org.jboss.forge.roaster.model.statements.ReturnStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.SuperStatement;
import org.jboss.forge.roaster.model.statements.SwitchCaseStatement;
import org.jboss.forge.roaster.model.statements.SwitchStatement;
import org.jboss.forge.roaster.model.statements.SynchStatement;
import org.jboss.forge.roaster.model.statements.ThisStatement;
import org.jboss.forge.roaster.model.statements.ThrowStatement;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;
import org.jboss.forge.roaster.model.statements.WhileStatement;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RebuildStatementTest
{

   @Test
   public void testRebuildReturnStatement()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return x; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertEquals("x", ((Variable) ((ReturnStatement) statements.get(0)).getReturn()).getName());
   }

   @Test
   public void testRebuildReturnStatementBoolean()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return true; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof BooleanLiteral);
   }

   @Test
   public void testRebuildReturnStatementWithCast()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return (my.own.Klass) foo; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof CastExpression);
      CastExpression cast = (CastExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("my.own.Klass", cast.getType());
      assertTrue(cast.getExpression() instanceof Variable);
   }

   @Test
   public void testRebuildReturnNull()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return null; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof NullLiteral);
   }

   @Test
   public void testRebuildReturnWithInstanceOf()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return x instanceof Test; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof InstanceofExpression);
      InstanceofExpression iof = (InstanceofExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("Test", iof.getTypeName());
      assertTrue(iof.getExpression() instanceof Variable);
   }

   @Test
   public void testRebuildReturnWithPrefix()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return ++x; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof UnaryExpression);
      UnaryExpression una = (UnaryExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(una.getExpression() instanceof Variable);
      assertEquals("++", una.getOperator());
   }

   @Test
   public void testRebuildReturnWithPostfix()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return x--; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof UnaryExpression);
      PostFixExpression una = (PostFixExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(una.getExpression() instanceof Variable);
      assertEquals("--", una.getOperator());
   }

   @Test
   public void testRebuildVariableDeclarations()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  int y = 1; " +
                      "  String z; " +
                      "  Object q; " +
                      "  my.pack.Foo foo; " +
                      "" +
                      "  return z; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      String[] names = new String[]{"y", "z", "q", "foo"};
      String[] types = new String[]{"int", "String", "Object", "my.pack.Foo"};

      assertEquals(5, statements.size());
      for (int j = 0; j < 4; j++)
      {
         assertTrue(statements.get(j) instanceof DeclareStatement);
         DeclareStatement decl = ((DeclareStatement) statements.get(j));
         assertEquals(names[j], decl.getVariableName());
         assertEquals(types[j], decl.getVariableType());
      }
   }

   @Test
   public void testRebuildSuperField()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "" +
                      "  return super.z; " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof Field);
      Field f = (Field) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("z", f.getFieldName());
      assertTrue(f.getInvocationTarget() instanceof Super);
   }

   @Test
   public void testRebuildSuperMethod()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "" +
                      "  return super.foo( 42 ); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof MethodCallExpression);
      MethodCallExpression m = (MethodCallExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("foo", m.getMethodName());
      assertTrue(m.getInvocationTarget() instanceof Super);
      assertEquals(1, m.getArguments().size());
   }

   @Test
   public void testRebuildSuperGetter()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "" +
                      "  return super.getFoo(); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof Getter);
      Getter m = (Getter) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("foo", m.getFieldName());
   }

   @Test
   public void testTheEmptyStatement()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " ; " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(0, statements.size());
   }

   @Test
   public void testRebuildWhileStatementWithContinue()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " while ( x > 0 ) { " +
                      "       x = x+1; " +
                      "       continue foo; " +
                      "  } " +
                      "  return x; " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(2, statements.size());
      assertTrue(statements.get(0) instanceof WhileStatement);
      WhileStatement whil = (WhileStatement) statements.get(0);
      assertTrue(whil.getCondition() instanceof OperatorExpression);
      assertEquals(2, whil.getBody().getStatements().size());
      assertTrue(whil.getBody().getStatements().get(0) instanceof AssignmentStatement);
      assertTrue(whil.getBody().getStatements().get(1) instanceof ContinueStatement);
   }

   @Test
   public void testRebuildDoWhileStatementWithBreak()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " do { " +
                      "       x = x+1; " +
                      "       break; " +
                      "  } while ( x > 0 ); " +
                      "  return x; " +
                      "}";
      List statements = parseSource(source);
      assertEquals(2, statements.size());
      assertTrue(statements.get(0) instanceof DoWhileStatement);
      DoWhileStatement whil = (DoWhileStatement) statements.get(0);
      assertTrue(whil.getCondition() instanceof OperatorExpression);
      assertEquals(2, whil.getBody().getStatements().size());
      assertTrue(whil.getBody().getStatements().get(0) instanceof AssignmentStatement);
      assertTrue(whil.getBody().getStatements().get(1) instanceof BreakStatement);
   }

   @Test
   public void testRebuildLabeledStatement()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " foo : do { " +
                      " } while ( true ); " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof DoWhileStatement);
      DoWhileStatement whil = (DoWhileStatement) statements.get(0);
      assertEquals("foo", whil.getLabel());
      assertTrue(whil.getCondition() instanceof BooleanLiteral);
      assertEquals(0, whil.getBody().getStatements().size());
   }

   @Test
   public void testRebuildEnhancedFor()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " for ( Integer x : numbers ) { " +
                      "   x++; " +
                      " } " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ForEachStatement);
      ForEachStatement foreach = (ForEachStatement) statements.get(0);
      assertTrue(foreach.getSource() instanceof Variable);
      assertEquals("x", foreach.getIteratorName());
      assertEquals(Integer.class.getSimpleName(), foreach.getIteratorType());
      assertEquals(1, foreach.getBody().getStatements().size());
      assertTrue(foreach.getBody().getStatements().get(0) instanceof ExpressionStatement);
      assertTrue(((ExpressionStatement) foreach.getBody().getStatements().get(0)).getExpr() instanceof PostFixExpression);
   }

   @Test
   public void testRebuildAssert2()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " assert x > 0 : 42; " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof AssertStatement);
      AssertStatement assrt = (AssertStatement) statements.get(0);
      assertTrue(assrt.getAssertion() instanceof OperatorExpression);
      assertTrue(assrt.getMessage() instanceof NumberLiteral);
   }

   @Test
   public void testRebuildThrow()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " throw new MysteryException(); " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ThrowStatement);
      ThrowStatement trow = (ThrowStatement) statements.get(0);
      assertTrue(trow.getThrowable() instanceof ConstructorExpression);
   }

   @Test
   public void testRebuildIfThenElse()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " if ( true ) { } else { } " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof IfStatement);
      IfStatement ifte = (IfStatement) statements.get(0);
      assertTrue(ifte.getCondition() instanceof BooleanLiteral);
      assertEquals(0, ifte.getThen().getStatements().size());
      assertEquals(0, ifte.getElse().getStatements().size());
   }

   @Test
   public void testRebuildIf()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " if ( x > 0 ) { return -1; } " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof IfStatement);
      IfStatement ifte = (IfStatement) statements.get(0);
      assertTrue(ifte.getCondition() instanceof OperatorExpression);
      assertEquals(1, ifte.getThen().getStatements().size());
      assertTrue(ifte.getThen().getStatements().get(0) instanceof ReturnStatement);
      assertNull(ifte.getElse());
   }

   @Test
   public void testRebuildFor()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      "  for ( int j = 0; j < 5; j++ ) { " +
                      "       x = 2*j; " +
                      "  }  " +
                      " } " +
                      "} ";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ForStatement);
      ForStatement forst = (ForStatement) statements.get(0);
      assertTrue(forst.getCondition() instanceof OperatorExpression);

      assertNotNull(forst.getDeclaration());
      assertTrue(forst.getDeclaration() instanceof DeclareExpression);

      assertEquals(1, forst.getUpdates().size());
      assertTrue(forst.getUpdates().get(0) instanceof PostFixExpression);
   }

   @Test
   public void testRebuildMultiVariableFor()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      "  for ( int j = 0, k = 5; j < 5; j++, k-- ) { " +
                      "  }  " +
                      " } " +
                      "} ";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ForStatement);
      ForStatement forst = (ForStatement) statements.get(0);
      assertTrue(forst.getCondition() instanceof OperatorExpression);

      assertEquals(2, forst.getDeclaration().getNumVariables());
      DeclareExpression decl = forst.getDeclaration();
      assertEquals(Arrays.asList("j", "k"), decl.getVariableNames());
      assertEquals("int", decl.getVariableType());
      assertTrue(decl.getInitExpressions().get("j") instanceof NumberLiteral);
      assertTrue(decl.getInitExpressions().get("k") instanceof NumberLiteral);

      assertEquals(2, forst.getUpdates().size());
      assertTrue(forst.getUpdates().get(0) instanceof PostFixExpression);

   }

   @Test
   public void testRebuildTryCatch()
   {
      String source = "public class Bean { " +
                      "public int echo(int x) { " +
                      " try { " +
                      "   int x = 1+1;" +
                      " } catch ( Exception1 e1 ) { " +
                      "   e1.printStackTrace(); " +
                      " } catch ( fully.qualified.Exception2 e2 ) { " +
                      "   System.out.println( e2.getMessage() ); " +
                      " } finally { " +
                      "    System.exit(-1); " +
                      " } " +
                      "} ";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof TryCatchStatement);
      TryCatchStatement trial = (TryCatchStatement) statements.get(0);

      assertEquals(1, trial.getBody().getStatements().size());
      assertTrue(trial.getBody().getStatements().get(0) instanceof DeclareStatement);

      assertEquals(1, trial.getFinally().getStatements().size());
      assertTrue(trial.getFinally().getStatements().get(0) instanceof InvokeStatement);
      InvokeStatement inv = (InvokeStatement) trial.getFinally().getStatements().get(0);
      assertEquals("exit", inv.getMethodName());
      assertEquals(1, inv.getArguments().size());

      assertEquals(2, trial.getCatches().size());
      Map<DeclareExpression,BlockSource> catches = trial.getCatches();
      for (DeclareExpression key : catches.keySet())
      {
         if ("e1".equals(key.getVariableName()))
         {
            assertEquals("Exception1", key.getVariableType());
            BlockSource b = catches.get(key);
            assertEquals(1, b.getStatements().size());
            assertTrue(b.getStatements().get(0) instanceof InvokeStatement);
         } else if ("e2".equals(key.getVariableName()))
         {
            assertEquals("fully.qualified.Exception2", key.getVariableType());
            BlockSource b = catches.get(key);
            assertEquals(1, b.getStatements().size());
            assertTrue(b.getStatements().get(0) instanceof InvokeStatement);
         } else
         {
            fail("Unrecognized catch declaration " + key);
         }
      }
   }

   @Test
   public void testMethodInvocationWithReturn()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " return callIt( 3, \"aaa\", 1+1 ); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof MethodCallExpression);
      MethodCallExpression call = (MethodCallExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertEquals("callIt", call.getMethodName());
      assertEquals(3, call.getArguments().size());
      assertTrue(call.getArguments().get(0) instanceof NumberLiteral);
      assertTrue(call.getArguments().get(1) instanceof StringLiteral);
      assertTrue(call.getArguments().get(2) instanceof OperatorExpression);
   }

   @Test
   public void testRebuildSynchronized()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " synchronized( x ) { " +
                      "     x++; " +
                      "  } " +
                      " } " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof SynchStatement);
      SynchStatement synch = (SynchStatement) statements.get(0);

      assertTrue(synch.getSynchOn() instanceof Variable);
      assertEquals(1, synch.getBody().getStatements().size());
   }

   @Test
   public void testRebuildSwitchCase()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " switch( x ) { " +
                      "     case 1 : return 0; " +
                      "     case 2 : y++;" +
                      "     case 3 : z++; break;" +
                      "     case 4 : " +
                      "     default : return -1; " +
                      "  } " +
                      " } " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof SwitchStatement);
      SwitchStatement swich = (SwitchStatement) statements.get(0);
      assertTrue(swich.getSwitch() instanceof Variable);

      assertEquals(5, swich.getCaseExpressions().size());
      for (int j = 0; j < 4; j++)
      {
         assertTrue(swich.getCaseExpressions().get(j) instanceof NumberLiteral);
      }
      assertNull(swich.getCaseExpressions().get(4));

      assertTrue(swich.getStatements().get(0) instanceof SwitchCaseStatement);
      assertTrue(swich.getStatements().get(2) instanceof SwitchCaseStatement);
      assertTrue(swich.getStatements().get(4) instanceof SwitchCaseStatement);
      assertTrue(swich.getStatements().get(7) instanceof SwitchCaseStatement);
      assertTrue(swich.getStatements().get(8) instanceof SwitchCaseStatement);

      assertTrue(swich.getStatements().get(1) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) swich.getStatements().get(1)).getReturn() instanceof NumberLiteral);
      assertTrue(swich.getStatements().get(3) instanceof ExpressionStatement);
      assertTrue(((ExpressionStatement) swich.getStatements().get(3)).getExpr() instanceof PostFixExpression);
      assertTrue(swich.getStatements().get(5) instanceof ExpressionStatement);
      assertTrue(((ExpressionStatement) swich.getStatements().get(5)).getExpr() instanceof PostFixExpression);
      assertTrue(swich.getStatements().get(6) instanceof BreakStatement);
      assertTrue(swich.getStatements().get(9) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) swich.getStatements().get(9)).getReturn() instanceof UnaryExpression);
   }

   @Test
   public void testMethodInvocations()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " callIt( 3, \"aaa\", 1+1 ); " +
                      " getFoo(); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(2, statements.size());
      assertTrue(statements.get(0) instanceof InvokeStatement);
      InvokeStatement inv = (InvokeStatement) statements.get(0);
      assertEquals("callIt", inv.getMethodName());
      assertEquals(3, inv.getArguments().size());
      assertTrue(statements.get(1) instanceof ExpressionStatement);
      assertTrue(((ExpressionStatement) statements.get(1)).getExpr() instanceof Getter);
   }

   @Test
   public void testMethodInvocationsChain()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " x.a().b(); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof InvokeStatement);
      InvokeStatement inv = (InvokeStatement) statements.get(0);
      assertEquals("b", inv.getMethodName());
      assertTrue(inv.getInvocationTarget() instanceof MethodCallExpression);
      MethodCallExpression call = (MethodCallExpression) inv.getInvocationTarget();
      assertTrue(call.getInvocationTarget() instanceof Variable);

   }

   @Test
   public void testMethodInvocationsChainGetSet()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " x.getA().setB( 0 ); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof InvokeStatement);
      InvokeStatement inv = (InvokeStatement) statements.get(0);
      assertEquals("setB", inv.getMethodName());
      assertTrue(inv.getArguments().get(0) instanceof NumberLiteral);
      assertTrue(inv.getInvocationTarget() instanceof Getter);
      Getter call = (Getter) inv.getInvocationTarget();
      assertTrue(call.getInvocationTarget() instanceof Variable);

      ExpressionSource inner = inv.getExpr();
      assertTrue(inner instanceof Setter);
      Setter set = (Setter) inner;
      assertTrue(set.getArguments().get(0) instanceof NumberLiteral);
      assertTrue(set.getValue() instanceof NumberLiteral);

   }

   @Test
   public void testParen()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      " return (2*1) + (x); " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      OperatorExpression op = (OperatorExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(op.getArguments().get(0) instanceof ParenExpression);
      assertTrue(op.getArguments().get(1) instanceof ParenExpression);
      assertTrue(((ParenExpression) op.getArguments().get(0)).getExpression() instanceof OperatorExpression);
   }

   @Test
   public void testRebuildVariableDeclarationsInline()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  int a = 3, b, c, d = 5; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof DeclareStatement);
      DeclareStatement dec = (DeclareStatement) statements.get(0);
      assertEquals(Arrays.asList("a", "b", "c", "d"), dec.getVariableNames());
      assertEquals("a", dec.getVariableName());
      assertTrue(dec.getInitExpression() instanceof NumberLiteral);
      assertNull(dec.getInitExpressions().get("b"));
      assertTrue(dec.getInitExpressions().get("d") instanceof NumberLiteral);
      assertEquals(3L, ((NumberLiteral) dec.getInitExpression()).getValue());
      assertEquals(5L, ((NumberLiteral) dec.getInitExpressions().get("d")).getValue());
   }

   @Test
   public void testComments()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  // test" +
                      "  /* test2 */  " +
                      "} " +
                      "}";
      List statements = parseSource(source);
      // TODO tests are not supported yet
      assertTrue(statements.isEmpty());
   }

   @Test
   public void testRebuildReturnStatementTernary()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return x > 0 ? 4 : 2; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof TernaryExpression);
      TernaryExpression tern = (TernaryExpression) ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(tern.getCondition() instanceof OperatorExpression);
      assertTrue(tern.getThenExpression() instanceof NumberLiteral);
      assertTrue(tern.getElseExpression() instanceof NumberLiteral);
   }

   @Test
   public void testRebuildReturnStatementChar()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return 'x'; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      assertTrue(((ReturnStatement) statements.get(0)).getReturn() instanceof CharacterLiteral);
      assertEquals((Character) 'x', ((CharacterLiteral) ((ReturnStatement) statements.get(0)).getReturn()).getValue());
   }

   @Test
   public void testRebuildReturnStatementExpr()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return 3+5*4; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource ret = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(ret instanceof OperatorExpression);
      OperatorExpression opxp = (OperatorExpression) ret;
      assertTrue(opxp.getArguments().get(0) instanceof NumberLiteral);
      assertEquals("+", opxp.getOperator());
      assertTrue(opxp.getArguments().get(1) instanceof OperatorExpression);
      OperatorExpression sub = (OperatorExpression) opxp.getArguments().get(1);
      assertEquals("*", sub.getOperator());
      assertEquals(4L, ((NumberLiteral) sub.getArguments().get(1)).getValue());
      assertEquals(5L, ((NumberLiteral) sub.getArguments().get(0)).getValue());
   }

   @Test
   public void testRebuildReturnStatementWithLiteralExpression()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return 4+x; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource ret = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(ret instanceof OperatorExpression);
      OperatorExpression opxp = (OperatorExpression) ret;
      assertTrue(opxp.getArguments().get(0) instanceof NumberLiteral);
      assertTrue(opxp.getArguments().get(1) instanceof Variable);
   }

   @Test
   public void testRebuildReturnStatementWithLiteralExpression2()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return 4+x+7; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource ret = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(ret instanceof OperatorExpression);
      OperatorExpression opxp = (OperatorExpression) ret;
      assertEquals(3, opxp.getArguments().size());
      assertTrue(opxp.getArguments().get(0) instanceof NumberLiteral);
      assertTrue(opxp.getArguments().get(1) instanceof Variable);
   }

   @Test
   public void testRebuildReturnStatementWithThis()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return this.x; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof Field);
      Field f = (Field) val;
      assertEquals("x", f.getFieldName());
      assertTrue(f.getInvocationTarget() instanceof ThisLiteral);
   }

   @Test
   public void testRebuildReturnStatementWithChain()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return this.a.b.c.d; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      Field f = null;
      String[] names = {"d", "c", "b", "a"};
      for (int j = 0; j < 4; j++)
      {
         assertTrue(val instanceof Field);
         f = (Field) val;
         assertEquals(names[j], f.getFieldName());
         val = f.getInvocationTarget();
      }
      assertTrue(f.getInvocationTarget() instanceof ThisLiteral);
   }

   @Test
   public void testRebuildReturnStatementWithConstructor()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return new Integer(42); " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ConstructorExpression);
      assertEquals(1, ((ConstructorExpression) val).getArguments().size());
   }

   @Test
   public void testRebuildConstrCall()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  new Integer(42); " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ExpressionStatement);
      ExpressionSource val = ((ExpressionStatement) statements.get(0)).getExpr();
      assertTrue(val instanceof ConstructorExpression);
      assertEquals(1, ((ConstructorExpression) val).getArguments().size());
   }

   @Test
   public void testRebuildConstrCallAsTarget()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  new Integer(42).toString(); " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ExpressionStatement);
      ExpressionSource val = ((ExpressionStatement) statements.get(0)).getExpr();
      assertTrue(val instanceof MethodCallExpression);
      assertEquals(0, ((MethodCallExpression) val).getArguments().size());
      assertTrue(((MethodCallExpression) val).getInvocationTarget() instanceof ConstructorExpression);
   }

   @Test
   public void testConstructorMutualCall()
   {
      String source = "public class Bean { " +
                      "  public Bean() { this(42); } " +
                      "  public Bean( int i ) {} " +
                      "}";
      JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, source);
      List<MethodSource<JavaClassSource>> methods = javaClassSource.getMethods();
      assertEquals(2, methods.size());
      MethodSource<JavaClassSource> method = methods.get(0);
      assertEquals(1, method.getBodyAsBlock().getStatements().size());
      StatementSource st = method.getBodyAsBlock().getStatements().get(0);
      assertTrue(st instanceof ThisStatement);
      assertEquals(1, ((ThisStatement) st).getArguments().size());
   }

   @Test
   public void testConstructorMutualCallSuper()
   {
      String source = "public class Bean { " +
                      "  public Bean() { super(42); } " +
                      "}";
      JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, source);
      List<MethodSource<JavaClassSource>> methods = javaClassSource.getMethods();
      assertEquals(1, methods.size());
      MethodSource<JavaClassSource> method = methods.get(0);
      assertEquals(1, method.getBodyAsBlock().getStatements().size());
      StatementSource st = method.getBodyAsBlock().getStatements().get(0);
      assertTrue(st instanceof SuperStatement);
      assertEquals(1, ((SuperStatement) st).getArguments().size());
   }

   @Test
   public void testRebuildReturnStatementWithGetter()
   {
      String source = "public class Bean { " +
                      "public String echo(String x) { " +
                      "  return getFoo(); " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof Getter);
      assertEquals("foo", ((Getter) val).getFieldName());
   }

   @Test
   public void testRebuildReturnKnownField()
   {
      String source = "public class Bean { " +
                      "  private String str; " +
                      "public String echo(String x) { " +
                      "  return this.str; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof Field);
   }

   @Test
   public void testRebuildArrayAccess()
   {
      String source = "public class Bean { " +
                      "public String echo(String[] x) { " +
                      "  return x[0]; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayIndexer);
      assertTrue(((ArrayIndexer) val).getIndex() instanceof NumberLiteral);
      assertTrue(((ArrayIndexer) val).getInvocationTarget() instanceof Variable);
   }

   @Test
   public void testRebuildArrayAccessMultiple()
   {
      String source = "public class Bean { " +
                      "public String echo(String[] x) { " +
                      "  return x[i][j]; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayIndexer);
      ArrayIndexer ix = (ArrayIndexer) val;
      assertTrue(ix.getIndex() instanceof Variable);
      assertEquals("j", ((Variable) ix.getIndex()).getName());
      assertTrue(ix.getInvocationTarget() instanceof ArrayIndexer);
      ArrayIndexer iy = (ArrayIndexer) ix.getInvocationTarget();
      assertTrue(iy.getInvocationTarget() instanceof Variable);
      assertTrue(iy.getIndex() instanceof Variable);
      assertEquals("i", ((Variable) iy.getIndex()).getName());
   }

   @Test
   public void testReturnArray()
   {
      String source = "public class Bean { " +
                      "public String[] echo() { " +
                      "  return new String[3]; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayConstructorExpression);
      assertEquals(1, ((ArrayConstructorExpression) val).getDimensions().size());
      assertTrue(((ArrayConstructorExpression) val).getDimensions().get(0) instanceof NumberLiteral);
   }

   @Test
   public void testReturnArrayMultiDim()
   {
      String source = "public class Bean { " +
                      "public String[] echo() { " +
                      "  return new String[3][j]; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayConstructorExpression);
      assertEquals(2, ((ArrayConstructorExpression) val).getDimensions().size());
      assertEquals(2, ((ArrayConstructorExpression) val).getDimension());
      assertTrue(((ArrayConstructorExpression) val).getDimensions().get(1) instanceof Variable);
   }

   @Test
   public void testReturnArrayWithInit()
   {
      String source = "public class Bean { " +
                      "public String[] echo() { " +
                      "  return new String[] { \"a\", \"b\" } ; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayConstructorExpression);
      assertEquals(0, ((ArrayConstructorExpression) val).getDimensions().size());
      assertEquals(1, ((ArrayConstructorExpression) val).getDimension());
      assertNotNull(((ArrayConstructorExpression) val).getInit());
      ExpressionSource expr = ((ArrayConstructorExpression) val).getInit();
      assertTrue(expr instanceof ArrayInit);
      ArrayInit init = (ArrayInit) expr;
      assertEquals(2, init.getElements().size());
      assertTrue(init.getElements().get(0) instanceof StringLiteral);
      assertTrue(init.getElements().get(1) instanceof StringLiteral);
   }

   @Test
   public void testAssignment()
   {
      String source = "public class Bean { " +
                      "public String[] echo() { " +
                      "  x = 2; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof AssignmentStatement);
      AssignmentStatement axx = (AssignmentStatement) statements.get(0);
      assertTrue(axx.getLeft() instanceof Variable);
      assertTrue(axx.getRight() instanceof NumberLiteral);
   }


   @Test
   public void testReturnArrayWithInitMultiDimension()
   {
      String source = "public class Bean { " +
                      "public String[] echo() { " +
                      "  return new String[][] { {\"a\", \"b\"}, {\"c\"} } ; " +
                      "} " +
                      "}";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof ReturnStatement);
      ExpressionSource val = ((ReturnStatement) statements.get(0)).getReturn();
      assertTrue(val instanceof ArrayConstructorExpression);
      assertEquals(0, ((ArrayConstructorExpression) val).getDimensions().size());
      assertEquals(2, ((ArrayConstructorExpression) val).getDimension());
      assertNotNull(((ArrayConstructorExpression) val).getInit());
      ExpressionSource expr = ((ArrayConstructorExpression) val).getInit();
      assertTrue(expr instanceof ArrayInit);
      ArrayInit init = (ArrayInit) expr;
      assertEquals(2, init.getElements().size());
      assertTrue(init.getElements().get(0) instanceof ArrayInit);
      assertTrue(init.getElements().get(1) instanceof ArrayInit);
      ArrayInit d1 = (ArrayInit) init.getElements().get(0);
      ArrayInit d2 = (ArrayInit) init.getElements().get(1);
      assertEquals(2, d1.size());
      assertEquals(1, d2.size());
   }

   @Test
   public void testRebuildAssertStatement()
   {
      String source = "public class Bean { " +
                      "public void foo() { " +
                      "  assert x : \"So be it\"; " +
                      "} " +
                      "} ";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof AssertStatement);
      AssertStatement as = (AssertStatement) statements.get(0);
      assertEquals("So be it", ((StringLiteralImpl) as.getMessage()).getValue());
      assertEquals("x", ((Variable) as.getAssertion()).getName());
   }

   @Test
   public void testRebuildBlockStatement()
   {
      String source = "public class Bean { " +
                      "public int foo() { " +
                      "  { return 0; } " +
                      "} " +
                      "} ";
      List statements = parseSource(source);

      assertEquals(1, statements.size());
      assertTrue(statements.get(0) instanceof BlockStatement);
      BlockStatement bs = (BlockStatement) statements.get(0);
      assertEquals(1, bs.getStatements().size());
      assertTrue(bs.getStatements().get(0) instanceof ReturnStatement);
      ReturnStatement rs = (ReturnStatement) bs.getStatements().get(0);
      ExpressionSource val = rs.getReturn();
      assertTrue(val instanceof NumberLiteral);
      assertEquals(0L, ((NumberLiteral) val).getValue());
   }

   private List parseSource(String source)
   {
      JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, source);
      List<MethodSource<JavaClassSource>> methods = javaClassSource.getMethods();
      assertEquals(1, methods.size());
      MethodSource<JavaClassSource> method = methods.get(0);

      return method.getBodyAsBlock().getStatements();
   }

}
