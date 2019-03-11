package org.jboss.forge.roaster.model.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.forge.roaster.model.impl.Strings.enquote;
import static org.jboss.forge.roaster.model.impl.Strings.unquote;

class StringsTest
{
   @Test
   void should_quote()
   {
      assertThat(enquote("a$nfq5ei1")).isEqualTo("\"a$nfq5ei1\"");
      assertThat(enquote("\'\'")).isEqualTo("\"\'\'\"");
      assertThat(enquote("&uel;")).isEqualTo("\"&uel;\"");
   }

   @Test
   void should_unquote()
   {
      assertThat(unquote("\"a$nfq5ei1\"")).isEqualTo("a$nfq5ei1");
      assertThat(unquote("\"\'\'\"")).isEqualTo("\'\'");
      assertThat(unquote("\'\'")).isEqualTo("\'\'");
      assertThat(unquote("&uel;")).isEqualTo("&uel;");
   }
}