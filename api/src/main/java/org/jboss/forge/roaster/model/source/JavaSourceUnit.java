/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaType;

public interface JavaSourceUnit {
	
	/**
	 * A type in the source file governs the declaration environment such as 'package' and 'import' information.
	 * 
	 * @return basically the first element of {@link #getTopLevelDeclaredTypes()}
	 */
	JavaType<?> getGoverningType();
	
	/**
	 * 
	 * @return types in the toplevel of source code in the declared order.
	 */
	List<? extends JavaType<?>> getTopLevelDeclaredTypes();
	
	
	/**
	 * write out the source code operated.
	 * 
	 * @return
	 */
	String toString();
	
	String toUnformattedString();

}
