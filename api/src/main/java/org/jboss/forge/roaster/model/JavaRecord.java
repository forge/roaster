package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a Record class (available as a preview feature in JDK 14). See {@link Roaster} for various options in
 * generating {@link JavaRecord} instances
 */
public interface JavaRecord<O extends JavaRecord<O>> extends JavaSource<O>
{
}
