package ch.ivyteam.ivy.addons.docfactory;

import java.math.BigDecimal;
import java.util.Calendar;

import com.aspose.words.net.System.Data.DataRow;
import com.aspose.words.net.System.Data.DataTable;

import ch.ivyteam.ivy.addons.docfactory.test.data.Address;
import ch.ivyteam.ivy.addons.docfactory.test.data.Insurance;
import ch.ivyteam.ivy.addons.docfactory.test.data.InsuranceBasket;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;

public abstract class DocFactoryTest {

  protected static final String TEMPLATE_PERSON_DOCX = "resources/template_person.docx";
  protected static final String TEMPLATE_WITH_FIELDS_FORM_DOCX = "resources/template_with_field_form.docx";
  protected static final String TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX = "resources/template_for_testing_null_value.docx";
  protected static final String TEMPLATE_FOR_TESTING_LONG_EDITABLE_FIELDS_DOCX = "resources/template_with_long_editable_fields.docx";

  static protected java.io.File makeFile(String path) {
    java.io.File resultFile = new java.io.File(path);
    if (resultFile.isFile()) {
      resultFile.delete();
    }
    if (!resultFile.getParentFile().isDirectory()) {
      resultFile.getParentFile().mkdirs();
    }
    return resultFile;
  }

  static protected Person makePerson() {
    return Person.withNameFirstname("Comba", "Emmanuel")
            .withAddress(Address.withStreetZipCodeCityCountry("Muristrasse 4", "8000", "Z�rich", "CH"))
            .withBirthday(Calendar.getInstance().getTime())
            .withInsuranceBasket(
                    InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
                            .putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
                            .putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
            .withId(new BigDecimal(213546));
  }

  static protected Person makePersonWithHTML() {
    String htmlStreet = "Hey dude! This is my address:<br>"
            + "<b>My Street in bold</b>"
            + "<br /><font color='blue'>I am blue</font>"
            + "<br /><i><font color='red'>I am red and italic</font></i>"
            + "<br /><div style='color:green; font-family:courier;'>I am green and courier</div>"
            + "<a href='www.axonivy.com'>Come and visit us!</a><br />"
            + "<br /><ul><b>And a list:</b>"
            + "<li>one"
            + "<li>two"
            + "<li>three"
            + "</ul>";
    return Person.withNameFirstname("Comba", "Emmanuel")
            .withAddress(Address.withStreetZipCodeCityCountry(htmlStreet, "3005", "Bern", "CH"))
            .withBirthday(Calendar.getInstance().getTime())
            .withInsuranceBasket(
                    InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
                            .putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
                            .putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
            .withId(new BigDecimal(213546));
  }

  static protected Person makePersonWithHTMLFormattedInCssFile() {
    String htmlStreet = "<html>"
            + "<head>"
            + "<link rel='stylesheet' href='styles.css'>"
            + "</head>"
            + "<body>"
            + "<h1>The style is defined in a css file.</h1>"
            + "<div>This is a div with a background and a border.</div>"
            + "</body>"
            + "</html>";
    return Person.withNameFirstname("Comba", "Emmanuel")
            .withAddress(Address.withStreetZipCodeCityCountry(htmlStreet, "3005", "Bern", "CH"))
            .withBirthday(Calendar.getInstance().getTime())
            .withInsuranceBasket(
                    InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
                            .putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
                            .putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
            .withId(new BigDecimal(213546));
  }

  static protected DataTable makeDataTable() {
    // The name of the DataTable must be the same as the name of the merge field
    // region (TableStart:itemPrices)
    com.aspose.words.net.System.Data.DataTable data = new DataTable("itemPrices");
    // add the columns which names are the same as the merge fields in the data
    // table region
    data.getColumns().add("Item", String.class);
    data.getColumns().add("Price", Number.class);
    data.getColumns().add("Currency", String.class);
    // add the rows
    DataRow dr = data.newRow();
    dr.set("Item", "T-Shirt");
    dr.set("Price", 22.56);
    dr.set("Currency", "$");
    data.getRows().add(dr);
    dr = data.newRow();
    dr.set("Item", "Porsche");
    dr.set("Price", 435345);
    dr.set("Currency", "CHF");
    data.getRows().add(dr);
    dr = data.newRow();
    dr.set("Item", "EM-Ticket");
    dr.set("Price", 45);
    dr.set("Currency", "CHF");
    data.getRows().add(dr);
    dr = data.newRow();
    dr.set("Item", "Super PC");
    dr.set("Price", 1500.00);
    dr.set("Currency", "Euro");
    data.getRows().add(dr);

    return data;
  }

}
