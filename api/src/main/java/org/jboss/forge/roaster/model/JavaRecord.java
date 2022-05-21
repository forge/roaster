package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Roaster;

import java.util.List;

/**
 * Represents a Record class (available as a preview feature in JDK 14). See {@link Roaster} for various options in
 * generating {@link JavaRecord} instances
 */
public interface JavaRecord<O extends JavaRecord<O>> extends
         JavaType<O>,
         MethodHolder<O>
{
   /**
    * @return the list of {@link JavaRecordComponent} for this record
    */
   List<JavaRecordComponent> getRecordComponents();
}
