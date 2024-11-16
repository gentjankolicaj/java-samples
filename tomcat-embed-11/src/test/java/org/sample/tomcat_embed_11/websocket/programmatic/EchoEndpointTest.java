package org.sample.tomcat_embed_11.websocket.programmatic;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.sample.tomcat_embed_11.websocket.ConnectorProperties;
import org.sample.tomcat_embed_11.websocket.ProgrammaticEndpoint;
import org.sample.tomcat_embed_11.websocket.TomcatServer;
import org.sample.tomcat_embed_11.websocket.TomcatWebSocketConfig;
import org.sample.tomcat_embed_11.websocket.WSTest;


/**
 * @author gentjan kolicaj
 * @Date: 11/16/24 10:01 PM
 */
@Slf4j
class EchoEndpointTest extends WSTest {

  private static final String TOMCAT_SERVER_STOP_MESSAGE = "tomcat.stop() called.";

  @Test
  void websocketTest() throws LifecycleException, ConfigurationException,
      DeploymentException, IOException {
    ConnectorProperties props = YamlConfigurations.load(ConnectorProperties.class,
        "/websocket/connector.yaml");

    TomcatServer tomcatServer = new TomcatServer(props, TomcatWebSocketConfigImpl.class);
    tomcatServer.start();

    //create websocket details
    String scheme = "ws";
    String host = "localhost";
    int port = props.getPort();
    String websocketUri = EchoEndpoint.WEBSOCKET_URI;

    //create websocket container
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

    //create a client websocket session
    TestClientEndpoint testClientEndpoint = new TestClientEndpoint();
    Session session = webSocketContainer.connectToServer(testClientEndpoint,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, websocketUri)));

    //TEST: send string-message to server
    String stringMessage = "Hello Server";
    session.getBasicRemote().sendText(stringMessage);
    //assert that sent message to server is equal received.
    assertThat(stringMessage).isEqualTo(testClientEndpoint.getStringMessage());

    //TEST: send byte-message to server
    byte[] byteMessage = "Hello Server".getBytes();
    session.getBasicRemote().sendBinary(ByteBuffer.wrap(byteMessage));
    assertThat(byteMessage).containsExactly(testClientEndpoint.getBinaryMessage().array());

    Awaitility.await()
        .timeout(Duration.ofSeconds(4))
        .pollDelay(Duration.ofSeconds(3))
        .untilAsserted(() -> {
          tomcatServer.stop();
          log.warn(TOMCAT_SERVER_STOP_MESSAGE);
        });

    //blocking join until close is called.
    tomcatServer.join();
  }

  //create a test websocket configuration class for server
  public static class TomcatWebSocketConfigImpl extends TomcatWebSocketConfig {

    {
      programmaticEndpoints = Set.of(new ProgrammaticEndpoint(EchoEndpoint.class,
          org.sample.tomcat_embed_11.websocket.programmatic.EchoEndpoint.WEBSOCKET_URI));
    }
  }


}