/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.JavaParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test whether the Charset of API given penetrates the 'JavaParser' or not.
 * 
 * @author kuzukami_user
 *
 */
public class EncodingTest
{
   private static Charset penetratedCharset = null;

   private static class DummpyParser implements JavaParser
   {
      @Override
      public <T extends JavaSource<?>> T create(Class<T> type)
      {
         throw new RuntimeException("create()");
      }

      @Override
      public JavaUnit parseUnit(InputStream data)
      {
         throw new RuntimeException("parseUnit(data)");
      }

      @Override
      public JavaUnit parseUnit(InputStream data, Charset encodingIfText)
      {
         penetratedCharset = encodingIfText;
         return new JavaUnit()
         {

            @Override
            public String toUnformattedString()
            {
               throw new RuntimeException("toUnformattedString()");
            }

            @Override
            public List<JavaType<?>> getTopLevelTypes()
            {
               return Arrays.<JavaType<?>> asList((JavaType<?>) getGoverningType());
            }

            @Override
            public <T extends JavaType<?>> T getGoverningType()
            {
               return (T) Proxy.newProxyInstance(
                        getClass().getClassLoader(), new Class[] { JavaType.class }, new InvocationHandler()
               {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                  {
                     throw new RuntimeException("invoke()");
                  }
               });
            }
         };
      }

      @Override
      public JavaType<?> parse(InputStream data)
      {
         throw new RuntimeException("parse(data)");
      }
   }

   @Before
   public void setupper()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
   {
      String parserListField = "parsers";
      Field f = Roaster.class.getDeclaredField(parserListField);
      f.setAccessible(true);
      penetratedCharset = null;
      f.set(null, Arrays.<JavaParser> asList(new DummpyParser()));
   }

   private static Charset UTF_8 = Charset.forName("UTF-8");
   private static Charset UTF_16 = Charset.forName("UTF-16BE");

   @Test
   public void testParse_Char1()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(new char[0]);
      Assert.assertEquals(UTF_8, penetratedCharset);
   }

   @Test
   public void testParse_Char2()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, new char[0]);
      Assert.assertEquals(UTF_8, penetratedCharset);
   }

   @Test
   public void testParse_String1()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse("hoge");
      Assert.assertEquals(UTF_8, penetratedCharset);
   }

   @Test
   public void testParse_String2()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, "hoge");
      Assert.assertEquals(UTF_8, penetratedCharset);
   }

   @Test
   public void testParseUnit_String1()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parseUnit("hoge");
      Assert.assertEquals(UTF_8, penetratedCharset);
   }

   private static File stdfile = new File("pom.xml");

   @Test
   public void testParse_File1() throws FileNotFoundException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(stdfile);
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_File2() throws FileNotFoundException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, stdfile);
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_File3() throws FileNotFoundException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, stdfile, UTF_16);
      Assert.assertEquals(UTF_16, penetratedCharset);
   }

   @Test
   public void testParse_Stream1()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(new ByteArrayInputStream(new byte[0]));
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_Stream2()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, new ByteArrayInputStream(new byte[0]));
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_Stream3()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, new ByteArrayInputStream(new byte[0]), UTF_16);
      Assert.assertEquals(UTF_16, penetratedCharset);
   }

   @Test
   public void testParseUnit_Stream1()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parseUnit(new ByteArrayInputStream(new byte[0]));
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParseUnit_Stream2()
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parseUnit(new ByteArrayInputStream(new byte[0]), UTF_16);
      Assert.assertEquals(UTF_16, penetratedCharset);
   }

   @Test
   public void testParse_URL1() throws IOException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(getClass().getResource(EncodingTest.class.getSimpleName() + ".class"));
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_URL2() throws IOException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, getClass().getResource(EncodingTest.class.getSimpleName() + ".class"));
      Assert.assertEquals(Roaster.getDefaultEncoding(), penetratedCharset);
   }

   @Test
   public void testParse_URL3() throws IOException
   {
      Assert.assertEquals(null, penetratedCharset);
      Roaster.parse(JavaType.class, getClass().getResource(EncodingTest.class.getSimpleName() + ".class"), UTF_16);
      Assert.assertEquals(UTF_16, penetratedCharset);
   }

}
