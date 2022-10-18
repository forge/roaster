package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaRecord;
import org.jboss.forge.roaster.model.JavaRecordComponent;

public interface JavaRecordSource extends
         JavaSource<JavaRecordSource>,
         JavaRecord<JavaRecordSource>,
         MethodHolderSource<JavaRecordSource>,
         TypeHolderSource<JavaRecordSource>
{
   JavaRecordComponentSource addRecordComponent(String type, String name);
   JavaRecordComponentSource addRecordComponent(Class<?> type, String name);

   JavaRecordSource removeRecordComponent(String name);
   JavaRecordSource removeRecordComponent(JavaRecordComponent recordComponent);

}
