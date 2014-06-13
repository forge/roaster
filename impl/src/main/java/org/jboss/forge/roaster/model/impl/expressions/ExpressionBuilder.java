/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.Expressions;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.MethodInvokeExpression;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.expressions.OrdinalArgument;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.impl.JDTHelper;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BOOLEAN_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CAST_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CHARACTER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CLASS_INSTANCE_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONDITIONAL_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.INSTANCEOF_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.POSTFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PREFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.QUALIFIED_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.SIMPLE_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.STRING_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.THIS_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_EXPRESSION;
import static org.jboss.forge.roaster.model.expressions.Expressions.assign;
import static org.jboss.forge.roaster.model.expressions.Expressions.cast;
import static org.jboss.forge.roaster.model.expressions.Expressions.declare;
import static org.jboss.forge.roaster.model.expressions.Expressions.getter;
import static org.jboss.forge.roaster.model.expressions.Expressions.instanceOf;
import static org.jboss.forge.roaster.model.expressions.Expressions.invoke;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.newArray;
import static org.jboss.forge.roaster.model.expressions.Expressions.nullLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.paren;
import static org.jboss.forge.roaster.model.expressions.Expressions.setter;
import static org.jboss.forge.roaster.model.expressions.Expressions.sup;
import static org.jboss.forge.roaster.model.expressions.Expressions.ternary;
import static org.jboss.forge.roaster.model.expressions.Expressions.thisLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.expressions.Expressions.vec;

public class ExpressionBuilder
{

   public static org.jboss.forge.roaster.model.expressions.ExpressionSource<?,?,?> asRoasterExpression(org.eclipse.jdt.core.dom.Expression jdtExpression)
   {
      org.jboss.forge.roaster.model.expressions.ExpressionSource roasterExpr;
      switch (jdtExpression.getNodeType())
      {
         //TODO FIXME : names are not necessarily vars, but it may not be possible to determine at this point
         case SIMPLE_NAME:
            roasterExpr = var(((SimpleName) jdtExpression).getIdentifier());
            break;
         case QUALIFIED_NAME:
            roasterExpr = var(((QualifiedName) jdtExpression).getFullyQualifiedName());
            break;
         case STRING_LITERAL:
            roasterExpr = literal(((StringLiteral) jdtExpression).getLiteralValue());
            break;
         case BOOLEAN_LITERAL:
            roasterExpr = literal(((BooleanLiteral) jdtExpression).booleanValue());
            break;
         case CHARACTER_LITERAL:
            roasterExpr = literal(((CharacterLiteral) jdtExpression).charValue());
            break;
         case NULL_LITERAL:
            roasterExpr = nullLiteral();
            break;
         case NUMBER_LITERAL:
            try
            {
               roasterExpr = literal(NumberFormat.getInstance().parse(
                     ((NumberLiteral) jdtExpression).getToken()));
            } catch (ParseException e)
            {
               // we know it's a valid number, so this should never happen
               e.printStackTrace();
               roasterExpr = null;
            }
            break;
         case CONDITIONAL_EXPRESSION:
            ConditionalExpression cond = (ConditionalExpression) jdtExpression;
            roasterExpr = ternary().setCondition(asRoasterExpression(cond.getExpression()))
                  .setThenExpression(asRoasterExpression(cond.getThenExpression()))
                  .setElseExpression(asRoasterExpression(cond.getElseExpression()));

            break;
         case CAST_EXPRESSION:
            CastExpression cast = (CastExpression) jdtExpression;
            roasterExpr = cast(cast.getType().toString(), asRoasterExpression(cast.getExpression()));
            break;
         case INFIX_EXPRESSION:
            InfixExpression infix = (InfixExpression) jdtExpression;
            roasterExpr = operator(Op.build(infix.getOperator().toString()))
                  .addArgument((Argument<?,?,?>) asRoasterExpression(infix.getLeftOperand()))
                  .addArgument((Argument<?,?,?>) asRoasterExpression(infix.getRightOperand()));
            for (Object o : infix.extendedOperands())
            {
               ((OperatorExpression) roasterExpr).addArgument((Argument<?,?,?>) asRoasterExpression((Expression) o));
            }
            break;
         case FIELD_ACCESS:
            FieldAccess fax = (FieldAccess) jdtExpression;
            Accessor axx = (Accessor) asRoasterExpression(fax.getExpression());
            roasterExpr = axx.field(fax.getName().getIdentifier());
            break;
         case INSTANCEOF_EXPRESSION:
            InstanceofExpression iof = (InstanceofExpression) jdtExpression;
            roasterExpr = instanceOf(iof.getRightOperand().toString(), asRoasterExpression(iof.getLeftOperand()));
            break;
         case THIS_EXPRESSION:
            roasterExpr = thisLiteral();
            break;
         case PREFIX_EXPRESSION:
            PrefixExpression pref = (PrefixExpression) jdtExpression;
            roasterExpr = operator(PrefixOp.build(pref.getOperator().toString())).setExpression( (Argument) asRoasterExpression(pref.getOperand()));
            break;
         case POSTFIX_EXPRESSION:
            PostfixExpression post = (PostfixExpression) jdtExpression;
            roasterExpr = asRoasterExpression(post.getOperand());
            if (post.getOperator() == PostfixExpression.Operator.INCREMENT)
            {
               roasterExpr = ((OrdinalArgument) roasterExpr).inc();
            } else
            {
               roasterExpr = ((OrdinalArgument) roasterExpr).dec();
            }
            break;
         case PARENTHESIZED_EXPRESSION:
            ParenthesizedExpression par = (ParenthesizedExpression) jdtExpression;
            roasterExpr = paren(asRoasterExpression(par.getExpression()));
            break;
         case CLASS_INSTANCE_CREATION:
            ClassInstanceCreation cic = (ClassInstanceCreation) jdtExpression;
            ConstructorExpression constr = Expressions.construct(cic.getType().toString());
            for (Object arg : cic.arguments())
            {
               constr.addArgument((Argument<?,?,?>) asRoasterExpression((Expression) arg));
            }
            roasterExpr = constr;
            break;
         case METHOD_INVOCATION:
            MethodInvocation invocation = (MethodInvocation) jdtExpression;
            String name = invocation.getName().toString();
            if (JDTHelper.isGetter(name, invocation.arguments()))
            {
               roasterExpr = getter(JDTHelper.fieldForGetter(name), name.startsWith("is") ? boolean.class : Object.class);
            } else if (JDTHelper.isSetter(name, invocation.arguments()))
            {
               roasterExpr = setter(JDTHelper.fieldForSetter(name), Object.class, asRoasterExpression((Expression) invocation.arguments().get(0)));
            } else
            {
               roasterExpr = invoke(name);
               for (Object arg : invocation.arguments())
               {
                  ((MethodCallExpression) roasterExpr).addArgument((Argument<?,?,?>) asRoasterExpression((Expression) arg));
               }
            }
            if (invocation.getExpression() != null)
            {
               ((MethodInvokeExpression) roasterExpr).setInvocationTarget(asRoasterExpression(invocation.getExpression()));
            }
            break;
         case ARRAY_ACCESS:
            ArrayAccess arrAxx = (ArrayAccess) jdtExpression;
            roasterExpr = ((Accessor) asRoasterExpression(arrAxx.getArray())).itemAt(asRoasterExpression(arrAxx.getIndex()));
            break;
         case ARRAY_CREATION:
            ArrayCreation arrC = (ArrayCreation) jdtExpression;
            ArrayConstructorExpression newArr = newArray(((ArrayType) arrC.getType()).getElementType().toString());
            for (Object o : arrC.dimensions())
            {
               newArr.addDimension(asRoasterExpression((Expression) o));
            }
            roasterExpr = newArr;
            if (arrC.getInitializer() != null)
            {
               newArr.init((ArrayInit<?,?>) asRoasterExpression(arrC.getInitializer()));
            }
            break;
         case ARRAY_INITIALIZER:
            ArrayInitializer arrInit = (ArrayInitializer) jdtExpression;
            ArrayInit arrI = vec();
            for (Object xp : arrInit.expressions())
            {
               arrI.addElement(asRoasterExpression((Expression) xp));
            }
            roasterExpr = arrI;
            break;
         case ASSIGNMENT:
            Assignment jass = (Assignment) jdtExpression;
            roasterExpr = assign(org.jboss.forge.roaster.model.expressions.Assignment.build(jass.getOperator().toString()))
                  .setLeft((Accessor<?,?,?>) asRoasterExpression(jass.getLeftHandSide()))
                  .setRight(asRoasterExpression(jass.getRightHandSide()));
            break;
         case SUPER_FIELD_ACCESS:
            SuperFieldAccess supf = (SuperFieldAccess) jdtExpression;
            roasterExpr = sup().field(supf.getName().toString());
            break;
         case SUPER_METHOD_INVOCATION:
            SuperMethodInvocation supm = (SuperMethodInvocation) jdtExpression;
            String supmName = supm.getName().toString();
            if (JDTHelper.isGetter(supmName, supm.arguments()))
            {
               roasterExpr = sup().getter(JDTHelper.fieldForGetter(supmName), supmName.startsWith("is") ? boolean.class : Object.class);
            } else
            {
               MethodCallExpression mce = sup().invoke(supm.getName().toString());
               for (Object o : supm.arguments())
               {
                  mce.addArgument((Argument<?,?,?>) asRoasterExpression((Expression) o));
               }
               roasterExpr = mce;
            }
            break;
         case VARIABLE_DECLARATION_EXPRESSION:
            VariableDeclarationExpression dek = (VariableDeclarationExpression) jdtExpression;
            String type = dek.getType().toString();
            DeclareExpression rstDecl = null;
            for (Object o : dek.fragments())
            {
               VariableDeclarationFragment frag = (VariableDeclarationFragment) o;
               if (rstDecl == null)
               {
                  if (frag.getInitializer() != null)
                  {
                     rstDecl = declare(type, frag.getName().toString(), asRoasterExpression(frag.getInitializer()));
                  } else
                  {
                     rstDecl = declare(type, frag.getName().toString());
                  }
               } else
               {
                  if (frag.getInitializer() != null)
                  {
                     rstDecl.addDeclaration(frag.getName().toString(), asRoasterExpression(frag.getInitializer()));
                  } else
                  {
                     rstDecl.addDeclaration(frag.getName().toString());
                  }
               }
            }
            roasterExpr = rstDecl;
            break;
         default:
            throw new UnsupportedOperationException("Unable to handle expression of type: " + jdtExpression.getNodeType() + " \n >> " + jdtExpression.toString());
      }
      if (roasterExpr != null)
      {
         ((org.jboss.forge.roaster.model.ASTNode) roasterExpr).setInternal(jdtExpression);
      }
      return roasterExpr;
   }

}
