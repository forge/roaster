/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.Objects;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Formatter;

public class JavaPackageInfoImpl extends JavaSourceImpl<JavaPackageInfoSource>
         implements JavaPackageInfoSource
{

   private PackageDeclaration pkg;
   public JavaPackageInfoImpl(JavaSource<?> enclosingType, Document document,
            CompilationUnit unit, PackageDeclaration pkg)
   {
      super(enclosingType, document, unit);
      this.pkg = pkg;
   }

   @Override
   public String getName()
   {
      return "package-info";
   }

   @Override
   protected ASTNode getDeclaration()
   {
      if (pkg != null)
      {
         return pkg;
      }
      throw new ParserException("Source body was not of the expected type (PackageDeclaration).");
   }

   @Override
   public JavaPackageInfoImpl setName(final String name)
   {
      throw new UnsupportedOperationException("Changing name of [" + getQualifiedName() + "] not supported.");
   }

   /*
    * Non-manipulation methods.
    */
   /**
    * Return this {@link JavaType} file as a String
    */
   @Override
   public String toString()
   {
      return Formatter.format(toUnformattedString());
   }

   @Override
   public String toUnformattedString()
   {
      Document documentLocal = new Document(this.document.get());

      try
      {
         TextEdit edit = unit.rewrite(documentLocal, null);
         edit.apply(documentLocal);
      }
      catch (Exception e)
      {
         throw new ParserException("Could not modify source: " + unit.toString(), e);
      }

      return documentLocal.get();
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + Objects.hashCode(pkg);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JavaPackageInfoImpl other = (JavaPackageInfoImpl) obj;
      if (pkg == null)
      {
         if (other.pkg != null)
            return false;
      }
      else if (!pkg.equals(other.pkg))
         return false;
      return super.equals(obj);
   }

   public PackageDeclaration getPkg()
   {
      return pkg;
   }

   @Override
   public JavaDocSource<JavaPackageInfoSource> getJavaDoc()
   {
      Javadoc javadoc = pkg.getJavadoc();
      if (javadoc == null)
      {
         javadoc = pkg.getAST().newJavadoc();
         pkg.setJavadoc(javadoc);
      }
      return new JavaDocImpl<>(this, javadoc);
   }

   @Override
   public JavaPackageInfoSource removeJavaDoc()
   {
      pkg.setJavadoc(null);
      return this;
   }

   @Override
   public boolean hasJavaDoc()
   {
      return pkg.getJavadoc() != null;
   }

   @Override
   protected JavaPackageInfoSource updateTypeNames(String name)
   {
     throw new ParserException();
   }
}