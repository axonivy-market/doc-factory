package com.axon.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;

import javax.ws.rs.core.HttpHeaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class DocResponse
{
  
  public static String getDocLink(HttpResponse<String> response) 
  {
    assertThat(response.statusCode())
    .as(response.toString()+"\n"+response.body())
    .isEqualTo(200);
    Element result = Jsoup.parse(response.body()).getElementById("docLink");
    return result.getElementsByTag("a").attr("href");
  }
  
  public static String contentType(HttpResponse<?> response)
  {
    return response.headers().firstValue(HttpHeaders.CONTENT_TYPE).get();
  }
}