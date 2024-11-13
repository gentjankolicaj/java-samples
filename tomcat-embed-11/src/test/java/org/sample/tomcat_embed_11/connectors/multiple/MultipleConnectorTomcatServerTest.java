package org.sample.tomcat_embed_11.connectors.multiple;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
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
 * @Date: 11/13/24 11:03 PM
 */
@Slf4j
class MultipleConnectorTomcatServerTest {

  public static final String TOMCAT_SERVER_STOP_CALLED = "tomcatServer.stop() called.";

  @Test
  void simpleTest()
      throws LifecycleException, IOException, InterruptedException, ConfigurationException {
    MultipleConnectorProperties connectorProperties = YamlConfigurations.load(
        MultipleConnectorProperties.class, "/connector/multiple_connector.yaml");

    MultipleConnectorTomcatServer tomcatServer = new MultipleConnectorTomcatServer(
        connectorProperties.getConnectors());
    tomcatServer.start();

    //Send test http request
    // Create an HttpClient instance
    HttpClient client = HttpClient.newHttpClient();

    // Create HttpRequest on port 8888
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8888"))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(404);
    assertThat(response.body()).contains(
        "The origin server did not find a current representation for the target resource or is not willing to disclose that one exists.");

    // Create HttpRequest on port 8888
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:9999"))
        .GET()
        .build();
    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    assertThat(response2.statusCode()).isEqualTo(404);
    assertThat(response2.body()).contains(
        "The origin server did not find a current representation for the target resource or is not willing to disclose that one exists.");

    Awaitility.await()
        .timeout(Duration.ofSeconds(6))
        .pollDelay(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          tomcatServer.stop();
          log.warn(TOMCAT_SERVER_STOP_CALLED);
        });

    //blocking join until close is called.
    tomcatServer.join();
  }


}