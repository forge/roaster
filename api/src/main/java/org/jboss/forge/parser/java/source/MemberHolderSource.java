/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.MemberHolder;

/**
 * Represents a {@link JavaSource} that may declare fields or methods.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MemberHolderSource<O extends JavaSource<O>> extends MemberHolder<O>
{
   /**
    * Return a list of all class members (fields, methods, etc.)
    */
   @Override
   public List<MemberSource<O, ?>> getMembers();
}