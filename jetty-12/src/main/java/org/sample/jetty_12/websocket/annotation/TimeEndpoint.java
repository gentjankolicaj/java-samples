package org.sample.jetty_12.websocket.annotation;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 10:50 PM
 */
@ServerEndpoint(TimeEndpoint.URI)
@Slf4j
public class TimeEndpoint {

  protected static final String URI = "/time";
  private Session session;

  @OnOpen
  public void onOpen(Session session) {
    log.info("server open session: {}", session);
    this.session = session;
    new Thread(this::pushTime).start();
  }

  @OnMessage
  public void onText(String message) {
    log.warn("server received-message : {}", message);
  }

  @OnClose
  public void onClose(CloseReason close) {
    log.info("server close reason {}", close);
    this.session = null;
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

}
