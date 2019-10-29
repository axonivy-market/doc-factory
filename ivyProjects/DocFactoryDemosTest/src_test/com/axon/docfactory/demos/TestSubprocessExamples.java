package com.axon.docfactory.demos;

import static com.axon.test.DocResponse.contentType;
import static com.axon.test.DocResponse.getDocLink;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

import com.axon.test.DocType;
import com.axon.test.EngineUrl;
import com.axon.test.SimpleHttpClient;

public class TestSubprocessExamples
{
  private final String suprocessExamples = EngineUrl.process()+"/DocFactoryDemos/160D67F5A5647B10";

  private final SimpleHttpClient http = new SimpleHttpClient();
  
  @Test
  public void docWithTemplateMergeFields() throws Exception
  {
    HttpResponse<String> response = http.request(suprocessExamples+"/start1.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithMergeFields.docx");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.DOCX);
  }
  
  @Test
  public void docWithCompositeObjData() throws Exception
  {
    HttpResponse<String> response = http.request(suprocessExamples+"/start2.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithObjectData.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }
  
  @Test
  public void docWithTable() throws Exception
  {
    HttpResponse<String> response = http.request(suprocessExamples+"/start3.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithTable.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }
  
  @Test
  public void docWithConditionalText() throws Exception
  {
    HttpResponse<String> response = http.request(suprocessExamples+"/start4.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithConditionalText.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }

  @Test
  public void docWithNestedTable() throws Exception
  {
    HttpResponse<String> response = http.request(suprocessExamples+"/start5.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithNestedTables.pdf");
    assertThat(contentType(http.head(docLink))).isEqualTo(DocType.PDF);
  }
  
}
