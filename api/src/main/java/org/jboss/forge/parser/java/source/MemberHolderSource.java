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
   public List<MemberSource<O, ?>> getMembers();
}