package com.axonivy.utils.docfactory.test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestAsposeEmailDemo
{

  @BeforeAll
  public static void setUp()
  {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Selenide.open();
  }

  @Test
  public void testMsgEmail() throws Exception
  {
	open(EngineUrl.createProcessUrl("AsposeEmailDemo/1712BF5507F25F15/start.ivp"));
    File msg = $(By.id("form:createBtn")).shouldBe(visible).download();

    Awaitility.await().untilAsserted(() -> {
      assertThat(msg).exists();
      assertThat(msg).isFile();
    });
  }

}
