/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSourceUnit;
import org.jboss.forge.roaster.spi.Streams;
import org.junit.Assert;
import org.junit.Test;


public class JavaClassMultiDeclsInAFile {
	static String classNameMain = "MockMultipleClassDeclesInAFile";
	static String classNameSub = "MockAcompanyingClass";
	
	@Test
	public void testParseOnly() throws UnsupportedEncodingException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Streams.write(
				JavaClassMultiDeclsInAFile.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockMultipleClassDeclesInAFile.java")
				, b );
		
		JavaSourceUnit unit = 
				Roaster.parse2("UTF-8", new ByteArrayInputStream(b.toByteArray()));
		
		org.junit.Assert.
		assertEquals( new String( b.toByteArray(), "UTF-8" ), unit.toString() );
	}
	
	
	@Test
	public void testParsedResult() throws UnsupportedEncodingException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Streams.write(
				JavaClassMultiDeclsInAFile.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockMultipleClassDeclesInAFile.java")
				, b );
		
		JavaSourceUnit unit = 
				Roaster.parse2("UTF-8", new ByteArrayInputStream(b.toByteArray()));
		
		Assert.assertEquals( classNameMain, unit.getTopLevelDeclaredTypes().get(0).getName() );
		Assert.assertEquals( classNameSub, unit.getTopLevelDeclaredTypes().get(1).getName() );
		
	}
	
	
	@Test
	public void testOperationOnPackage() throws UnsupportedEncodingException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Streams.write(
				JavaClassMultiDeclsInAFile.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockMultipleClassDeclesInAFile.java")
				, b );
		
		JavaSourceUnit unit = 
				Roaster.parse2("UTF-8", new ByteArrayInputStream(b.toByteArray()));
		
		JavaClassSource j = Roaster.tryCast(unit.getGoverningType(), JavaClassSource.class);
		j.setPackage("tesstto.operated");
		
		String newsource = unit.toString();
		
		Assert.assertTrue(newsource.contains("package tesstto.operated;"));
//		Assert.assertTrue(newsource.contains("package tesstto.operted;")); // test of test
		Assert.assertTrue(newsource.contains(classNameMain));
		Assert.assertTrue(newsource.contains(classNameSub));
		
//		System.out.println( newsource ); seems ok
	}

	@Test
	public void testOperationOnAccompaningClass() throws UnsupportedEncodingException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Streams.write(
				JavaClassMultiDeclsInAFile.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockMultipleClassDeclesInAFile.java")
				, b );
		
		JavaSourceUnit unit = 
				Roaster.parse2("UTF-8", new ByteArrayInputStream(b.toByteArray()));
		
		JavaClassSource j = Roaster.tryCast( unit.getTopLevelDeclaredTypes().get(1), JavaClassSource.class);
		j.addField("private static int tesueito;");
		
		
		String newsource = unit.toString();
		
		Assert.assertTrue(newsource.contains(classNameMain));
		Assert.assertTrue(newsource.contains(classNameSub));
		Assert.assertTrue(newsource.contains("private static int tesueito;"));
//		Assert.assertTrue(newsource.contains("private static int tesueitoX;")); //test of test
		Assert.assertTrue(
				newsource.indexOf(classNameMain) < 
				newsource.indexOf(classNameSub));
		Assert.assertTrue(
				newsource.indexOf(classNameSub) < 
				newsource.indexOf("private static int tesueito;"));
		
//		System.out.println( newsource ); seems ok
		
	}
	
	@Test
	public void testBothAnnotationAdd() throws UnsupportedEncodingException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Streams.write(
				JavaClassMultiDeclsInAFile.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockMultipleClassDeclesInAFile.java")
				, b );
		
		JavaSourceUnit unit = 
				Roaster.parse2("UTF-8", new ByteArrayInputStream(b.toByteArray()));
		
		
		Class x[] = new Class[]{ Deprecated.class, SuppressWarnings.class };
		
		for ( int i = 0; i < 2; i ++ ){
			JavaClassSource j = Roaster.tryCast( unit.getTopLevelDeclaredTypes().get(i), JavaClassSource.class );
			j.addAnnotation(x[i]);
		}
		
		
		String newsource = unit.toString();
		
		Assert.assertTrue(newsource.contains(classNameMain));
		Assert.assertTrue(newsource.contains(classNameSub));
		Assert.assertTrue(newsource.contains(x[0].getSimpleName() ));
		Assert.assertTrue(newsource.contains(x[1].getSimpleName() ));
		
		Assert.assertTrue(
				newsource.indexOf(x[0].getSimpleName()) < 
				newsource.indexOf(classNameMain));
		Assert.assertTrue(
				newsource.indexOf(classNameMain) < 
				newsource.indexOf(x[1].getSimpleName()));
//		Assert.assertTrue( //test of test
//				newsource.indexOf(classNameMain) > 
//				newsource.indexOf(x[1].getSimpleName()));
		Assert.assertTrue(
				newsource.indexOf(x[1].getSimpleName()) < 
				newsource.indexOf(classNameSub));

		
//		System.out.println( newsource ); seems ok
		
	}

}
