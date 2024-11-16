package org.sample.tomcat_embed_11.websocket.programmatic;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/16/24 7:45 PM
 */
@Slf4j
public class EchoEndpoint extends Endpoint {

  protected static final String WEBSOCKET_URI = "/prog_basic_echo";


  @Override
  public void onOpen(Session session, EndpointConfig config) {
    log.info("server session opened.");

    //set handlers
    session.addMessageHandler(new StringHandler(session));
    session.addMessageHandler(new BinaryHandler(session));
  }

  public void onClose(Session session, CloseReason closeReason) {
    log.warn("server session closed, id: {}, reason : {}", session.getId(), closeReason.toString());
  }


  public void onError(Session session, Throwable throwable) {
    log.error("server session error, id: {}", session.getId(), throwable);
  }


  @AllArgsConstructor
  static class StringHandler implements MessageHandler.Whole<String> {

    Session session;

    @Override
    public void onMessage(String message) {
      try {
        onStringMessage(message);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void onStringMessage(String message) throws IOException {
      log.info("server session id {}, received string-message: {}, sending-back: {}",
          session.getId(), message, message);

      //sent back to client same message
      session.getBasicRemote().sendText(message);

    }
  }

  @AllArgsConstructor
  static class BinaryHandler implements MessageHandler.Whole<ByteBuffer> {

    Session session;

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
      try {
        onByteMessage(byteBuffer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public void onByteMessage(ByteBuffer byteBuffer) throws IOException {
      log.info("server session id {}, received byte-message: {}, sending-back: {}",
          session.getId(), byteBuffer.array(), byteBuffer.array());

      //sent back to client same message
      session.getBasicRemote().sendBinary(byteBuffer);
    }
  }



}
