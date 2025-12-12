package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.aspose.words.IMailMergeDataSource;

import ch.ivyteam.ivy.addons.docfactory.BaseMergeFieldTest;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;

public class MailMergeDataSourceGeneratorTest extends BaseMergeFieldTest {

  @Test
  public void getFromCollectionTypeTemplateMergeField_throws_IAE_with_null_parameter() throws Exception {
    assertThatThrownBy(() -> MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void getFromCollectionTypeTemplateMergeField_returns_null_if_templateMergeField_not_Collection_Type()
          throws Exception {

    TemplateMergeField tmf = TemplateMergeField.withName("myHobbies").withValue("a value");
    IMailMergeDataSource mmds = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
    assertThat(mmds).isNull();
  }

  @Test
  public void getFromCollectionTypeTemplateMergeField_CollectionOfString_returns_IMailMergeDataSource()
          throws Exception {

    Collection<String> hobbies = new ArrayList<>();
    hobbies.add("swimming");
    hobbies.add("singing");
    hobbies.add("dancing");

    TemplateMergeField tmf = TemplateMergeField.withName("myHobbies").withValue(hobbies);
    IMailMergeDataSource mmds = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
    assertThat(mmds.getTableName()).isEqualTo("myHobbies");
    for (@SuppressWarnings("unused")
    String string : hobbies) {
      assertThat(mmds.moveNext()).isTrue();
    }

  }

  @Test
  public void getFromCollectionTypeTemplateMergeField_MapOfString_returns_IMailMergeDataSource()
          throws Exception {

    Map<Integer, String> hobbies = new HashMap<>();
    hobbies.put(1, "swimming");
    hobbies.put(2, "singing");
    hobbies.put(3, "dancing");

    TemplateMergeField tmf = TemplateMergeField.withName("myHobbies").withValue(hobbies);
    IMailMergeDataSource mmds = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
    assertThat(mmds.getTableName()).isEqualTo("myHobbies");
    for (@SuppressWarnings("unused")
    String string : hobbies.values()) {
      assertThat(mmds.moveNext()).isTrue();
    }
  }

  @Test
  public void getFromCollectionTypeTemplateMergeField_CollectionOfBeans_returns_IMailMergeDataSource()
          throws Exception {

    Collection<Person> persons = new ArrayList<>();
    persons.add(Person.withName("Andersson").withBirthday(Calendar.getInstance().getTime()).withICQ(4345634));
    persons.add(Person.withName("Andrea").withBirthday(Calendar.getInstance().getTime()).withICQ(87645645));
    persons.add(Person.withName("Matti").withBirthday(Calendar.getInstance().getTime()).withICQ(337345));

    TemplateMergeField tmf = TemplateMergeField.withName("persons").withValue(persons);
    IMailMergeDataSource mmds = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
    assertThat(mmds.getTableName()).isEqualTo("persons");
    for (@SuppressWarnings("unused")
    Person p : persons) {
      assertThat(mmds.moveNext()).isTrue();
    }
  }

  @Test
  public void getFromCollectionTypeTemplateMergeField_MapOfTemplateMergeField_returns_IMailMergeDataSource()
          throws Exception {

    Map<Integer, Person> persons = new HashMap<>();
    persons.put(1,
            Person.withName("Andersson").withBirthday(Calendar.getInstance().getTime()).withICQ(4345634));
    persons.put(2,
            Person.withName("Andrea").withBirthday(Calendar.getInstance().getTime()).withICQ(87645645));
    persons.put(3, Person.withName("Matti").withBirthday(Calendar.getInstance().getTime()).withICQ(337345));

    TemplateMergeField tmf = TemplateMergeField.withName("myHobbies").withValue(persons);
    IMailMergeDataSource mmds = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
    assertThat(mmds.getTableName()).isEqualTo("myHobbies");
    for (@SuppressWarnings("unused")
    Person p : persons.values()) {
      assertThat(mmds.moveNext()).isTrue();
    }
  }

  public static class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Date birthDay;
    private long ICQNumber;

    private Person() {}

    public static Person withName(String name) {
      Person p = new Person();
      p.setName(name);
      return p;
    }

    public Person withBirthday(Date birthday) {
      this.setBirthDay(birthday);
      return this;
    }

    public Person withICQ(long icqNumber) {
      this.setICQNumber(icqNumber);
      return this;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Date getBirthDay() {
      return birthDay;
    }

    public void setBirthDay(Date birthday) {
      this.birthDay = birthday;
    }

    public long getICQNumber() {
      return ICQNumber;
    }

    public void setICQNumber(long iCQNumber) {
      ICQNumber = iCQNumber;
    }
  }

}
