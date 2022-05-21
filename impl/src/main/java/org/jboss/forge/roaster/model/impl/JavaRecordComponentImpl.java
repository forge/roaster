package org.jboss.forge.roaster.model.impl;

import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

public class JavaRecordComponentImpl extends ParameterImpl<JavaRecordSource> implements JavaRecordComponentSource
{
   public JavaRecordComponentImpl(final JavaRecordImpl parent, final Object internal)
   {
      super(parent, internal);
   }


}
