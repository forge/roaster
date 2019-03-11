/**
 *  Copyright 2019 The ModiTect authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jboss.forge.test.roaster.model;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.assertj.core.util.Lists;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.moditect.deptective.plugintest.PluginTestBase;
import org.moditect.deptective.plugintest.basic.barctorcall.BarCtorCall;
import org.moditect.deptective.plugintest.basic.barfield.BarField;
import org.moditect.deptective.plugintest.basic.barlocalvar.BarLocalVar;
import org.moditect.deptective.plugintest.basic.barloopvar.BarLoopVar;
import org.moditect.deptective.plugintest.basic.barparameter.BarParameter;
import org.moditect.deptective.plugintest.basic.barretval.BarRetVal;
import org.moditect.deptective.plugintest.basic.bartypearg.BarTypeArg;
import org.moditect.deptective.plugintest.basic.foo.Foo;
import org.moditect.deptective.plugintest.basic.foo.FooWithoutErrors;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;

public class ROASTER130 extends PluginTestBase {

   private Compilation compile() {
      Compilation compilation = Compiler.javac()
               .withOptions("-Xplugin:Deptective", getConfigFileOption())
               .compile(
                        forTestClass(BarCtorCall.class),
                        forTestClass(BarField.class),
                        forTestClass(BarLocalVar.class),
                        forTestClass(BarLoopVar.class),
                        forTestClass(BarParameter.class),
                        forTestClass(BarRetVal.class),
                        forTestClass(BarTypeArg.class),
                        forTestClass(Foo.class)
               );

      return compilation;
   }

   private String packageFooMustNotAccess(String packageName) {
      return "package org.moditect.deptective.plugintest.basic.foo must not access " + packageName;
   }

   @Test
   public void shouldAllowAccessToJavaLangAutomatically() {
      Compilation compilation = Compiler.javac()
               .withOptions("-Xplugin:Deptective", getConfigFileOption())
               .compile(forTestClass(FooWithoutErrors.class));
      assertThat(compilation).succeeded();
   }

   @Test
   public void shouldDetectInvalidClassAssignment() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barclass")
      );
   }

   @Test
   public void shouldDetectInvalidQualifiedClassReference() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barqualified")
      );
   }

   //    @Test
   //    public void shouldDetectInvalidUnusedImport() {
   //        Compilation compilation = compile();
   //        assertThat(compilation).failed();
   //
   //        assertThat(compilation).hadErrorContaining(
   //                packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barunused"));
   //    }

   @Test
   public void shouldDetectInvalidStarImport() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.bardemand")
      );
   }

   @Test
   public void shouldDetectInvalidQualifiedReferenceToStaticMethod() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barstatic")
      );
   }

   @Test
   public void shouldDetectInvalidQualifiedReferenceToStaticField() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barstaticfield")
      );
   }

   @Test
   public void shouldDetectInvalidClassLiteralReference() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barclsliteral")
      );
   }

   @Test
   public void shouldDetectInvalidReferencesInGenericBounds() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.bargenericbound")
      );

   }

   @Test
   public void shouldNotReportValidReferences() {
      // in our Testclass all "wanted" illegal references point to "org.moditect.deptective.plugintest.basic.bar*"
      // so Deptective should only return those

      Compilation compilation = compile();
      assertThat(compilation).failed();

      List<Diagnostic<? extends JavaFileObject>> collect = compilation.errors().stream()
               .filter(e -> e.getMessage(null).indexOf("must not access org.moditect.deptective.plugintest.basic.bar") == -1)
               .collect(Collectors.toList());

      assertThat(
               "Test-Class 'Foo' should not have any invalid dependencies beside those to 'org.moditect.deptective.plugintest.basic.bar*'",
               collect, Is.is(Lists.emptyList()));
   }

   @Test
   public void shouldDetectInvalidSuperClass() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barsuper"));

      // inner class
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barinnersuper"));
   }

   @Test
   public void shouldDetectInvalidImplementedInterface() {
      Compilation compilation = compile();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barinter"));

      // inner interface
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barinnerinner"));
   }

   @Test
   public void shouldDetectInvalidConstructorParameters() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barctorparam"));
   }

   @Test
   public void shouldDetectInvalidConstructorCalls() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barctorcall"));
   }

   @Test
   public void shouldDetectInvalidFieldReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barfield"));
   }

   @Test
   public void shouldDetectInvalidLocalVariableReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barlocalvar"));
   }

   @Test
   public void shouldDetectInvalidLoopVariableReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barloopvar"));
   }

   @Test
   public void shouldDetectInvalidMethodParameterReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barparameter"));
   }

   @Test
   public void shouldDetectInvalidAnnotationReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barclazzan"));
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barfieldan"));
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barvalueann"));
   }

   @Test
   public void shouldDetectInvalidAnnotationParameterReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barclass"));

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.baranparam"));
   }

   @Test
   public void shouldDetectInvalidReturnValueReferences() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess(
                        "org.moditect.deptective.plugintest.basic.barretval"
               )
      );

      // Invalid Reference in Type Parameter
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess(
                        "org.moditect.deptective.plugintest.basic.barretvalgen"
               )
      );
   }

   @Test
   public void shouldDetectInvalidTypeArguments() {
      Compilation compilation = compile();
      assertThat(compilation).failed();

      // in type argument
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.bartypearg"));

      // in class definition type argument bound
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.bargen"));

      // in 'extends' class definition type argument
      assertThat(compilation).hadErrorContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.bargentype"));
   }

   @Test
   public void shouldUseWarnReportingPolicy() {
      Compilation compilation = Compiler.javac()
               .withOptions("-Xplugin:Deptective", getConfigFileOption(), "-Adeptective.reporting_policy=WARN")
               .compile(forTestClass(BarCtorCall.class), forTestClass(BarField.class), forTestClass(BarLocalVar.class),
                        forTestClass(BarLoopVar.class), forTestClass(BarParameter.class), forTestClass(BarRetVal.class),
                        forTestClass(BarTypeArg.class), forTestClass(Foo.class)
               );

      assertThat(compilation).succeeded();
      assertThat(compilation).hadWarningContaining(
               packageFooMustNotAccess("org.moditect.deptective.plugintest.basic.barfield"));
   }
}