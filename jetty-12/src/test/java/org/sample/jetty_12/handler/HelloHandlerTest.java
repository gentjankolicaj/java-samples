package org.sample.jetty_12.handler;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.net.ssl.SSLContext;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyHandlerWrapper;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.SSLTest;

/**
 * @author gentjan kolicaj
 * @Date: 11/22/24 2:18 PM
 */
class HelloHandlerTest extends SSLTest {


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
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }


  @Test
  void jettyHttpsYaml() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_https.yaml");
    JettyServer jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        new JettyHandlerWrapper(new HelloHandler("hello world"), "/"));

    //start jetty server
    jettyServer.start();

    // Create a custom SSLContext that trusts all certificates
    SSLContext sslContext = createSSLContext(DUMMY_TRUST_MANAGER);

    // Build the HttpClient with the custom SSLContext
    HttpClient httpsClient = HttpClient.newBuilder()
        .sslContext(sslContext)
        .build();

    String scheme = "https";
    String host = "127.0.0.1";
    int port = 8443;
    String path = "/";

    // Create a https/1.1 request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_2)
        .build();
    HttpResponse<String> response = httpsClient.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(400);
    assertThat(response.version()).isEqualTo(Version.HTTP_2);

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