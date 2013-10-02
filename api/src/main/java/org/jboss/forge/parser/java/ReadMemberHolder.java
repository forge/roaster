/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.ReadMember.Member;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadMemberHolder<O extends ReadJavaSource<O>>
{
   /**
    * Return a list of all class members (fields, methods, etc.)
    */
   public List<? extends ReadMember<O>> getMembers();

   public interface MemberHolder<O extends JavaSource<O>> extends ReadMemberHolder<O>
   {
      /**
       * Return a list of all class members (fields, methods, etc.)
       */
      public List<Member<O, ?>> getMembers();
   }
}
