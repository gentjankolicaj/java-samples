package org.sample.tomcat_embed_11.websocket;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.nio.ByteBuffer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/16/24 9:59 PM
 */
@Slf4j
public class WSTest {

  //create a test client endpoint class for websocket
  @ClientEndpoint
  @Getter
  public static class TestClientEndpoint {

    protected Session session;
    private String stringMessage;
    private ByteBuffer binaryMessage;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("client session opened id {}", session.getId());

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
      log.warn("client session closed, id: {}, reason : {}", session.getId(),
          closeReason.toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
      log.error("client session error, id: {}", session.getId(), throwable);
    }

    @OnMessage
    public void onStringMessage(Session session, String message) {
      log.info("client session id {}, received string-message: {}", session.getId(), message);

      //store message for testing purposes
      this.stringMessage = message;
    }

    @OnMessage
    public void onByteMessage(Session session, ByteBuffer byteBuffer) {
      log.info("client session id {}, received byte-message: {}", session.getId(),
          byteBuffer.array());

      //store message for testing purposes
      this.binaryMessage = byteBuffer;
    }
  }


  @Getter
  public static class TestStringMessageHandler implements MessageHandler.Whole<String> {

    private String message;

    @Override
    public void onMessage(String message) {
      //store message
      this.message = message;
    }
  }

  @Getter
  public static class TestBinaryMessageHandler implements MessageHandler.Whole<ByteBuffer> {

    private ByteBuffer byteBuffer;

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
      //store message
      this.byteBuffer = byteBuffer;
    }
  }

}
