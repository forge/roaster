/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

/**
 * A Java interface method is always abstract unless it is declared as a default method
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaInterfaceMethodImpl extends MethodImpl<JavaInterfaceSource>
{

   public JavaInterfaceMethodImpl(JavaInterfaceSource parent, MethodDeclaration internal, Document document)
   {
      super(parent, internal, document);
   }

   @Override
   public boolean isAbstract()
   {
      return !isDefault();
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.PUBLIC;
   }
}
