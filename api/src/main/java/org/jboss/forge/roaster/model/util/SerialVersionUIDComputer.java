/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MemberSource;

/**
 * Computes the SerialVersion UID value
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class SerialVersionUIDComputer
{

   private SerialVersionUIDComputer()
   {
   }

   /**
    * This is not the official algorithm as defined in the JLS spec, but it is close
    * 
    * @see {@link java.io.ObjectStreamClass#getSerialVersionUID()}
    * @return the computed serialVersionUID value
    */
   public static long compute(JavaClassSource source)
   {
      if (!source.hasInterface(Serializable.class))
      {
         return 0L;
      }
      try
      {
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
         DataOutputStream dout = new DataOutputStream(bout);

         dout.writeUTF(source.getQualifiedName());
         int classMods = 0;
         if (source.isPublic())
         {
            classMods |= Modifier.PUBLIC;
         }
         if (source.isFinal())
         {
            classMods |= Modifier.FINAL;
         }
         if (source.isInterface())
         {
            classMods |= Modifier.INTERFACE;
         }
         if (source.isAbstract())
         {
            classMods |= Modifier.ABSTRACT;
         }
         dout.writeInt(classMods);

         List<MemberSource<JavaClassSource, ?>> members = source.getMembers();
         for (MemberSource<JavaClassSource, ?> member : members)
         {
            if (!member.isPrivate())
            {
               dout.writeUTF(member.getName());
               dout.writeUTF(member.toString().replace('/', '.'));
            }
         }
         dout.flush();
         System.out.println(bout);
         MessageDigest md = MessageDigest.getInstance("SHA");
         byte[] hashBytes = md.digest(bout.toByteArray());
         long hash = 0;
         for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--)
         {
            hash = (hash << 8) | (hashBytes[i] & 0xFF);
         }
         return hash;
      }
      catch (IOException ex)
      {
         throw new RuntimeException("Error while calculating serialVersionUID", ex);
      }
      catch (NoSuchAlgorithmException ex)
      {
         throw new SecurityException(ex.getMessage());
      }
   }

}
