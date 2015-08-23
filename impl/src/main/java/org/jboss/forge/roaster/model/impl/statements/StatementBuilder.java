/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.MethodInvokeExpression;
import org.jboss.forge.roaster.model.impl.BlockImpl;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.DeclareStatement;
import org.jboss.forge.roaster.model.statements.SuperStatement;
import org.jboss.forge.roaster.model.statements.ThisStatement;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;

import static org.eclipse.jdt.core.dom.ASTNode.ASSERT_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.BREAK_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONTINUE_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.LABELED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.RETURN_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SWITCH_CASE;
import static org.eclipse.jdt.core.dom.ASTNode.SWITCH_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.SYNCHRONIZED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.THROW_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TRY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.WHILE_STATEMENT;
import static org.jboss.forge.roaster.model.expressions.Expressions.declare;
import static org.jboss.forge.roaster.model.impl.expressions.ExpressionBuilder.asRoasterExpression;
import static org.jboss.forge.roaster.model.statements.Statements.newAssert;
import static org.jboss.forge.roaster.model.statements.Statements.newBlock;
import static org.jboss.forge.roaster.model.statements.Statements.newBreak;
import static org.jboss.forge.roaster.model.statements.Statements.newCase;
import static org.jboss.forge.roaster.model.statements.Statements.newContinue;
import static org.jboss.forge.roaster.model.statements.Statements.newDeclare;
import static org.jboss.forge.roaster.model.statements.Statements.newDoWhile;
import static org.jboss.forge.roaster.model.statements.Statements.newFor;
import static org.jboss.forge.roaster.model.statements.Statements.newForEach;
import static org.jboss.forge.roaster.model.statements.Statements.newIf;
import static org.jboss.forge.roaster.model.statements.Statements.newReturn;
import static org.jboss.forge.roaster.model.statements.Statements.newSuper;
import static org.jboss.forge.roaster.model.statements.Statements.newSwitch;
import static org.jboss.forge.roaster.model.statements.Statements.newSynchronized;
import static org.jboss.forge.roaster.model.statements.Statements.newThis;
import static org.jboss.forge.roaster.model.statements.Statements.newThrow;
import static org.jboss.forge.roaster.model.statements.Statements.newTryCatch;
import static org.jboss.forge.roaster.model.statements.Statements.newWhile;

public class StatementBuilder
{

   public static org.jboss.forge.roaster.model.statements.StatementSource<?,?,?> asRoasterStatement(Statement jdtStatement)
   {
      org.jboss.forge.roaster.model.statements.StatementSource roasterStmt = null;
      switch (jdtStatement.getNodeType())
      {
         case RETURN_STATEMENT:
            ReturnStatement returnStatement = (ReturnStatement) jdtStatement;
            roasterStmt = newReturn()
                  .setReturn(asRoasterExpression(returnStatement.getExpression()));
            break;
         case ASSERT_STATEMENT:
            AssertStatement assertStatement = (AssertStatement) jdtStatement;
            roasterStmt = newAssert()
                  .setAssertion(asRoasterExpression(assertStatement.getExpression()))
                  .setMessage(asRoasterExpression(assertStatement.getMessage()));
            break;
         case BLOCK:
            Block block = (Block) jdtStatement;
            roasterStmt = newBlock();
            ((BlockImpl) roasterStmt).setInternal(block);
            for (Object o : block.statements())
            {
               ((BlockStatement) roasterStmt).addStatement(StatementBuilder.asRoasterStatement((Statement) o));
            }
            break;
         case VARIABLE_DECLARATION_STATEMENT:
            VariableDeclarationStatement vds = (VariableDeclarationStatement) jdtStatement;
            DeclareStatement declareStatement = newDeclare();
            for (Object var : vds.fragments())
            {
               VariableDeclarationFragment vdf = (VariableDeclarationFragment) var;
               if (vdf.getInitializer() != null)
               {
                  declareStatement.setVariable(vds.getType().toString(), vdf.getName().toString(), asRoasterExpression(vdf.getInitializer()));
               } else
               {
                  declareStatement.setVariable(vds.getType().toString(), vdf.getName().toString());
               }
            }
            roasterStmt = declareStatement;
            break;
         case EMPTY_STATEMENT:
            roasterStmt = null;
            break;
         case EXPRESSION_STATEMENT:
            ExpressionSource expr = asRoasterExpression(((ExpressionStatement) jdtStatement).getExpression());
            if (expr instanceof MethodInvokeExpression)
            {
               roasterStmt = new InvokeStatementImpl((MethodInvokeExpression) expr);
            } else if (expr instanceof AssignExpression)
            {
               roasterStmt = new AssignStatementImpl((AssignExpression) expr);
            } else
            {
               roasterStmt = new ExpressionStatementImpl(expr);
            }
            break;
         case CONSTRUCTOR_INVOCATION:
            ThisStatement call = newThis();
            ConstructorInvocation civ = (ConstructorInvocation) jdtStatement;
            for (Object o : civ.arguments())
            {
               call.addArgument((Argument<?,?,?>) asRoasterExpression((Expression) o));
            }
            roasterStmt = call;
            break;
         case SUPER_CONSTRUCTOR_INVOCATION:
            SuperStatement supCall = newSuper();
            SuperConstructorInvocation sup = (SuperConstructorInvocation) jdtStatement;
            for (Object o : sup.arguments())
            {
               supCall.addArgument((Argument<?,?,?>) asRoasterExpression((Expression) o));
            }
            roasterStmt = supCall;
            break;
         case WHILE_STATEMENT:
            WhileStatement whil = (WhileStatement) jdtStatement;
            roasterStmt = newWhile()
                  .setCondition(asRoasterExpression(whil.getExpression()))
                  .setBody(asRoasterStatement(whil.getBody()));

            break;
         case DO_STATEMENT:
            DoStatement doWhil = (DoStatement) jdtStatement;
            roasterStmt = newDoWhile()
                  .setCondition(asRoasterExpression(doWhil.getExpression()))
                  .setBody(asRoasterStatement(doWhil.getBody()));

            break;
         case BREAK_STATEMENT:
            BreakStatement brik = (BreakStatement) jdtStatement;
            roasterStmt = brik.getLabel() != null ? newBreak().setBreakToLabel(brik.getLabel().toString()) : newBreak();
            break;
         case CONTINUE_STATEMENT:
            ContinueStatement cont = (ContinueStatement) jdtStatement;
            roasterStmt = newContinue().setContinueToLabel(cont.getLabel().toString());
            break;
         case LABELED_STATEMENT:
            LabeledStatement lbl = (LabeledStatement) jdtStatement;
            roasterStmt = asRoasterStatement(lbl.getBody());
            roasterStmt.setLabel(lbl.getLabel().toString());
            jdtStatement = ((LabeledStatement) jdtStatement).getBody();
            break;
         case ENHANCED_FOR_STATEMENT:
            EnhancedForStatement foreach = (EnhancedForStatement) jdtStatement;
            roasterStmt = newForEach().setIterator(foreach.getParameter().getType().toString(), foreach.getParameter().getName().toString())
                  .setSource(asRoasterExpression(foreach.getExpression()))
                  .setBody(asRoasterStatement(foreach.getBody()));
            break;
         case TRY_STATEMENT:
            TryStatement trial = (TryStatement) jdtStatement;
            TryCatchStatement tryCatchStatement = newTryCatch()
                  .setBody(asRoasterStatement(trial.getBody()))
                  .setFinally(asRoasterStatement(trial.getFinally()));
            for (Object o : trial.catchClauses())
            {
               CatchClause c = (CatchClause) o;
               tryCatchStatement.addCatch(declare(c.getException().getType().toString(), c.getException().getName().toString()),
                                          asRoasterStatement(c.getBody()));
            }
            roasterStmt = tryCatchStatement;
            break;
         case THROW_STATEMENT:
            ThrowStatement trow = (ThrowStatement) jdtStatement;
            roasterStmt = newThrow().setThrowable(asRoasterExpression(trow.getExpression()));
            break;
         case IF_STATEMENT:
            IfStatement iff = (IfStatement) jdtStatement;
            org.jboss.forge.roaster.model.statements.IfStatement rstIf = newIf().setCondition(asRoasterExpression(iff.getExpression()));
            if (iff.getThenStatement() != null)
            {
               rstIf.setThen(asRoasterStatement(iff.getThenStatement()));
            }
            if (iff.getElseStatement() != null)
            {
               rstIf.setElse(asRoasterStatement(iff.getElseStatement()));
            }
            roasterStmt = rstIf;
            break;
         case FOR_STATEMENT:
            ForStatement forst = (ForStatement) jdtStatement;
            org.jboss.forge.roaster.model.statements.ForStatement rstFor = newFor();
            if (forst.getExpression() != null)
            {
               rstFor.setCondition(asRoasterExpression(forst.getExpression()));
            }
            if (forst.getBody() != null)
            {
               rstFor.setBody(asRoasterStatement(forst.getBody()));
            }
            for (Object init : forst.initializers())
            {
               rstFor.setDeclaration((DeclareExpression<?,?>) asRoasterExpression((Expression) init));
            }
            for (Object updt : forst.updaters())
            {
               rstFor.addUpdate(asRoasterExpression((Expression) updt));
            }
            roasterStmt = rstFor;
            break;
         case SYNCHRONIZED_STATEMENT:
            SynchronizedStatement synch = (SynchronizedStatement) jdtStatement;
            roasterStmt = newSynchronized().setSynchOn(asRoasterExpression(synch.getExpression()))
                  .setBody(asRoasterStatement(synch.getBody()));
            break;
         case SWITCH_STATEMENT:
            SwitchStatement swicc = (SwitchStatement) jdtStatement;
            org.jboss.forge.roaster.model.statements.SwitchStatement rstSwitch = newSwitch().setSwitch(asRoasterExpression(swicc.getExpression()));
            for (Object o : swicc.statements())
            {
               rstSwitch.addStatement(asRoasterStatement((Statement) o));
            }
            roasterStmt = rstSwitch;
            break;
         case SWITCH_CASE:
            SwitchCase cas = (SwitchCase) jdtStatement;
            if (!cas.isDefault())
            {
               roasterStmt = newCase().setCaseExpression(asRoasterExpression(cas.getExpression()));
            } else
            {
               roasterStmt = newCase();
            }
            break;
         default:
            throw new UnsupportedOperationException(" Unknown statement type : " + jdtStatement.getNodeType() + " >> " + jdtStatement.toString());
      }
      if (roasterStmt != null)
      {
         ((org.jboss.forge.roaster.model.ASTNode) roasterStmt).setInternal(jdtStatement);
      }
      return roasterStmt;
   }


}


