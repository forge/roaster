/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.Objects;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ImportImpl implements Import
{
   private AST ast = null;
   private ImportDeclaration imprt = null;

   private void init(final JavaSource<?> parent)
   {
      CompilationUnit cu = (CompilationUnit) parent.getInternal();
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
      if (isWildcard())
      {
         return WILDCARD;
      }
      // if the name is simple it must be a wildcard import, handled one above
      if (imprt.getName().isSimpleName())
      {
         throw new IllegalStateException("Unexpected simple name for an import");
      }
      return ((QualifiedName) imprt.getName()).getName().getFullyQualifiedName();
   }

   @Override
   public String getQualifiedName()
   {
      if (isWildcard())
      {
         return imprt.getName().getFullyQualifiedName() + "." + WILDCARD;
      }
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
      return Objects.hashCode(imprt);
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
         return true;

      if (!(obj instanceof Import))
         return false;

      Import other = (Import) obj;
      return getQualifiedName().equals(other.getQualifiedName());
   }

   @Override
   public String toString()
   {
      return "Import [" + getQualifiedName() + "]";
   }

   @Override
   public String getPackage()
   {
      if (isWildcard())
      {
         return imprt.getName().getFullyQualifiedName();
      }
      return Types.getPackage(getQualifiedName());
   }
}