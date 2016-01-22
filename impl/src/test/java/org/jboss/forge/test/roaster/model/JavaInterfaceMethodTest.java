package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JavaInterfaceMethodTest
{

   private InputStream stream;
   private JavaInterfaceSource javaInterface;
   private MethodSource<JavaInterfaceSource> method;

   @Rule
   public final ExpectedException exception = ExpectedException.none();

   @Before
   public void reset()
   {
      stream = JavaInterfaceMethodTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockInterface.java");
      javaInterface = Roaster.parse(JavaInterfaceSource.class, stream);
      javaInterface.addMethod("URL rewriteURL(String pattern, String replacement);");
      method = javaInterface.getMethods().get(javaInterface.getMethods().size() - 1);
   }

   @Test
   public void testGetMethodByString()
   {
      javaInterface.addMethod("public void random() { }");
      javaInterface.addMethod("public void random(String randomString) { }");

      MethodSource<JavaInterfaceSource> randomMethod = javaInterface.getMethod("random");

      List<ParameterSource<JavaInterfaceSource>> randomMethodParameters = randomMethod.getParameters();
      assertEquals(0, randomMethodParameters.size());
      assertFalse(javaInterface.hasMethodSignature(method.getName()));

      MethodSource<JavaInterfaceSource> randomMethodString = javaInterface.getMethod("random", "String");
      List<ParameterSource<JavaInterfaceSource>> randomMethodStringParameters = randomMethodString.getParameters();
      assertEquals(1, randomMethodStringParameters.size());
      assertEquals("String", randomMethodStringParameters.get(0).getType().getName());
      assertFalse(javaInterface.hasMethodSignature(method.getName()));
   }

   @Test
   public void testGetMethodByClass() throws Exception
   {
      javaInterface.addMethod("public void random() { }");
      javaInterface.addMethod("public void random(String randomString) { }");

      MethodSource<JavaInterfaceSource> randomMethod = javaInterface.getMethod("random");
      List<ParameterSource<JavaInterfaceSource>> randomMethodParameters = randomMethod.getParameters();
      assertEquals(0, randomMethodParameters.size());
      assertFalse(javaInterface.hasMethodSignature(method.getName()));

      MethodSource<JavaInterfaceSource> randomMethodString = javaInterface.getMethod("random", String.class);
      List<ParameterSource<JavaInterfaceSource>> randomMethodStringParameters = randomMethodString.getParameters();
      assertEquals(1, randomMethodStringParameters.size());
      assertEquals("String", randomMethodStringParameters.get(0).getType().getName());
      assertFalse(javaInterface.hasMethodSignature(method.getName()));
   }

   @Test
   public void testSetAbstract()
   {
      assertTrue(method.isAbstract());
      method.setAbstract(true);
      Assert.assertEquals(((MethodDeclaration) method.getInternal()).getModifiers(), Modifier.ABSTRACT);
      exception.expect(IllegalStateException.class);
      method.setAbstract(false);
   }

   @Test
   public void testSetConstructor()
   {
      assertFalse(method.isConstructor());
      exception.expect(UnsupportedOperationException.class);
      method.setConstructor(true);
   }

   @Test
   public void testSetPublic()
   {
      assertTrue(method.isPublic());
      method.setPublic();
      Assert.assertEquals(((MethodDeclaration) method.getInternal()).getModifiers(), Modifier.PUBLIC);

   }

   @Test
   public void testSetPrivate()
   {
      assertFalse(method.isPrivate());
      exception.expect(UnsupportedOperationException.class);
      method.setPrivate();
   }

   @Test
   public void testSetProtected()
   {
      assertFalse(method.isProtected());
      exception.expect(UnsupportedOperationException.class);
      method.setProtected();
   }

   @Test
   public void testSetPackagePrivate()
   {
      assertFalse(method.isPackagePrivate());
      exception.expect(UnsupportedOperationException.class);
      method.setPackagePrivate();
   }

   @Test
   public void testSetDefault()
   {
      method.setDefault(true);
      method.setBody("Object o;");
      Assert.assertEquals(method.getBody(), "Object o;");
   }

   @Test
   public void testSetStatic()
   {
      method.setStatic(true);
      method.setBody("Object o;");
      Assert.assertEquals(method.getBody(), "Object o;");
   }

   @Test
   public void testSetBody()
   {
      exception.expect(IllegalStateException.class);
      method.setBody("Object o;");
   }
}