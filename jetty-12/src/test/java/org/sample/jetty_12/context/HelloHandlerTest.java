package org.sample.jetty_12.context;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyHandlerWrapper;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;

/**
 * @author gentjan kolicaj
 * @Date: 11/22/24 2:18 PM
 */
class HelloHandlerTest {


  @Test
  void jettyYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class, "/jetty.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        new JettyHandlerWrapper(new HelloHandler("hello world"), "/"));

    //start server
    jettyServer.start();

    // Create a HttpClient instance
    HttpClient client = HttpClient.newHttpClient();
    String scheme = "http";
    String host = "localhost";
    int port = 8080;
    String path = "/";

    // Create a http request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).contains("hello world");

    Awaitility.await()
        .timeout(Duration.ofSeconds(4))
        .pollDelay(Duration.ofSeconds(3))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }


  @Test
  void jettySSLMultipleConnectorsYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_ssl_multiple_connectors.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        (ContextHandlerCollection) null);

    jettyServer.start();

    Awaitility.await()
        .timeout(Duration.ofSeconds(24))
        .pollDelay(Duration.ofSeconds(23))
        .untilAsserted(() -> {
          jettyServer.stop();
          ;
        });

    //blocking join until close is called.
    jettyServer.join();
  }

}