/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.Importer;
import org.jboss.forge.roaster.spi.WildcardImportResolver;

/**
 * Tries to resolve based on the current classloader
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class ClassLoaderWildcardImportResolver implements WildcardImportResolver
{
   @Override
   public String resolve(JavaType<?> source, String type)
   {
      if (source instanceof Importer)
      {
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         Importer<?> importer = (Importer<?>) source;
         for (Import importDeclaration : importer.getImports())
         {
            if (importDeclaration.isWildcard())
            {
               String resolvedClass = importDeclaration.getQualifiedName() + "." + type;
               try
               {
                  contextClassLoader.loadClass(resolvedClass);
                  return resolvedClass;
               }
               catch (ClassNotFoundException e)
               {
                  // Class doesn't exist
               }
            }
         }

      }
      return type;
   }

}
