package org.sample.websocket.chat_json;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.sample.websocket.AbstractWSConfig;
import org.sample.websocket.TomcatServer;
import org.sample.websocket.chat_json.codec.MessageJsonDecoder;
import org.sample.websocket.chat_json.codec.MessageJsonEncoder;
import org.sample.websocket.chat_json.message.ChatMessage;
import org.sample.websocket.chat_json.message.GetUsersMessage;
import org.sample.websocket.chat_json.message.JoinUserMessage;
import org.sample.websocket.chat_json.message.Message;
import org.sample.websocket.chat_json.message.User;

/**
 * @author gentjan kolicaj
 * @Date: 11/9/24 4:20 PM
 */
class JSONChatEndpointTest {


  @Test
  void endpoint() throws LifecycleException, IOException, DeploymentException, EncodeException {
    TomcatServer tomcatServer = new TomcatServer(8080, WSConfig.class);
    tomcatServer.start();

    //websocket client request
    WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();

    //create session & send message object directly.
    Session wsSession = wsContainer.connectToServer(CurrentClientEndpoint.class,
        URI.create("ws://localhost:8080/" + JSONChatEndpoint.ENDPOINT_URI));

    //send join user message
    wsSession.getBasicRemote().sendObject(new JoinUserMessage(new User("username", "john", "doe")));

    //send chat message
    wsSession.getBasicRemote().sendObject(new ChatMessage("Hello world"));

    //get all users message
    wsSession.getBasicRemote().sendObject(new GetUsersMessage());

    //stop tomcat after 11 seconds
    Awaitility.await()
        .timeout(Duration.ofSeconds(6))
        .pollDelay(Duration.ofSeconds(5))
        .untilAsserted(tomcatServer::stop);

    //blocking join until close is called.
    tomcatServer.join();

  }


  public static class WSConfig extends AbstractWSConfig {

    public WSConfig() {
      super(Set.of(JSONChatEndpoint.class), Set.of());
    }
  }


  @Slf4j
  @ClientEndpoint(encoders = MessageJsonEncoder.class, decoders = MessageJsonDecoder.class)
  @Getter
  public static class CurrentClientEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("client: session opened.");
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
      log.info("client received message-type : {}", message);
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer buffer) {
      log.info("client received buffer : {}", buffer.array());
    }
  }


}