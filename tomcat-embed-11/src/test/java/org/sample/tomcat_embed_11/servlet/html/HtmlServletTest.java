package org.sample.tomcat_embed_11.servlet.html;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.sample.tomcat_embed_11.servlet.ConnectorProperties;
import org.sample.tomcat_embed_11.servlet.TomcatServer;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 4:05 PM
 */
@SuppressWarnings("all")
@Slf4j
class HtmlServletTest {

  public static final String TOMCAT_SERVER_STOP_CALLED = "tomcatServer.stop() called.";

  @Test
  void test()
      throws LifecycleException, IOException, InterruptedException, ConfigurationException {
    ConnectorProperties props = YamlConfigurations.load(ConnectorProperties.class,
        "/servlet/connector.yaml");
    props.setPort(8001);

    TomcatServer tomcatServer = new TomcatServer(props, List.of(new HtmlServlet()));
    tomcatServer.start();

    // Create an HttpClient instance
    HttpClient client = HttpClient.newHttpClient();

    // Create an dummy http request
    HttpRequest request1 = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8001"))
        .GET()
        .build();
    HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
    assertThat(response1.statusCode()).isEqualTo(404);
    assertThat(response1.body()).contains(
        "The origin server did not find a current representation for the target resource or is not willing to disclose that one exists.");

    // Create an valid http request
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8001/html"))
        .GET()
        .build();
    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    assertThat(response2.statusCode()).isEqualTo(200);
    assertThat(response2.body()).contains("HtmlServlet");
    assertThat(response2.body()).contains("Classname");

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