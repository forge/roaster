/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;
import java.util.Set;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * A {@link JavaDoc} represents Javadoc-style doc comment
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaDoc<O> extends Internal, Origin<O>
{
   /**
    * Returns the text for this {@link JavaDoc}, excluding tags
    */
   String getText();

   /**
    * Returns the full text for this {@link JavaDoc}, including tags
    */
   String getFullText();

   /**
    * Return the tag names for this {@link JavaDoc}
    */
   Set<String> getTagNames();

   /**
    * Returns a list of {@link JavaDocTag} values for the given tag name (eg: @param) or an empty list if not found
    */
   List<JavaDocTag> getTags(String tagName);

   /**
    * Returns a list of {@link JavaDocTag} values for the given tag name (eg: @param) or an empty list if not found
    */
   List<JavaDocTag> getTags();
}
