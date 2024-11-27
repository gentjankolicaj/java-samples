package org.sample.jetty_12.filter;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.servlet.DispatcherType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.EnumSet;
import org.awaitility.Awaitility;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.servlet.ErrorServlet;
import org.sample.jetty_12.servlet.HelloWorldServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 9:32 PM
 */
class ApiKeyHeaderFilterTest {


  JettyServer jettyServer;

  @Test
  void servletWithFilter() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_http_versions.yaml");

    // Setup the basic application "context" for this application at "/"
    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler servletContextHandler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    servletContextHandler.setContextPath("/");

    //add servlets
    servletContextHandler.addServlet(HelloWorldServlet.class, HelloWorldServlet.SERVLET_PATH);
    servletContextHandler.addServlet(ErrorServlet.class, ErrorServlet.SERVLET_PATH);

    // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
    servletContextHandler.addServlet(DefaultServlet.class, "/");

    //Add servlet filter
    EnumSet<DispatcherType> dispatches = EnumSet.allOf(DispatcherType.class);
    servletContextHandler.addFilter(ApiKeyFilter.class, HelloWorldServlet.SERVLET_PATH, dispatches);

    //============================================================================
    //jetty server creation
    jettyServer = new JettyServer(jettyProperties.getJettyServer(), servletContextHandler);
    jettyServer.start();

    // Create a HttpClient instance
    HttpClient client = HttpClient.newHttpClient();
    String scheme = "http";
    String host = "localhost";
    int port = 8081;
    String path = HelloWorldServlet.SERVLET_PATH;

    // Create a http/1.1 request without X-API-KEY header
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.version()).isEqualTo(Version.HTTP_1_1);
    assertThat(response.headers().allValues(ApiKeyFilter.CUSTOM_JETTY_HEADER_KEY)).contains(
        "not-authenticated");

    // Create a http/2 request without X-API-KEY header
    //change port because http2 is on different port & connector
    port = 8082;
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_2)
        .build();
    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    assertThat(response2.statusCode()).isEqualTo(200);
    assertThat(response2.version()).isEqualTo(Version.HTTP_2);
    assertThat(response.headers().allValues(ApiKeyFilter.CUSTOM_JETTY_HEADER_KEY)).contains(
        "not-authenticated");

    // Create a http/1.1 request without X-API-KEY header
    HttpRequest request3 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .header(ApiKeyFilter.CUSTOM_API_KEY_HEADER_KEY, "")
        .build();
    HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
    assertThat(response3.statusCode()).isEqualTo(200);
    assertThat(response3.version()).isEqualTo(Version.HTTP_1_1);
    assertThat(response3.headers().allValues(ApiKeyFilter.CUSTOM_JETTY_HEADER_KEY)).contains(
        "not-authenticated");

    // Create a http/1.1 request with X-API-KEY header
    HttpRequest request4 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .header(ApiKeyFilter.CUSTOM_API_KEY_HEADER_KEY, "101-key")
        .build();
    HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
    assertThat(response4.statusCode()).isEqualTo(200);
    assertThat(response4.version()).isEqualTo(Version.HTTP_1_1);
    assertThat(response4.headers().allValues(ApiKeyFilter.CUSTOM_JETTY_HEADER_KEY)).contains(
        "authenticated");

    // Create a http/2 request with X-API-KEY header
    //change port because http2 is on different port & connector
    port = 8082;
    HttpRequest request5 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_2)
        .header(ApiKeyFilter.CUSTOM_API_KEY_HEADER_KEY, "101-key")
        .build();
    HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
    assertThat(response5.statusCode()).isEqualTo(200);
    assertThat(response5.version()).isEqualTo(Version.HTTP_2);
    assertThat(response5.headers().allValues(ApiKeyFilter.CUSTOM_JETTY_HEADER_KEY)).contains(
        "authenticated");

    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @AfterEach
  void testClean() throws Exception {
    jettyServer.stop();
  }

}