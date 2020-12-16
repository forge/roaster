package org.jboss.forge.test.roaster.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.junit.jupiter.api.Test;

public class JavaRecordTest
{
   @Test
   void testParseRecord()
   {
      JavaRecordSource record = Roaster.parse(JavaRecordSource.class,
               "public record PhoneNumber(String prefix, String number){}");
      assertThat(record.getPackage()).isNullOrEmpty();
      assertThat(record.getVisibility()).isEqualTo(Visibility.PUBLIC);
      assertThat(record.getName()).isEqualTo("PhoneNumber");
   }
}
