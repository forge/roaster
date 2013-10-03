package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.FieldHolder;

/**
 * Represents a {@link JavaSource} that may contain field definitions.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface FieldHolderSource<O extends JavaSource<O>> extends FieldHolder<O>, MemberHolderSource<O>
{

   /**
    * Add a new Java {@link Field} to this {@link O} instance. This field will be a stub until further modified.
    */
   public FieldSource<O> addField();

   /**
    * Add a new {@link Field} declaration to this {@link O} instance, using the given {@link String} as the
    * declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Field f = javaClass.addField("private String newField;");</code>
    */
   public FieldSource<O> addField(final String declaration);

   /**
    * Remove the given {@link Field} from this {@link O} instance, if it exists; otherwise, do nothing.
    */
   public O removeField(final Field<O> field);
   /**
    * Get the {@link Field} with the given name and return it, otherwise, return null.
    */
   @Override
   public FieldSource<O> getField(String name);
   
   /**
    * Get a list of all {@link Field}s declared by this {@link O}, or return an empty list if no {@link Field}s
    * are declared.
    */
   @Override
   public List<FieldSource<O>> getFields();
}