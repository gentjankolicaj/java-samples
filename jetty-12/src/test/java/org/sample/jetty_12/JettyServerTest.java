package org.sample.jetty_12;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 10:47 PM
 */
@Slf4j
class JettyServerTest {


  @Test
  void jettyYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class, "/jetty.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        (ContextHandlerCollection) null);

    jettyServer.start();

    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @Test
  void jettySSLYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_ssl.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        (ContextHandlerCollection) null);

    jettyServer.start();

    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
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
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @Test
  void jettyHttpVersionsYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_http_versions.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        (ContextHandlerCollection) null);

    jettyServer.start();

    // Create a HttpClient instance
    HttpClient client = HttpClient.newHttpClient();
    String scheme = "http";
    String host = "localhost";
    int port = 8081;
    String path = "";

    // Create a http/1.1 request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(404);
    assertThat(response.version()).isEqualTo(Version.HTTP_1_1);

    // Create a http/2 request
    //change port because http2 is on different port & connector
    port = 8082;
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_2)
        .build();
    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    assertThat(response2.statusCode()).isEqualTo(404);
    assertThat(response2.version()).isEqualTo(Version.HTTP_2);

    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @Test
  void jettyHttpsVersionsYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_https_versions.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        (ContextHandlerCollection) null);

    jettyServer.start();

    // Create a HttpClient instance
    HttpClient client = HttpClient.newHttpClient();
    String scheme = "https";
    String host = "localhost";
    int port = 8444;
    String path = "";

    // Create a http/1.1 request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(404);
    assertThat(response.version()).isEqualTo(Version.HTTP_1_1);

    // Create a http/2 request
    //change port because http2 is on different port & connector
    port = 8445;
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_2)
        .build();
    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    assertThat(response2.statusCode()).isEqualTo(404);
    assertThat(response2.version()).isEqualTo(Version.HTTP_2);

    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }


}