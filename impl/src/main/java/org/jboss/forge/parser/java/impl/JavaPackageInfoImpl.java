package org.jboss.forge.parser.java.impl;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.java.JavaPackageInfo;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.SourceType;

public class JavaPackageInfoImpl extends AbstractJavaPackageSource<JavaPackageInfo> implements
         JavaPackageInfo
{

   public JavaPackageInfoImpl(JavaSource<?> enclosingType, Document document,
            CompilationUnit unit, PackageDeclaration pkg)
   {
      super(enclosingType, document, unit, pkg);
   }

   public SourceType getSourceType()
   {
      return SourceType.PACKAGEINFO;
   }

   @Override
   public String getName()
   {
      return "package-info";
   }
   
   @Override
   protected JavaPackageInfo updateTypeNames(String name)
   {
      return this;
   }

}
