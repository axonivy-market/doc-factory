package com.axon.docfactory.demos;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest(headless = true)
class WebTestApiExamplesIT {
  private final String DOC_DEMOS_BASE = "/DocFactoryDemos/16B45CBCE0D2056C/";

  @BeforeAll
  static void setup(@TempDir Path tempDir) {
    Configuration.timeout = 10000;
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Configuration.downloadsFolder = tempDir.toString();
  }

  @Test
  void docWithCompositeObjPdf() throws Exception {
    assertDownload("start6.ivp", "DocWithCompositeObject.pdf");
  }

  @Test
  void docWithCompositeObjPdfA() throws Exception {
    assertDownload("start8.ivp", "DocWithCompositeObjectA.pdf");
  }

  @Test
  void docWithNestedTablesPDF() throws Exception {
    assertDownload("start3.ivp", "DocWithFullNestedTables.pdf");
  }

  @Test
  void docWithNestedTablesDOCX() throws Exception {
    assertDownload("start4.ivp", "DocWithFullNestedTables.docx");
  }

  @Test
  void docWithNestedTablesHTML() throws Exception {
    assertDownload("start5.ivp", "DocWithFullNestedTables.html");
  }

  @Test
  void zipMultipleDocuments() throws Exception {
    open(EngineUrl.createProcessUrl("/DocFactoryDemos/16CD7829EF6B489B/start2.ivp"));
    var doc = Selenide.$$("button").find(exactText("Create Multiple Formats")).shouldBe(visible).download();
    assertThat(doc).hasName("Documents.zip");
    assertThat(doc.length() / 1024).isGreaterThan(100);
  }

  @Test
  void ivyDocApi(WebAppFixture fixture) {
    if (!EngineUrl.isDesigner()) {
      fixture.login("test", "test");
    }
    fixture.config("StandardProcess.DefaultPages", "ch.ivyteam.workflow:dev-workflow-ui");

    open(EngineUrl.createProcessUrl("/DocFactoryDemos/16DFD8AB2E4BFFF9/start2.ivp"));
    $(By.id("form:name")).shouldBe(visible).sendKeys("Batman");
    $("button").click();
    $(withText("Task End")).shouldBe(visible);

    open(EngineUrl.create().path("tasks").toUrl());
    $(By.linkText("Task: View attached document")).shouldBe(visible).click();
    Selenide.switchTo().frame("iFrame");
    $("h3").shouldHave(exactText("DocFactoryDemos: Attached Document"));
    $("iframe").shouldBe(visible);
  }

  private void assertDownload(String process, String expectedFileName) throws Exception {
    open(EngineUrl.createProcessUrl(DOC_DEMOS_BASE + process));
    var doc = $("#docLink").shouldBe(visible).download();
    assertThat(doc).hasName(expectedFileName);
    assertThat(doc.length() / 1024).isGreaterThan(8);
  }
}
