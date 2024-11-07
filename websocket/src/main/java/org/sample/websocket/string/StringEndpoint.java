package org.sample.websocket.string;

import jakarta.websocket.CloseReason;
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
public class StringEndpoint extends Endpoint {

  /**
   * As opposed to servlets, WebSocket endpoints are instantiated multiple times. The container
   * creates an instance of an endpoint per connection to its deployment URI. Each instance is
   * associated with one and only one connection. This facilitates keeping user state for each
   * connection and makes development easier, because there is only one thread executing the code of
   * an endpoint instance at any given time. This is why I store session references,
   */
  public static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
  public static final String ENDPOINT_URI = "/websocket/string";


  /**
   * This method is called when client opens a connection to server. We can get also remoteEndpoint
   * to communicate with client.
   *
   * @param session the session that has just been activated.
   * @param config  the configuration used to configure this endpoint.
   */
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    log.info("onOpen() invoked.");
    //store session
    SESSION_MAP.putIfAbsent(session.getId(), session);

    //handles all message
    session.addMessageHandler(new WholeMessageHandler(session.getBasicRemote()));

    //handles parts of messages
    session.addMessageHandler(new PartialMessageHandler(session.getBasicRemote()));
  }

  /**
   * This method is called when client closes connection to server.
   *
   * @param session     the session about to be closed.
   * @param closeReason the reason the session was closed.
   */
  @Override
  public void onClose(Session session, CloseReason closeReason) {
    log.info("StringEndpoint: closed connection with session-id {}", session.getId());
  }

  @RequiredArgsConstructor
  static class WholeMessageHandler implements MessageHandler.Whole<String> {

    private final RemoteEndpoint.Basic remoteEndpoint;

    @Override
    public void onMessage(String message) {
      try {
        //send message from server to remote peer
        remoteEndpoint.sendText("StringEndpoint: Whole message received remote Peer !!!");
        log.info("StringEndpoint: Whole message: {}", message);
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
        if (last) {
          log.info("StringEndpoint: last message part: {}", partialMessage);
          remoteEndpoint.sendText("StringEndpoint: Last part message received remote Peer !!!");
        } else {
          log.info("StringEndpoint: message part: {}", partialMessage);
          remoteEndpoint.sendText("StringEndpoint: Part message received remote Peer !!!");
        }
      } catch (IOException ioe) {
        log.info("", ioe);
      }
    }
  }

}
