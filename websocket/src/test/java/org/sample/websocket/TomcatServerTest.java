package org.sample.websocket;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 4:05 PM
 */
@SuppressWarnings("all")
@Slf4j
class TomcatServerTest {

  public static final String TOMCAT_SERVER_STOP_CALLED = "tomcatServer.stop() called.";

  @Test
  void withoutServlet() throws LifecycleException, IOException, InterruptedException {
    TomcatServer tomcatServer = new TomcatServer(8081);
    tomcatServer.start();

    //Send test http request
    // Create an HttpClient instance
    HttpClient client = HttpClient.newHttpClient();

    // Create an HttpRequest
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8081"))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(404);

    Awaitility.await()
        .timeout(Duration.ofSeconds(11))
        .pollDelay(Duration.ofSeconds(10))
        .untilAsserted(() -> {
          tomcatServer.stop();
          log.warn(TOMCAT_SERVER_STOP_CALLED);
        });

    //blocking join until close is called.
    tomcatServer.join();
  }

  @Test
  void withServlet() throws LifecycleException, IOException, InterruptedException {
    TomcatServer tomcatServer = new TomcatServer(8080, true);
    tomcatServer.start();

    //Send test http request
    // Create an HttpClient instance
    HttpClient client = HttpClient.newHttpClient();

    // Create an HttpRequest
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/welcome"))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).contains("Have a great day !!!");

    //async await
    Awaitility.await()
        .timeout(Duration.ofSeconds(5))
        .pollDelay(Duration.ofSeconds(4))
        .untilAsserted(() -> {
          tomcatServer.stop();
          log.warn(TOMCAT_SERVER_STOP_CALLED);
        });

    //blocking join until close is called.
    tomcatServer.join();
  }

}