package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaRecord;
import org.jboss.forge.roaster.model.JavaRecordComponent;

import java.util.List;

public interface JavaRecordSource extends
         JavaSource<JavaRecordSource>,
         JavaRecord<JavaRecordSource>,
         MethodHolderSource<JavaRecordSource>,
         InitializerHolderSource<JavaRecordSource>,
         TypeHolderSource<JavaRecordSource>,
         InterfaceCapableSource<JavaRecordSource>
{
   JavaRecordComponentSource addRecordComponent(String type, String name);
   JavaRecordComponentSource addRecordComponent(Class<?> type, String name);

   @Override
   List<JavaRecordComponentSource> getRecordComponents();

   JavaRecordSource removeRecordComponent(String name);
   JavaRecordSource removeRecordComponent(JavaRecordComponent recordComponent);

}
