package org.jboss.forge.test.roaster.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaRecordComponent;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class JavaRecordTest
{
   @Test
   void testParseRecord()
   {
      JavaRecordSource record = Roaster.parse(JavaRecordSource.class,
               "public record PhoneNumber(String prefix, String number){ public String getFoo(){return null;}}");
      assertThat(record.getPackage()).isNullOrEmpty();
      assertThat(record.getMethods()).hasSize(1);
      assertThat(record.getVisibility()).isEqualTo(Visibility.PUBLIC);
      assertThat(record.getName()).isEqualTo("PhoneNumber");
   }

   @Test
   void testCreateRecord() {
      final JavaRecordSource javaRecord = Roaster.create(JavaRecordSource.class)
               .setName("PhoneNumber")
               .setPackage("org.example.foo");
      javaRecord.addRecordComponent(BigInteger.class, "number");
      javaRecord.addMethod().setName("dial").setReturnType(boolean.class).setBody("return true;");
      List<JavaRecordComponentSource> recordComponents = javaRecord.getRecordComponents();
      assertThat(recordComponents).hasSize(1);
      assertThat(recordComponents.get(0).getName()).isEqualTo("number");
      assertThat(recordComponents.get(0).getType().getQualifiedName()).isEqualTo("java.math.BigInteger");
   }

   @Test
   void testNestedTypes() {
      final JavaRecordSource javaRecord = Roaster.create(JavaRecordSource.class)
               .setName("PhoneNumber")
               .setPackage("org.example.foo");
      javaRecord.addRecordComponent(BigInteger.class, "number");
      javaRecord.addNestedType(Roaster.create(JavaInterfaceSource.class).setName("InnerInterface"));
      assertThat(javaRecord.getNestedTypes()).hasSize(1);
   }
}
