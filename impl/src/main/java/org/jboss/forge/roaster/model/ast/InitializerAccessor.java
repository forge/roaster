package org.jboss.forge.roaster.model.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.jboss.forge.roaster.model.impl.InitializerImpl;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public final class InitializerAccessor
{

   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> List<InitializerSource<O>> getInitializers(O origin,
            AbstractTypeDeclaration declaration)
   {

      List<InitializerSource<O>> result = new ArrayList<>();
      List<BodyDeclaration> bodyDeclarations = declaration.bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof Initializer)
         {
            Initializer initializer = (Initializer) bodyDeclaration;
            result.add(new InitializerImpl<>(origin, initializer));
         }
      }
      return Collections.unmodifiableList(result);
   }

   public static <O extends JavaSource<O>> boolean hasInitializer(AbstractTypeDeclaration declaration,
            org.jboss.forge.roaster.model.Initializer<?, ?> initializer)
   {
      return declaration.bodyDeclarations().contains(initializer.getInternal());
   }

   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> InitializerSource<O> addInitializer(O origin,
            AbstractTypeDeclaration declaration)
   {
      InitializerImpl<O> init = new InitializerImpl<>(origin);
      declaration.bodyDeclarations().add(init.getInternal());
      return init;
   }

   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> InitializerSource<O> addInitializer(O origin,
            AbstractTypeDeclaration declaration, String initializer)
   {
      InitializerImpl<O> init = new InitializerImpl<>(origin, initializer);
      declaration.bodyDeclarations().add(init.getInternal());
      return init;
   }

   public static void removeInitializer(AbstractTypeDeclaration declaration,
            org.jboss.forge.roaster.model.Initializer<?, ?> initializer)
   {
      declaration.bodyDeclarations().remove(initializer.getInternal());
   }

}
