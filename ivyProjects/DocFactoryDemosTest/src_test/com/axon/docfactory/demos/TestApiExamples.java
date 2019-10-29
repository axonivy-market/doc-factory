package com.axon.docfactory.demos;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ws.rs.core.HttpHeaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import com.axon.test.EngineUrl;

public class TestApiExamples
{
  private final HttpClient client = HttpClient.newBuilder()
          .followRedirects(Redirect.NORMAL)
          .cookieHandler(new CookieManager())
          .build();
  
  private final String docFactoryApiExamples = EngineUrl.process()+"/DocFactoryDemos/16B45CBCE0D2056C";

  @Test
  public void docWithCompositeObj() throws Exception
  {
    HttpResponse<String> response = request(docFactoryApiExamples+"/start6.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithCompositeObject.pdf");
    assertThat(contentType(checkFile(docLink))).isEqualTo("application/pdf");
  }
  
  @Test
  public void docWithNestedTablesPDF() throws Exception
  {
    HttpResponse<String> response = request(docFactoryApiExamples+"/start3.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.pdf");
    assertThat(contentType(checkFile(docLink))).isEqualTo("application/pdf");
  }
  
  @Test
  public void docWithNestedTablesDOCX() throws Exception
  {
    HttpResponse<String> response = request(docFactoryApiExamples+"/start4.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.docx");
    assertThat(contentType(checkFile(docLink)))
      .isEqualTo("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
  }
  
  @Test
  public void docWithNestedTablesHTML() throws Exception
  {
    HttpResponse<String> response = request(docFactoryApiExamples+"/start5.ivp");
    String docLink = getDocLink(response);
    assertThat(docLink).endsWith("DocWithFullNestedTables.html");
    assertThat(contentType(checkFile(docLink))).isEqualTo("text/html");
  }

  private static String getDocLink(HttpResponse<String> response) 
  {
    assertThat(response.statusCode())
      .as(response.toString()+"\n"+response.body())
      .isEqualTo(200);
    Element result = Jsoup.parse(response.body()).getElementById("docLink");
    return result.getElementsByTag("a").attr("href");
  }
  
  private static String contentType(HttpResponse<?> response)
  {
    return response.headers().firstValue(HttpHeaders.CONTENT_TYPE).get();
  }
  
  private HttpResponse<String> request(String uri) throws Exception
  {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(uri))
      .build();
    return client.send(request, BodyHandlers.ofString());
  }
  
  private HttpResponse<Void> checkFile(String fileLink) throws Exception
  {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(fileLink))
      .method("HEAD", BodyPublishers.noBody())
      .build();
    return client.send(request, BodyHandlers.discarding());
  }
  
}
