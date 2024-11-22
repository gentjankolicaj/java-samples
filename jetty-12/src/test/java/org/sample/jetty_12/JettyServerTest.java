package org.sample.jetty_12;

import io.jdev.jackson.YamlConfigurations;
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
        .timeout(Duration.ofSeconds(24))
        .pollDelay(Duration.ofSeconds(23))
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
        .timeout(Duration.ofSeconds(24))
        .pollDelay(Duration.ofSeconds(23))
        .untilAsserted(() -> {
          jettyServer.stop();
          ;
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