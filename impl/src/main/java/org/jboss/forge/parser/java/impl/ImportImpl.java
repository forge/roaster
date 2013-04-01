/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ImportImpl implements Import
{
   private AST ast = null;
   private CompilationUnit cu = null;
   private ImportDeclaration imprt = null;

   private void init(final JavaSource<?> parent)
   {
      cu = (CompilationUnit) parent.getInternal();
      ast = cu.getAST();
   }

   public ImportImpl(final JavaSource<?> parent)
   {
      init(parent);
      imprt = ast.newImportDeclaration();
   }

   public ImportImpl(final JavaSource<?> parent, final Object internal)
   {
      init(parent);
      imprt = (ImportDeclaration) internal;
   }

   @Override
   public String getSimpleName()
   {
      return Types.toSimpleName(imprt.getName().getFullyQualifiedName());
   }

   @Override
   public String getQualifiedName()
   {
      return imprt.getName().getFullyQualifiedName();
   }

   @Override
   public Import setName(final String name)
   {
      if (name.endsWith(".*"))
      {
         imprt.setName(ast.newName(Types.tokenizeClassName(name.replaceAll("\\.\\*", ""))));
         imprt.setOnDemand(true);
      }
      else
      {
         imprt.setName(ast.newName(Types.tokenizeClassName(name)));
      }
      return this;
   }

   @Override
   public boolean isStatic()
   {
      return imprt.isStatic();
   }

   @Override
   public Import setStatic(final boolean value)
   {
      imprt.setStatic(value);
      return this;
   }

   @Override
   public boolean isWildcard()
   {
      return imprt.isOnDemand();
   }

   @Override
   public Object getInternal()
   {
      return imprt;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((imprt == null) ? 0 : imprt.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
          return true;
      
      if (!(obj instanceof Import))
          return false;
      
      Import other = (Import) obj;
      if (!getQualifiedName().equals(other.getQualifiedName()))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "Import [" + getQualifiedName() + "]";
   }

   @Override
   public String getPackage()
   {
      return Types.getPackage(getQualifiedName());
   }
}
