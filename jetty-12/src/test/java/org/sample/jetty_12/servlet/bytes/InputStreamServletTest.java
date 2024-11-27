package org.sample.jetty_12.servlet.bytes;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.servlet.ErrorServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 8:15 PM
 */
class InputStreamServletTest {

  JettyServer jettyServer;

  @Test
  void servlet() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_http_versions.yaml");

    // Setup the basic application "context" for this application at "/"
    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler servletContextHandler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    servletContextHandler.setContextPath("/");

    //add servlets
    servletContextHandler.addServlet(InputStreamServlet.class, InputStreamServlet.SERVLET_PATH);
    servletContextHandler.addServlet(ErrorServlet.class, ErrorServlet.SERVLET_PATH);

    // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
    servletContextHandler.addServlet(DefaultServlet.class, "/");

    //add error handler to servlet context
    ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
    errorHandler.addErrorPage(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE,
        ErrorServlet.SERVLET_PATH);
    servletContextHandler.setErrorHandler(errorHandler);

    //jetty server creation
    jettyServer = new JettyServer(jettyProperties.getJettyServer(),
        servletContextHandler);
    jettyServer.start();

    // Create a HttpClient instance
    HttpClient client = HttpClient.newHttpClient();
    String scheme = "http";
    String host = "localhost";
    int port = 8081;
    String path = InputStreamServlet.SERVLET_PATH;

    // Create a http/1.1 request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .version(Version.HTTP_1_1)
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.version()).isEqualTo(Version.HTTP_1_1);
    assertThat(response.body()).contains(InputStreamServlet.class.getSimpleName());
    assertThat(response.body()).contains("classname");

    // Create a http/2 request
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
    assertThat(response2.body()).contains(InputStreamServlet.class.getSimpleName());
    assertThat(response2.body()).contains("classname");

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