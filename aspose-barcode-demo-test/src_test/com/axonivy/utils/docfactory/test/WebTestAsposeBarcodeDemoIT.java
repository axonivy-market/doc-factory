package com.axonivy.utils.docfactory.test;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;

@IvyWebTest(headless = true)
public class WebTestAsposeBarcodeDemoIT{

  @Test
  public void createBarcodePage(){
    open(EngineUrl.createProcessUrl("AsposeBarcodeDemo/16CD7829EF6B489B/start.ivp"));

    $(By.cssSelector("[id$='str']")).shouldBe(empty);
    $(By.cssSelector("[id$='str']")).sendKeys(String.valueOf("Hello Axon Ivy"));
    $(By.cssSelector("[id$='proceed']")).shouldBe(visible).click();
    $(By.cssSelector("[id$='code_128']")).shouldBe(visible, Duration.ofSeconds(30)).isImage();
  }

}