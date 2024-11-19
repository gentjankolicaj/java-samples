package org.sample.tomcat_embed_11.websocket.programmatic;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/19/24 11:10 AM
 */
@Slf4j
@Getter
public class AsyncEchoEndpoint extends Endpoint {

  protected static final String WEBSOCKET_URI = "/prog_async_echo";
  private Session session;

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    log.info("server session opened.");
    this.session = session;

    //set of handlers
    session.addMessageHandler(new StringMessageHandler(session));
    session.addMessageHandler(new BinaryMessageHandler(session));
  }

  public void onClose(Session session, CloseReason closeReason) {
    log.warn("server session closed, id: {}, reason : {}", session.getId(), closeReason.toString());
  }


  public void onError(Session session, Throwable throwable) {
    log.error("server session error, id: {}", session.getId(), throwable);
  }

  @AllArgsConstructor
  static class StringMessageHandler implements MessageHandler.Partial<String> {

    Session session;

    @Override
    public void onMessage(String message, boolean last) {
      try {
        onStringMessage(message, last);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void onStringMessage(String message, boolean last) throws IOException {
      log.info(
          "server session id {}, received string-message: {}, last-message: {}, sending-back: {}",
          session.getId(), message, last, message);

      //sent back to client same message
      session.getBasicRemote().sendText(message);

    }
  }

  @AllArgsConstructor
  static class BinaryMessageHandler implements MessageHandler.Partial<ByteBuffer> {

    Session session;

    @Override
    public void onMessage(ByteBuffer byteBuffer, boolean last) {
      try {
        onByteMessage(byteBuffer, last);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public void onByteMessage(ByteBuffer byteBuffer, boolean last) throws IOException {
      log.info("server session id {}, received byte-message: {}, last-message: {},sending-back: {}",
          session.getId(), byteBuffer.array(), last, byteBuffer.array());

      //sent back to client same message
      session.getBasicRemote().sendBinary(byteBuffer);
    }
  }
}
