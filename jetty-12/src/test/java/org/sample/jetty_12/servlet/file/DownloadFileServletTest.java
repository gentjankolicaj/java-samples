package org.sample.jetty_12.servlet.file;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
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
 * @Date: 11/27/24 8:57 PM
 */
class DownloadFileServletTest {

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
    servletContextHandler.addServlet(DownloadFileServlet.class, DownloadFileServlet.SERVLET_PATH);
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
    String path = DownloadFileServlet.SERVLET_PATH;

    // Create a http/1.1 request
    HttpRequest request1 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .build();
    HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
    assertThat(response1.statusCode()).isEqualTo(200);

    // File download test
    //=============================================================================================
    //read file for asserting
    byte[] fileBytes;
    try (FileInputStream fis = new FileInputStream(ReadFileServlet.getServerFile())) {
      fileBytes = fis.readAllBytes();
    }
    // Create an valid http request
    HttpRequest request2 = HttpRequest.newBuilder()
        .uri(URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)))
        .GET()
        .build();
    HttpResponse<byte[]> response2 = client.send(request2, HttpResponse.BodyHandlers.ofByteArray());
    assertThat(response2.statusCode()).isEqualTo(200);
    assertThat(response2.body()).isNotEmpty();
    assertThat(response2.body()).contains(fileBytes);

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