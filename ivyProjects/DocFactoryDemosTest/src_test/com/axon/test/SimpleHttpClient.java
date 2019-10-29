package com.axon.test;

import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class SimpleHttpClient
{
  private final HttpClient client = HttpClient.newBuilder()
          .followRedirects(Redirect.NORMAL)
          .cookieHandler(new CookieManager())
          .build();
  
  public HttpResponse<String> request(String uri) throws Exception
  {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
    return client.send(request, BodyHandlers.ofString());
  }
  
  public HttpResponse<Void> head(String fileLink) throws Exception
  {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(fileLink))
            .method("HEAD", BodyPublishers.noBody())
            .build();
    return client.send(request, BodyHandlers.discarding());
  }
}