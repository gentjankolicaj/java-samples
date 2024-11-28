package org.sample.jetty_12.websocket.programatic;


import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler.Whole;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 10:56 PM
 */
@ServerEndpoint(ProgTimeEndpoint.WEBSOCKET_URI)
@Slf4j
public class ProgTimeEndpoint extends Endpoint {

  protected static final String WEBSOCKET_URI = "/prog_time";
  private Session session;


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    this.session = session;
    this.setup(session);
  }

  @Override
  public void onClose(Session session, CloseReason closeReason) {
    log.info("server close reason {}", closeReason.getReasonPhrase());
    this.session = null;
  }

  public void setup(Session session) {
    log.info("server open session: {}", session);
    new Thread(() -> pushTime()).start();
    session.addMessageHandler(new TextMessageHandler());
  }

  private void pushTime() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    while (this.session != null) {
      try {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        this.session.getBasicRemote().sendText(zonedDateTime.format(dateTimeFormatter));
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  static class TextMessageHandler implements Whole<String> {

    @Override
    public void onMessage(String message) {
      log.warn("server received-message : {}", message);
    }

  }


}