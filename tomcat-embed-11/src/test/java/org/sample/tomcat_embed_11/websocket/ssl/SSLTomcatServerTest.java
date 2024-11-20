package org.sample.tomcat_embed_11.websocket.ssl;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/20/24 6:57 PM
 */
@Slf4j
class SSLTomcatServerTest {

  private static final String TOMCAT_SERVER_STOP_MESSAGE = "tomcat.stop() called.";

  @Test
  void TomcatWithSSl() throws ConfigurationException, LifecycleException {
    SSLConnectorProperties connectorProperties =
        YamlConfigurations.load(SSLConnectorProperties.class, "/ssl/connector.yaml");

    SSLTomcatServer sslTomcatServer = new SSLTomcatServer(connectorProperties);
    sslTomcatServer.start();

    Awaitility.await()
        .timeout(Duration.ofSeconds(24))
        .pollDelay(Duration.ofSeconds(23))
        .untilAsserted(() -> {
          sslTomcatServer.stop();
          log.warn(TOMCAT_SERVER_STOP_MESSAGE);
        });

    //blocking join until close is called.
    sslTomcatServer.join();

  }

}