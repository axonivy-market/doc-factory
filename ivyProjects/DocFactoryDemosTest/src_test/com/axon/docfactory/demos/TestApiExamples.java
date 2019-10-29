package com.axon.docfactory.demos;

import static com.axon.test.DocResponse.contentType;
import static com.axon.test.DocResponse.getDocLink;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

import com.axon.test.DocType;
import com.axon.test.EngineUrl;
import com.axon.test.SimpleHttpClient;

public class TestApiExamples
{
  private final String docFactoryApiExamples = EngineUrl.process()+"/DocFactoryDemos/16B45CBCE0D2056C";

  private final SimpleHttpClient http = new SimpleHttpClient();
  
  @Test
  public void docWithCompositeObj() throws Exception
  {
    HttpResponse<String> response = http.request(docFactoryApiExamples+"/start6.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithCompositeObject.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }
  
  @Test
  public void docWithNestedTablesPDF() throws Exception
  {
    HttpResponse<String> response = http.request(docFactoryApiExamples+"/start3.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }
  
  @Test
  public void docWithNestedTablesDOCX() throws Exception
  {
    HttpResponse<String> response = http.request(docFactoryApiExamples+"/start4.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.docx");
    assertThat(contentType(http.head(docLink)))
      .isEqualTo(DocType.DOCX);
  }
  
  @Test
  public void docWithNestedTablesHTML() throws Exception
  {
    HttpResponse<String> response = http.request(docFactoryApiExamples+"/start5.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.html");
    assertThat(contentType(http.head(docLink))).isEqualTo("text/html");
  }
}
