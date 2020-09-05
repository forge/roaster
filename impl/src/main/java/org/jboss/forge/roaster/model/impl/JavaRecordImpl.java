/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.RecordDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a Java Source File containing a Record Type.
 */
public class JavaRecordImpl extends AbstractJavaSource<JavaRecordSource> implements JavaRecordSource
{
   public JavaRecordImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            RecordDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaRecordSource updateTypeNames(String name)
   {
      return this;
   }

}
