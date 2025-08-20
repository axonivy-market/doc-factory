package com.axonivy.utils.docfactory.test;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.WebDriverRunner;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@IvyWebTest
public class WebTestAsposeBarcodeDemoIT{

  @Test
  public void createBarcodePage(){
    open(EngineUrl.createProcessUrl("AsposeBarcodeDemo/16CD7829EF6B489B/start.ivp"));

    $(By.cssSelector("[id$='str']")).shouldBe(empty);
    $(By.cssSelector("[id$='str']")).sendKeys(String.valueOf("Hello Axon Ivy"));
    $(By.cssSelector("[id$='proceed']")).shouldBe(visible).click();
    new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(30))
    .until(ExpectedConditions.urlContains("BarCode.xhtml"));
    switchTo().window(0);
    $("body").shouldHave(text("CODE_128"), Duration.ofSeconds(200));
    $(By.cssSelector("[id$='code_12']")).shouldBe(visible, Duration.ofSeconds(200));
  }
}