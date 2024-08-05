package com.axon.docfactory.demos;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DownloadOptions;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.files.FileFilters;

@IvyWebTest(headless = true)
class WebTestSubprocessExamplesIT {
  private final String DOC_DEMOS_BASE = "/DocFactoryDemos/160D67F5A5647B10/";

  @BeforeAll
  static void setup(@TempDir Path tempDir) {
    Configuration.timeout = 10000;
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Configuration.downloadsFolder = tempDir.toString();
  }

  @Test
  void docWithTemplateMergeFields() throws Exception {
    assertDownload("start1.ivp", "DocWithMergeFields.docx");
  }

  @Test
  void docWithCompositeObjData() throws Exception {
    assertDownload("start2.ivp", "DocWithObjectData.pdf");
  }

  @Test
  void docWithTable() throws Exception {
    assertDownload("start3.ivp", "DocWithTable.pdf");
  }

  @Test
  void docWithConditionalText() throws Exception {
    assertDownload("start4.ivp", "DocWithConditionalText.pdf");
  }

  @Test
  void docWithNestedTable() throws Exception {
    assertDownload("start5.ivp", "DocWithNestedTables.pdf");
  }

  @Test
  void docWithNestedObject() throws Exception {
    assertDownload("start6.ivp", "DocWithNestedObject.pdf");
  }

  private void assertDownload(String process, String expectedFileName) throws Exception {
    open(EngineUrl.createProcessUrl(DOC_DEMOS_BASE + process));
    var doc = $("#docLink").shouldBe(visible).download(DownloadOptions.using(FileDownloadMode.PROXY)
            .withTimeout(Duration.ofSeconds(10))
            .withFilter(FileFilters.withName(expectedFileName)));

    assertThat(doc).hasName(expectedFileName);
    assertThat(doc.length() / 1024).isGreaterThan(15);
  }

}
