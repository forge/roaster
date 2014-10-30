/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaDoc;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.JavaType;

/**
 * Represents a {@link JavaDoc} entry of a {@link JavaType}.
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaDocSource<O> extends JavaDoc<O>
{
   /**
    * Sets the whole {@link JavaDoc} text, including tags
    */
   JavaDocSource<O> setFullText(String text);

   /**
    * Sets the text for this {@link JavaDoc} (without any tags)
    */
   JavaDocSource<O> setText(String text);

   /**
    * Add a tag value for this {@link JavaDocSource}
    */
   JavaDocSource<O> addTagValue(String tagName, String tagValue);

   /**
    * Add a tag value given an existing {@link JavaDocTag}
    */
   JavaDocSource<O> addTagValue(JavaDocTag tag);

   /**
    * Removes the given tagName from this {@link JavaDoc}
    */
   JavaDocSource<O> removeTags(String tagName);

   /**
    * Removes the given {@link JavaDocTag}
    */
   JavaDocSource<O> removeTag(JavaDocTag tag);

   /**
    * Remove all tags
    */
   JavaDocSource<O> removeAllTags();

}
