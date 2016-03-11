package org.jboss.forge.roaster.model.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Methods;

public class JavaInterfaceMethodImpl extends MethodImpl<JavaInterfaceSource>
{

   public JavaInterfaceMethodImpl(JavaInterfaceSource parent)
   {
      super(parent);
   }

   public JavaInterfaceMethodImpl(JavaInterfaceSource parent, Method reflectMethod)
   {
       this(parent);
       // Set method return type
       if (reflectMethod.getReturnType() == Void.TYPE)
       {
       setReturnTypeVoid();
       }
       else
       {
       setReturnType(reflectMethod.getReturnType());
       }
       // Set method name
       setName(reflectMethod.getName());
       // Set method parameters
       Class<?>[] paramTypes = reflectMethod.getParameterTypes();
       String[] paramNames = Methods.generateParameterNames(paramTypes);
       for (int i = 0; i < paramTypes.length; i++)
       {
       addParameter(paramTypes[i], paramNames[i]);
       }
   }

   public JavaInterfaceMethodImpl(JavaInterfaceSource parent, Object internal)
   {
      super(parent, internal);
   }

   public JavaInterfaceMethodImpl(JavaInterfaceSource parent, String method)
   {
      super(parent, method);
   }

   @Override
   public boolean isAbstract()
   {
      if (isDefault() || isStatic())
      {
         return false;
      }
      else
      {
         return true;
      }
   }

   @Override
   public MethodSource<JavaInterfaceSource> setAbstract(boolean abstrct)
   {
      if (abstrct)
      {
         return super.setAbstract(abstrct);
      }
      else
      {
         throw new IllegalStateException("Method interface should be 'abstract'");
      }
   }

   @Override
   public boolean isPublic()
   {
      return true;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setBody(String body)
   {
      if (body == null)
      {
         return this;
      }
      if (isDefault())
      {
         return super.setBody(body);
      }
      else if (isStatic())
      {
         return super.setBody(body);
      }
      else
      {
         throw new IllegalStateException("To set body, a method interface should be 'default' or 'static'");
      }
   }

   @Override
   public boolean isConstructor()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setConstructor(boolean constructor)
   {
      throw new UnsupportedOperationException("An interface couldn't have a constructor");
   }

   @Override
   public boolean isFinal()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setFinal(boolean finl)
   {
      throw new UnsupportedOperationException("A method interface couldn't be final");
   }

   @Override
   public boolean isNative()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setNative(boolean value)
   {
      throw new UnsupportedOperationException("A method interface couldn't be native");
   }

   @Override
   public boolean isPrivate()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setPrivate()
   {
      throw new UnsupportedOperationException("A method interface couldn't be private");
   }

   @Override
   public boolean isProtected()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setProtected()
   {
      throw new UnsupportedOperationException("A method interface couldn't be protected");
   }

   @Override
   public boolean isPackagePrivate()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setPackagePrivate()
   {
      throw new UnsupportedOperationException("A method interface couldn't be package private");
   }

   @Override
   public boolean isSynchronized()
   {
      return false;
   }

   @Override
   public MethodSource<JavaInterfaceSource> setSynchronized(boolean value)
   {
      throw new UnsupportedOperationException("A method interface couldn't be synchronized");
   }

}
