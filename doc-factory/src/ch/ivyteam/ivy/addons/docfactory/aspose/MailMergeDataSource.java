/**
 *
 */
package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.lang.WordUtils;

import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.ref.Ref;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.scripting.objects.util.MetaType;

/**
 * @author lt
 * @since 20/04/2011 Class used by Aspose for Mail merge with regions.
 */
public class MailMergeDataSource implements IMailMergeDataSource {

  private String tableName;
  private Recordset tableValues;
  private int rowIndex;
  private List<List<CompositeObject>> childrenDatasources;
  private List<java.util.List<List<CompositeObject>>> nestedChildrenDatasources;
  private List<List<Tree>> treeChildrenDS;
  private LinkedList<IMailMergeDataSource> childrenMailMergeDataSource;

  /**
   * Instantiates the MailMergeDataSource object for Aspose MailMergeWithRegions
   * with ch.ivyteam.ivy.scripting.objects.Recordset and tableName.
   * @param recordSet : the ch.ivyteam.ivy.scripting.objects.Recordset
   * @param tableName : the tableName
   */
  public MailMergeDataSource(Recordset recordSet, String tableName) {
    this.tableValues = recordSet;
    this.tableName = tableName;
    // When the data source is initialized, it must be positioned before the
    // first record.
    this.rowIndex = -1;
  }

  /**
   * Instantiates the MailMergeDataSource object for Aspose MailMergeWithRegions
   * with list from columnNames and List from List of Fields Values and
   * tableName.
   * @param _columnNames : The column names List (List from String)
   * @param _val : the list of list of Values List<List<String>>
   * @param _tablename : the table name.
   */
  public MailMergeDataSource(List<String> _columnNames, List<List<Object>> _val, String _tablename) {
    Recordset rs = new Recordset(_columnNames);

    for (int j = 0; j < _val.size(); j++) {
      if (rs.columnCount() >= _val.get(j).size()) {
        rs.add(_val.get(j));
      }
    }
    this.tableValues = rs;
    this.tableName = _tablename;
    // When the data source is initialized, it must be positioned before the
    // first record.
    this.rowIndex = -1;
  }

  /**
   * Instantiates the MailMergeDataSource object for Aspose MailMerge
   * WithNestedRegions
   * @param parentDataSourceObjects List of CompositeObjects (DataClasses)
   *          corresponding to the parent Table data
   * @param _childrenDatasources List of List of CompositeObjects (DataClasses)
   *          corresponding to the children Table data
   * @throws Exception
   */
  public MailMergeDataSource(List<CompositeObject> parentDataSourceObjects,
          List<List<CompositeObject>> _childrenDatasources) throws Exception {

    this.tableName = parentDataSourceObjects.get(0).getClass().getName()
            .substring(parentDataSourceObjects.get(0).getClass().getName().lastIndexOf(".") + 1);
    this.tableValues = new Recordset();
    this.tableValues.add(parentDataSourceObjects.get(0));
    for (int i = 1; i < parentDataSourceObjects.size(); i++) {
      // Check List consistency: all the compositeObjects have to have the same
      // type
      if (parentDataSourceObjects.get(i).getClass().getName() != parentDataSourceObjects.get(i - 1).getClass()
              .getName()) {
        throw new IllegalArgumentException(Ivy.cms().co(
                "/ch/ivyteam/ivy/addons/docfactory/messages/mailMergeWithRegionsNeedsListOfSameDataClassTypes"));
      }
      this.tableValues.add(parentDataSourceObjects.get(i));
    }
    // When the data source is initialized, it must be positioned before the
    // first record.
    this.rowIndex = -1;
    if (_childrenDatasources != null && !_childrenDatasources.isEmpty()) {
      this.childrenDatasources = _childrenDatasources;
    }

  }

  /**
   * Instantiates the MailMergeDataSource object for Aspose MailMerge
   * WithNestedRegions. The parameter is a DataClass that can contain List of
   * other DataClasses. Each of these is will be considered as a child data
   * source.
   * @param dataSourceObjects
   */
  public MailMergeDataSource(List<CompositeObject> dataSourceObjects) {
    if (dataSourceObjects == null || dataSourceObjects.isEmpty()) {
      return;
    }
    this.tableName = dataSourceObjects.get(0).getClass().getName()
            .substring(dataSourceObjects.get(0).getClass().getName().lastIndexOf(".") + 1);
    this.tableValues = new Recordset();
    this.tableValues.add(dataSourceObjects.get(0));
    for (int i = 1; i < dataSourceObjects.size(); i++) {
      // Check List consistency: all the compositeObjects have to have the same
      // type
      if (dataSourceObjects.get(i).getClass().getName() != dataSourceObjects.get(i - 1).getClass()
              .getName()) {
        throw new IllegalArgumentException(Ivy.cms().co(
                "/ch/ivyteam/ivy/addons/docfactory/messages/mailMergeWithRegionsNeedsListOfSameDataClassTypes"));
      }
      this.tableValues.add(dataSourceObjects.get(i));
    }
    this.rowIndex = -1;
    makeListOfChildrenDataSourceInComposite(dataSourceObjects);
  }

  public <T> MailMergeDataSource(java.util.Collection<T> dataSourceObjects) {
    if (dataSourceObjects == null || dataSourceObjects.isEmpty()) {
      return;
    }
    java.util.List<T> dataList = new ArrayList<>();
    dataList.addAll(dataSourceObjects);
    this.tableName = dataList.get(0).getClass().getName()
            .substring(dataList.get(0).getClass().getName().lastIndexOf(".") + 1);
    this.tableValues = new Recordset();

    // this.tableValues.add(dataList.get(0));
    for (int i = 1; i < dataList.size(); i++) {
      // Check List consistency: all the compositeObjects have to have the same
      // type
      if (dataList.get(i).getClass().getName() != dataList.get(i - 1).getClass().getName()) {
        throw new IllegalArgumentException(Ivy.cms().co(
                "/ch/ivyteam/ivy/addons/docfactory/messages/mailMergeWithRegionsNeedsListOfSameDataClassTypes"));
      }
      // this.tableValues.add(dataList.get(i));
    }
    this.rowIndex = -1;
    // makeListOfChildrenDataSourceInComposite(dataSourceObjects);
  }

  @SuppressWarnings("all")
  private void makeListOfChildrenDataSourceInComposite(List<CompositeObject> dataSourceObjects) {
    this.nestedChildrenDatasources = List.create(MetaType.listOf(MetaType.listOf(CompositeObject.class)));
    for (int j = 0; j < dataSourceObjects.size(); j++) {
      CompositeObject co = dataSourceObjects.get(j);
      Field[] fields = co.getClass().getDeclaredFields();
      List<List<CompositeObject>> tempList = List.create(MetaType.listOf(CompositeObject.class));
      for (int i = 0; i < fields.length; i++) {
        try {
          if (fields[i].toString().contains("ch.ivyteam.ivy.scripting.objects.List")) {
            Method m = co.getClass().getMethod("get" + WordUtils.capitalize(fields[i].getName()),
                    new Class[] {});
            Object o = m.invoke(co, new Object[] {});
            List<?> test = (List<?>) o;
            if (!test.isEmpty() && test.get(0) instanceof CompositeObject) {
              tempList.add((List<CompositeObject>) test);
            }
          }
        } catch (Exception e) {
          Ivy.log().error("In makeListOfChildrenDataSourceInComposite " + e.getMessage(), e);
        }
      }
      this.nestedChildrenDatasources.add(tempList);
    }
  }

  public MailMergeDataSource(Tree _tree) {
    this.rowIndex = -1;
    if (_tree == null) {
      return;
    }
    this.treeChildrenDS = List.create(MetaType.listOf(Tree.class));
    List<Tree> children = _tree.getChildren();
    if (!children.isEmpty() && children.get(0).getValue() instanceof CompositeObject) {
      this.tableName = children.get(0).getValue().getClass().getName()
              .substring(children.get(0).getValue().getClass().getName().lastIndexOf(".") + 1);
      this.tableValues = new Recordset();
      this.tableValues.add((CompositeObject) children.get(0).getValue());
      this.treeChildrenDS.add(children.get(0).getChildren());
      for (int i = 1; i < children.size(); i++) {
        this.tableValues.add((CompositeObject) children.get(i).getValue());
        this.treeChildrenDS.add(children.get(i).getChildren());
      }
    }
  }

  public MailMergeDataSource(List<Tree> _trees, String _tablename) {
    this.rowIndex = -1;
    if (_trees == null || _trees.isEmpty() || _tablename == null || _tablename.trim().length() == 0) {
      return;
    }
    this.tableName = _tablename;
    this.treeChildrenDS = List.create(MetaType.listOf(Tree.class));

    this.tableValues = new Recordset();
    for (Tree t : _trees) {
      CompositeObject ob = null;
      try {
        ob = (CompositeObject) t.getValue();
        if (this.tableName.trim()
                .equals(ob.getClass().getName().substring(ob.getClass().getName().lastIndexOf(".") + 1))) {
          this.tableValues.add(ob);
          this.treeChildrenDS.add(t.getChildren());
        }
      } catch (Exception e) {
        Ivy.log().error(e.getMessage(), e);
      }
    }
  }

  @Override
  public boolean moveNext() throws Exception {
    if (rowIndex + 1 < tableValues.size()) {
      rowIndex++;
      return true;
    } else {
      rowIndex = -1;
      return false;
    }
  }

  @Override
  public String getTableName() throws Exception {
    return tableName;
  }

  public void setTableName(String name) {
    tableName = name;
  }

  @Override
  public boolean getValue(String fieldName, Ref<Object> fieldValue) throws Exception {
    try {
      if (tableValues.getKeys().contains(fieldName)) {
        fieldValue.set(tableValues.getColumn(fieldName).get(rowIndex));
        return true;
      } else {
        fieldValue.set(0);
        return false;
      }
    } catch (Exception e) {
      Ivy.log().error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public IMailMergeDataSource getChildDataSource(String _tablename)
          throws Exception {
    if (this.childrenDatasources != null && !this.childrenDatasources.isEmpty()) {
      int i = rowIndex >= 0 ? rowIndex : 0;
      if (i < this.childrenDatasources.size()) {
        List<CompositeObject> liste = this.childrenDatasources.get(i);
        if (liste != null && !liste.isEmpty()) {
          if (liste.get(0).getClass().getName()
                  .substring(liste.get(0).getClass().getName().lastIndexOf(".") + 1).equals(_tablename)) {
            return new MailMergeDataSource(liste, null);
          }
        }
      }
    } else if (this.nestedChildrenDatasources != null && !this.nestedChildrenDatasources.isEmpty()) {
      int i = rowIndex >= 0 ? rowIndex : 0;
      if (i < this.nestedChildrenDatasources.size()) {
        for (List<CompositeObject> liste : this.nestedChildrenDatasources.get(i)) {
          if (liste != null && !liste.isEmpty()) {
            if (liste.get(0).getClass().getName()
                    .substring(liste.get(0).getClass().getName().lastIndexOf(".") + 1).equals(_tablename)) {
              return new MailMergeDataSource(liste);
            }
          }
        }
      }
    } else if (this.treeChildrenDS != null && !this.treeChildrenDS.isEmpty()) {
      int i = rowIndex >= 0 ? rowIndex : 0;
      if (i < this.treeChildrenDS.size()) {
        return new MailMergeDataSource(this.treeChildrenDS.get(i), _tablename);
      }
    } else if (this.childrenMailMergeDataSource != null) {
      int i = 0;
      for (IMailMergeDataSource ds : this.childrenMailMergeDataSource) {
        if (ds.getTableName().equals(_tablename)) {
          return this.childrenMailMergeDataSource.remove(i);
        }
        i++;
      }
    }
    return null;
  }

  void putChildMailMergeDataSource(IMailMergeDataSource mmds, @SuppressWarnings("all") String tblName) {
    if (mmds == null) {
      return;
    }
    if (this.childrenMailMergeDataSource == null) {
      this.childrenMailMergeDataSource = new LinkedList<>();
    }
    this.childrenMailMergeDataSource.add(mmds);
  }

  protected Recordset getTableValues() {
    return this.tableValues;
  }

}
