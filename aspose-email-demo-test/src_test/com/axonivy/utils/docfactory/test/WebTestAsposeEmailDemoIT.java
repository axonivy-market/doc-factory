package com.axonivy.utils.docfactory.test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DownloadOptions;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

@IvyWebTest
public class WebTestAsposeEmailDemoIT {

  @BeforeAll
  public static void setUp() {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Selenide.open();
  }

  @Test
  public void testMsgEmail() throws Exception {
    open(EngineUrl.createProcessUrl("AsposeEmailDemo/1712BF5507F25F15/start.ivp"));
    String html = WebDriverRunner.getWebDriver().getPageSource();
    System.out.println("✅ PAGE SOURCE:\n" + html);
    $(By.cssSelector("[id$='customer']")).shouldBe(visible, Duration.ofSeconds(30));
    var creator = $(By.cssSelector("[id$='createBtn']")).shouldBe(visible, Duration.ofSeconds(30));
    File msg = creator.download(DownloadOptions.using(FileDownloadMode.PROXY).withTimeout(Duration.ofSeconds(30)));

    Awaitility.await().untilAsserted(() -> {
      assertThat(msg).exists();
      assertThat(msg).isFile();
    });
  }

}
