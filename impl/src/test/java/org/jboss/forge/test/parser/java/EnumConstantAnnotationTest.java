/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.EnumConstant;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.test.parser.java.common.AnnotationTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class EnumConstantAnnotationTest extends AnnotationTest<JavaEnum, EnumConstant<JavaEnum>>
{
   @Override
   public void resetTests()
   {
      InputStream stream = EnumConstantAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedEnumConstant.java");
      EnumConstant<JavaEnum> field = JavaParser.parse(JavaEnum.class, stream).getEnumConstants().get(0);
      setTarget(field);
   }

}
