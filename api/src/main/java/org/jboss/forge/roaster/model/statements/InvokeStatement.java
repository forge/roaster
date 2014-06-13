/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.MethodInvokeExpression;
import org.jboss.forge.roaster.model.expressions.MethodWithArgumentsInvokeExpression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;

/**
 * Represents a method invocation expression statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface InvokeStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ExpressionStatement<O,P,InvokeStatement<O,P>>
{

   /**
    * Returns the name of the method being invoked
    * @return the name of the method
    */
   public String getMethodName();

   /**
    * Sets the name of the method to be invoked
    * @param method The name of the method
    * @return  this <code>InvokeStatement</code> itself
    */
   public InvokeStatement<O,P> setMethodName(String method);

   /**
    * The expression returning the object on which this <code>InvokeStatement</code> is invoked
    * @return The expression returning the object on which this <code>InvokeStatement</code> is invoked
    */
   public Accessor<O,MethodInvokeExpression<O,InvokeStatement<O,P>,?>,?> getInvocationTarget();

   /**
    * Sets the expression returning the object on which this <code>InvokeStatement</code> is invoked
    * @param target  The expression returning the object on which this <code>InvokeStatement</code> is invoked
    * @return  This <code>InvokeStatement</code> itself
    */
   public InvokeStatement<O,P> setInvocationTarget(Accessor<?,?,?> target);

   /**
    * Returns the current list of arguments
    * @return  An immutable list containing the arguments
    */
   public List<Argument<O,MethodWithArgumentsInvokeExpression<O,InvokeStatement<O,P>,?>,?>> getArguments();

   /**
    * Adds an argument to the method
    * @param argument  The argument to be added
    * @return  The <code>InvokeStatement</code> itself
    */
   public InvokeStatement<O,P> addArgument(Argument<?,?,?> argument);

}
