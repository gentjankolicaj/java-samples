package org.sample.websocket.chat;

import static org.sample.websocket.chat.ChatEndpoint.ENDPOINT_URI;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 1:01 PM
 */
@Slf4j
@Getter
@ServerEndpoint(ENDPOINT_URI)
public class ChatEndpoint {

  protected static final String ENDPOINT_URI = "/websocket/chat";
  protected static List<ChatEndpoint> clientEndpoints = new CopyOnWriteArrayList<>();

  private Session session;
  private ByteArrayOutputStream buffer;

  /**
   * Method called on open connection from client.
   *
   * @param session
   * @param config
   */
  @OnOpen
  public void onOpen(Session session, ServerEndpointConfig config) {
    this.session = session;
    clientEndpoints.add(this);
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    log.info("Session {} closed.Reason {}", session.getId(), reason.getReasonPhrase());
    clientEndpoints.remove(this);
  }


  /**
   * Method called on client sent string message
   *
   * @param message
   */
  @OnMessage
  public void onMessage(String message) {
    broadcastString(message);
  }


  /**
   * Method called on client sent binary message
   *
   * @param byteBuffer
   * @param last
   */
  @OnMessage
  public void onMessage(ByteBuffer byteBuffer, boolean last) {
    if (buffer == null) {
      buffer = new ByteArrayOutputStream();
    }
    try {
      //check if not last buffer
      if (!last) {
        //accumulate byte buffer
        buffer.write(byteBuffer.array());

        //broadcast buffer
        broadcastBinary(byteBuffer);
      } else {
        //accumulate byte buffer
        buffer.write(byteBuffer.array());

        //write file and reset byte array output stream
        writeFile();

        //broadcast buffer
        broadcastBinary(byteBuffer);
      }
    } catch (IOException ioe) {
      log.error("", ioe);
    }
  }

  private void broadcastString(String message) {
    for (ChatEndpoint clientEndpoint : clientEndpoints) {
      try {
        clientEndpoint.getSession().getBasicRemote().sendText(message);
      } catch (IOException ioe) {
        log.error("", ioe);
        clientEndpoints.remove(clientEndpoint);
        try {
          clientEndpoint.session.close();
        } catch (IOException ioe2) {
          log.error("", ioe2);
        }
      }
    }
  }

  private void broadcastBinary(ByteBuffer byteBuffer) {
    for (ChatEndpoint clientEndpoint : clientEndpoints) {
      try {
        clientEndpoint.getSession().getBasicRemote().sendBinary(byteBuffer);
      } catch (IOException ioe) {
        log.error("", ioe);
        clientEndpoints.remove(clientEndpoint);
        try {
          clientEndpoint.session.close();
        } catch (IOException ioe2) {
          log.error("", ioe2);
        }
      }
    }
  }

  private void writeFile() {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream("tmp_file");

      //write buffer to fos
      buffer.writeTo(fos);

      //flush fos
      fos.flush();

      //destroy byte array output stream
      buffer.reset();
      buffer.close();
      buffer = null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (fos != null) {
          fos.close();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }


}
