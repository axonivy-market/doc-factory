package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.util.Collection;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.docfactory.mergefield.TemplateMergeFieldType;
import ch.ivyteam.ivy.addons.docfactory.mergefield.internal.MergeFieldsExtractor;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Recordset;

import com.aspose.words.IMailMergeDataSource;

public class MailMergeDataSourceGenerator {

  private Recordset recordset = new Recordset();
  private String tableName;
  TemplateMergeField templateMergeField;

  private MailMergeDataSourceGenerator() {}

  public static IMailMergeDataSource getFromCollectionTypeTemplateMergeField(
          TemplateMergeField templateMergeField) {
    API.checkNotNull(templateMergeField, "TemplateMergeField");
    MailMergeDataSource mmds = null;
    if (templateMergeField.isPossibleTableData()
            && templateMergeField.getType().is(TemplateMergeFieldType.COLLECTION)) {
      MailMergeDataSourceGenerator serializableMailMergeDataSource = new MailMergeDataSourceGenerator();
      serializableMailMergeDataSource.templateMergeField = templateMergeField;
      serializableMailMergeDataSource.tableName = templateMergeField.getMergeFieldName();
      serializableMailMergeDataSource.buildRecordset();

      mmds = new MailMergeDataSource(serializableMailMergeDataSource.recordset,
              templateMergeField.getMergeFieldName());
      Collection<TemplateMergeField> collectionFieldValue = templateMergeField.getChildren();
      for (TemplateMergeField tmf : collectionFieldValue) {
        if (tmf.isCollection() || tmf.getType().is(TemplateMergeFieldType.OBJECT)) {
          makeEmbeddedMailMergeDataSources(mmds, tmf, serializableMailMergeDataSource.tableName);
        }
      }
    }
    return mmds;
  }

  private static IMailMergeDataSource getFromCollectionTypeTemplateMergeField(
          TemplateMergeField templateMergeField, String parent) {
    API.checkNotNull(templateMergeField, "TemplateMergeField");
    MailMergeDataSource mmds = null;
    if (templateMergeField.isPossibleTableData()
            && templateMergeField.getType().is(TemplateMergeFieldType.COLLECTION)) {
      MailMergeDataSourceGenerator serializableMailMergeDataSource = new MailMergeDataSourceGenerator();
      serializableMailMergeDataSource.templateMergeField = templateMergeField;
      serializableMailMergeDataSource.tableName = getLastPart(parent)
              + getSecondPart(templateMergeField.getMergeFieldName());
      serializableMailMergeDataSource.buildRecordset();

      mmds = new MailMergeDataSource(serializableMailMergeDataSource.recordset,
              getLastPart(parent) + getSecondPart(templateMergeField.getMergeFieldName()));
      Collection<TemplateMergeField> collectionFieldValue = templateMergeField.getChildren();
      for (TemplateMergeField tmf : collectionFieldValue) {
        if (tmf.isCollection() || tmf.getType().is(TemplateMergeFieldType.OBJECT)) {
          makeEmbeddedMailMergeDataSources(mmds, tmf, serializableMailMergeDataSource.tableName);
        }
      }
    }
    return mmds;
  }

  private static IMailMergeDataSource makeEmbeddedMailMergeDataSources(IMailMergeDataSource parentMmds,
          TemplateMergeField templateMergeField, String parentTableName) {
    MailMergeDataSource parentDataSource = (MailMergeDataSource) parentMmds;
    String simplifiedName = getLastPart(parentTableName);
    IMailMergeDataSource childMailMergeDataSource = null;
    if (templateMergeField.isPossibleTableData() && templateMergeField.isCollection()) {
      childMailMergeDataSource = getFromCollectionTypeTemplateMergeField(templateMergeField, parentTableName);
      parentDataSource.putChildMailMergeDataSource(childMailMergeDataSource,
              simplifiedName + getSecondPart(templateMergeField.getMergeFieldName()));
    }
    if (templateMergeField.isPossibleTableData()
            && templateMergeField.getType().is(TemplateMergeFieldType.OBJECT)) {
      Collection<TemplateMergeField> childrenFields = MergeFieldsExtractor
              .getMergeFields(templateMergeField.getValue());
      for (TemplateMergeField tmf : childrenFields) {
        IMailMergeDataSource result = getFromCollectionTypeTemplateMergeField(tmf, parentTableName);
        if (result != null) {
          parentDataSource.putChildMailMergeDataSource(result,
                  simplifiedName + getSecondPart(tmf.getMergeFieldName()));
        }
      }
    }
    return parentDataSource;
  }

  private void buildRecordset() {
    Collection<Collection<TemplateMergeField>> mergeFieldsCollections = MergeFieldsExtractor
            .getMergeFieldsForCollectionTemplateMergeField(tableName, templateMergeField);
    String simplifiedName = getLastPart(tableName);

    for (Collection<TemplateMergeField> mergeFields : mergeFieldsCollections) {
      Record rec = new Record();
      for (TemplateMergeField tmf : mergeFields) {
        if (tmf.getMergeFieldName().equalsIgnoreCase(tableName)) {
          rec.putField(simplifiedName, tmf.getValueForMailMerging());
        } else {
          rec.putField(simplifiedName + getSecondPart(tmf.getMergeFieldName()), tmf.getValueForMailMerging());
        }
      }
      recordset.add(rec);
    }
  }

  private static String getSecondPart(String mergeFieldName) {
    if (mergeFieldName.contains(".")) {
      return mergeFieldName.substring(mergeFieldName.indexOf("."));
    }
    return "." + mergeFieldName;
  }

  public String getTableName() throws Exception {
    return tableName;
  }

  private static String getLastPart(String s) {
    if (s.contains(".")) {
      return s.substring(s.lastIndexOf(".") + 1);
    }
    return s;
  }

}
