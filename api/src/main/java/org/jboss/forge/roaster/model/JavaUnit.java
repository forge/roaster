/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * A {@link JavaUnit} represents a Java compilation unit.
 * 
 * The source range for this type is ordinarily the entire source file.
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaUnit
{
   /**
    * A type in the source file governs the declaration environment such as 'package' and 'import' information.
    * 
    * @return basically the first element of {@link #getTopLevelTypes()}
    */
   <T extends JavaType<?>> T getGoverningType();

   /**
    * @return an immutable {@link List} of {@link JavaType} objects in the top-level of source code in the declared
    *         order.
    */
   List<JavaType<?>> getTopLevelTypes();

   /**
    * Return the generated code without any formatting.
    */
   String toUnformattedString();
}
