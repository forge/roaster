package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaRecordComponent;

public interface JavaRecordComponentSource extends
         JavaRecordComponent<JavaRecordSource>,
         ParameterSource<JavaRecordSource>
{
   @Override
   default boolean isFinal()
   {
      throw new UnsupportedOperationException("A record component is always final");
   }

   @Override
   default ParameterSource<JavaRecordSource> setFinal(boolean finl)
   {
      throw new UnsupportedOperationException("A record component cannot be marked as final");
   }
}
