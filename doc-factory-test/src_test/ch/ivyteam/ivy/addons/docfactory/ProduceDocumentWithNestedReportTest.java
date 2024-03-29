package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.test.data.Address;
import ch.ivyteam.ivy.addons.docfactory.test.data.Insurance;
import ch.ivyteam.ivy.addons.docfactory.test.data.InsuranceBasket;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class ProduceDocumentWithNestedReportTest {

  @Test
  public void produceDocument_for_reporting_with_nested_tables() throws Exception {
    File template = new File(
            this.getClass().getResource("resources/template_with_nested_tables.docx").toURI().getPath());

    InputData data = new InputData();
    data.setCreationDate(Calendar.getInstance().getTime());
    data.setComment("Please take care of checking this dossier");
    data.setUID("fd2g1-ertr5-55466-www25");

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForMailMerge(data).useLocale(Locale.forLanguageTag("de-ch"));

    FileOperationMessage result = null;
    File resultFile = new File("test/nestedTables.pdf");
    try {
      if (resultFile.isFile()) {
        resultFile.delete();
      }
      result = documentTemplate.produceDocument(resultFile);
    } catch (Exception ex) {
      System.out.println("Exception : " + ex.toString());
    }

	assertThat(result).isNotNull();
	assertThat(result.isSuccess()).isTrue();
	assertThat(result.getFiles()).contains(resultFile);
  }

  public class InputData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String UID;
    private String comment;
    private Date creationDate;

    Collection<Person> persons = makePersons();

    public String getUID() {
      return UID;
    }

    public void setUID(String uID) {
      UID = uID;
    }

    public String getComment() {
      return comment;
    }

    public void setComment(String comment) {
      this.comment = comment;
    }

    public Date getCreationDate() {
      return creationDate;
    }

    public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
    }

    public Collection<Person> getPersons() {
      return persons;
    }

    public void setPersons(Collection<Person> persons) {
      this.persons = persons;
    }
  }

  private Collection<Person> makePersons() {
    Collection<Person> persons = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    cal.set(1988, 9, 19);
    persons.add(
            Person.withNameFirstname("Comba", "Emmanuel")
                    .withAddress(Address.withStreetZipCodeCityCountry("Muristrasse 4", "3005", "Bern", "CH"))
                    .withBirthday(cal.getTime())
                    .withInsuranceBasket(
                            InsuranceBasket
                                    .withInsurance(Insurance.withName("AXA").withContractNumber("3458796")
                                            .withLocation(Address.withStreetZipCodeCityCountry(
                                                    "Geilestrasse 4", "3000", "Bern", "CH"))
                                            .withLocation(Address.withStreetZipCodeCityCountry("Hauptsitz 4",
                                                    "3005", "Z�rich", "CH")))
                                    .putInsurance(
                                            Insurance.withName("GENERALI").withContractNumber("fsghdg6666")
                                                    .withLocation(Address.withStreetZipCodeCityCountry(
                                                            "Am Seehof 45", "3003", "Bern", "CH")))
                                    .putInsurance(Insurance.withName("AB").withContractNumber("23445656")
                                            .withLocation(Address.withStreetZipCodeCityCountry("AB 8", "3006",
                                                    "Bern", "CH"))))
                    .withId(new BigDecimal(213546)));
    cal.set(1990, 8, 22);
    persons.add(
            Person.withNameFirstname("Tuchel", "Lars")
                    .withAddress(Address.withStreetZipCodeCityCountry("Seegasse 4", "4534", "Biel", "CH"))
                    .withBirthday(cal.getTime())
                    .withInsuranceBasket(
                            InsuranceBasket
                                    .withInsurance(
                                            Insurance.withName("Previfrance").withContractNumber("88895656")
                                                    .withLocation(Address.withStreetZipCodeCityCountry(
                                                            "Au bord du canal", "31200", "Toulouse", "FR"))
                                                    .withLocation(Address.withStreetZipCodeCityCountry(
                                                            "B�rsenstrasse 26", "8001", "Z�rich", "CH")))
                                    .putInsurance(Insurance.withName("AB").withContractNumber("51235431")
                                            .withLocation(Address.withStreetZipCodeCityCountry("AB 8", "3006",
                                                    "Bern", "CH"))))
                    .withId(new BigDecimal(444458)));

    return persons;
  }

}
