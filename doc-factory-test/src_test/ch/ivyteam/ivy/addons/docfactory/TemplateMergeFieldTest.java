package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

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
    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(""));
    assertTrue(result.equals(expected));
  }

  @Test
  public void TemplateMergeField_generated_with_String_as_value_is_from_type_TEXT() {
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue("aString");

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals("aString"));
    assertTrue(result.getType().equals(TemplateMergeFieldType.TEXT));
  }

  @Test
  public void TemplateMergeField_generated_with_File_as_value_is_from_type_FILE() {
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(new File("test.doc"));

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(new File("test.doc")));
    assertTrue(result.getType().equals(TemplateMergeFieldType.FILE));
  }

  @Test
  public void TemplateMergeField_generated_with_ByteArray_as_value_is_from_type_BYTES() {
    byte[] bytes = new byte[8];
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(bytes);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(bytes));
    assertTrue(result.getType().equals(TemplateMergeFieldType.BYTES));
  }

  @Test
  public void TemplateMergeField_generated_with_Java_Date_as_value_is_from_type_DATE() {
    Calendar cal = Calendar.getInstance();
    cal.set(1972, 9, 19);
    Date birthday = cal.getTime();
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(birthday);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(birthday));
    assertTrue(result.getType().equals(TemplateMergeFieldType.DATE));
  }

  @Test
  public void TemplateMergeField_generated_with_Sql_Date_as_value_is_from_type_DATE() {
    Calendar cal = Calendar.getInstance();
    cal.set(1972, 9, 19);
    java.sql.Date birthday = java.sql.Date.valueOf("1972-10-19");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(birthday);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(birthday));
    assertTrue(result.getType().equals(TemplateMergeFieldType.DATE));
  }

  @Test
  public void TemplateMergeField_generated_with_int_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(25);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(25));
    assertTrue(result.getType().equals(TemplateMergeFieldType.NUMBER));
  }

  @Test
  public void TemplateMergeField_generated_with_long_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(2589l);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(2589l));
    assertTrue(result.getType().equals(TemplateMergeFieldType.NUMBER));
  }

  @Test
  public void TemplateMergeField_generated_with_double_as_value_is_from_type_NUMBER() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(28.66);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(28.66));
    assertTrue(result.getType().equals(TemplateMergeFieldType.NUMBER));
  }

  @Test
  public void TemplateMergeField_generated_with_double_as_value_hasNoChildren() {

    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(28.66);

    assertTrue(!result.hasChildren());
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_is_from_type_Collection() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(hobbies));
    assertTrue(result.getType().equals(TemplateMergeFieldType.COLLECTION));
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_hasChildren() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.hasChildren());
  }

  @Test
  public void TemplateMergeField_generated_with_List_as_value_has_same_number_of_children_than_list_size() {
    java.util.List<String> hobbies = new ArrayList<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);
    assertTrue(result.getChildren().size() == hobbies.size());
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_is_from_type_Collection() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(hobbies));
    assertTrue(result.getType().equals(TemplateMergeFieldType.COLLECTION));
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_hasChildren() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.hasChildren());
  }

  @Test
  public void TemplateMergeField_generated_with_IvyList_as_value_has_same_number_of_children_than_list_size() {
    List<String> hobbies = List.create(String.class);
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.getChildren().size() == hobbies.size());
  }

  @Test
  public void TemplateMergeField_generated_with_Set_as_value_is_from_type_Collection() {
    java.util.Set<String> hobbies = new HashSet<>();
    hobbies.add("hobbie1");
    hobbies.add("hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(hobbies));
    assertTrue(result.getType().equals(TemplateMergeFieldType.COLLECTION));
  }

  @Test
  public void TemplateMergeField_generated_with_Map_as_value_is_from_type_Collection() {
    java.util.Map<Integer, String> hobbies = new HashMap<>();
    hobbies.put(1, "hobbie1");
    hobbies.put(2, "hobbie2");
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(hobbies);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(hobbies));
    assertTrue(result.getType().equals(TemplateMergeFieldType.COLLECTION));
  }

  @Test
  public void TemplateMergeField_generated_with_Collection_of_beans_has_children() {
    Collection<Person> persons = makePersons();
    TemplateMergeField result = TemplateMergeField.withName("aName").withValue(persons);

    assertTrue(result.getMergeFieldName().equals("aName"));
    assertTrue(result.getValue().equals(persons));
    assertTrue(result.getType().equals(TemplateMergeFieldType.COLLECTION));
    for (TemplateMergeField tmf : result.getChildren()) {
      System.out.println(tmf.getMergeFieldName() + " " + tmf.getMergeFieldValue());
    }
    System.out.println(result.getChildren().size());
    assertTrue(result.hasChildren());
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
