package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.mergefield.TemplateMergeFieldType;
import ch.ivyteam.ivy.addons.docfactory.test.data.Address;
import ch.ivyteam.ivy.addons.docfactory.test.data.Insurance;
import ch.ivyteam.ivy.addons.docfactory.test.data.InsuranceBasket;
import ch.ivyteam.ivy.addons.docfactory.test.data.Order;
import ch.ivyteam.ivy.addons.docfactory.test.data.OrderDetail;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.scripting.objects.List;

public class TemplateMergeFieldTest {

  @Test
  public void withName_returns_TemplateMergeField_as_TEXT() {
    TemplateMergeField result = TemplateMergeField.withName("aName");

    TemplateMergeField expected = TemplateMergeField.withName("aName")
            .withValueAs(TemplateMergeFieldType.TEXT, "");
    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals("")).isTrue();
    assertThat(result.equals(expected)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_String_as_value_is_from_type_TEXT() {
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue("aString");

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals("aString")).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.TEXT)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_File_as_value_is_from_type_FILE() {
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(new File("test.doc"));

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(new File("test.doc"))).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.FILE)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_ByteArray_as_value_is_from_type_BYTES() {
    byte[] bytes = new byte[8];
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(bytes);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(bytes)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.BYTES)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_Java_Date_as_value_is_from_type_DATE() {
    Calendar cal = Calendar.getInstance();
    cal.set(1972, 9, 19);
    Date birthday = cal.getTime();
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(birthday);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(birthday)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.DATE)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_Sql_Date_as_value_is_from_type_DATE() {
    Calendar cal = Calendar.getInstance();
    cal.set(1972, 9, 19);
    java.sql.Date birthday = java.sql.Date.valueOf("1972-10-19");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(birthday);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(birthday)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.DATE)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_int_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(25);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(25)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.NUMBER)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_long_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(2589l);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(2589l)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.NUMBER)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_double_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(28.66);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(28.66)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.NUMBER)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_double_as_value_hasNoChildren() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(28.66);

    assertThat(!result.hasChildren()).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_is_from_type_Collection() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(hobbies)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.COLLECTION)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_hasChildren() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.hasChildren()).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_has_same_number_of_children_than_list_size() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);
    assertThat(result.getChildren().size() == hobbies.size()).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_is_from_type_Collection() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(hobbies)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.COLLECTION)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_hasChildren() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.hasChildren()).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_has_same_number_of_children_than_list_size() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.getChildren().size() == hobbies.size()).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_Set_as_value_is_from_type_Collection() {
    java.util.Set<String> hobbies = new HashSet<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(hobbies)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.COLLECTION)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_Map_as_value_is_from_type_Collection() {
    java.util.Map<Integer, String> hobbies = new HashMap<>();
    hobbies.put(1, "hobbie1");
    hobbies.put(2, "hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(hobbies)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.COLLECTION)).isTrue();
  }

  @Test
  public void TemplateMergeField_generated_with_Collection_of_beans_has_children() {
    Collection<Person> persons = makePersons();
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(persons);

    assertThat(result.getMergeFieldName().equals("aName")).isTrue();
    assertThat(result.getValue().equals(persons)).isTrue();
    assertThat(result.getType().equals(TemplateMergeFieldType.COLLECTION)).isTrue();
    for (TemplateMergeField tmf : result.getChildren()) {
      System.out.println(tmf.getMergeFieldName() + " " + tmf.getMergeFieldValue());
    }
    System.out.println(result.getChildren().size());
    assertThat(result.hasChildren()).isTrue();
  }

  @Test
  public void test() {
    Order order = new Order();
    order.setName("Your order");
    order.setPrice(58.00);
    OrderDetail od = new OrderDetail(order);
    od.setComment("comment");
    od.setDate(Calendar.getInstance().getTime());
    order.putDetail(od);

    TemplateMergeField result = TemplateMergeField.withName("aName").asSimpleValue(order);
    System.out.println(result.hasChildren());
  }

  private Collection<Person> makePersons() {
    Collection<Person> persons = new ArrayList<>();

    persons.add(Person.withNameFirstname("Comba", "Emmanuel")
            .withAddress(Address.withStreetZipCodeCityCountry("Muristrasse 4", "3005", "Bern", "CH"))
            .withBirthday(Calendar.getInstance().getTime())
            .withInsuranceBasket(
                    InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
                            .putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
                            .putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
            .withId(new BigDecimal(213546)));

    persons.add(Person.withNameFirstname("Tuchel", "Lars")
            .withAddress(Address.withStreetZipCodeCityCountry("Seegasse 4", "4534", "Biel", "CH"))
            .withBirthday(Calendar.getInstance().getTime())
            .withId(new BigDecimal(444458)));

    return persons;
  }
}
