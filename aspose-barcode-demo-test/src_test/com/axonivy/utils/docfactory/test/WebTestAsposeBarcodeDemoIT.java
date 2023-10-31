package com.axonivy.utils.docfactory.test;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;

@IvyWebTest(headless = true)
public class WebTestAsposeBarcodeDemoIT{

  private static final String BARCODE_STRING = "form:str";
  private static final String PROCEED_BUTTON = "form:proceed";
  private static final String BARCODE_IMAGE = "code_128";

  @Test
  public void createBarcodePage(){
    open(EngineUrl.createProcessUrl("AsposeBarcodeDemo/16CD7829EF6B489B/start.ivp"));

    $(By.id(BARCODE_STRING)).shouldBe(empty);
    $(By.id(BARCODE_STRING)).sendKeys(String.valueOf("Hello Axon Ivy"));
    $(By.id(PROCEED_BUTTON)).shouldBe(visible).click();

    $(By.id(BARCODE_IMAGE)).shouldBe(visible).isImage();
  }

}