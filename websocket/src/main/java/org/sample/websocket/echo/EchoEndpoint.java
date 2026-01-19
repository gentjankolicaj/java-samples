package org.sample.websocket.echo;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/5/24 1:30 PM
 */
@Slf4j
public class EchoEndpoint extends Endpoint {

  /**
   * As opposed to servlets, WebSocket endpoints are instantiated multiple times. The container creates an instance of an endpoint per
   * connection to its deployment URI. Each instance is associated with one and only one connection. This facilitates keeping user state for
   * each connection and makes development easier, because there is only one thread executing the code of an endpoint instance at any given
   * time. This is why I store session references,
   */
  public static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
  public static final String ENDPOINT_URI = "/echo";


  /**
   * This method is called when client opens a connection to server. We can get also remoteEndpoint to communicate with client.
   *
   * @param session the session that has just been activated.
   * @param config  the configuration used to configure this endpoint.
   */
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    log.info("server: session opened, onOpen() invoked.");
    //store session
    SESSION_MAP.putIfAbsent(session.getId(), session);

    //handles all message
    session.addMessageHandler(new WholeMessageHandler(session.getBasicRemote()));

    //handles parts of messages , commented because a message handler already set.
    //session.addMessageHandler(new PartialMessageHandler(session.getBasicRemote()));
  }

  @RequiredArgsConstructor
  static class WholeMessageHandler implements MessageHandler.Whole<String> {

    private final RemoteEndpoint.Basic remoteEndpoint;

    @Override
    public void onMessage(String message) {
      try {
        remoteEndpoint.sendText(message);
      } catch (IOException ioe) {
        log.error("", ioe);
      }
    }
  }

  @RequiredArgsConstructor
  static class PartialMessageHandler implements MessageHandler.Partial<String> {

    private final RemoteEndpoint.Basic remoteEndpoint;


    @Override
    public void onMessage(String partialMessage, boolean last) {
      try {
        remoteEndpoint.sendText("Part received.");
        if (last) {
          log.info("Last message part");
        } else {
          log.info("Message part");
        }
      } catch (IOException ioe) {
        log.info("", ioe);
      }
    }
  }

}
