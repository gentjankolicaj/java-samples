package org.sample.websocket.chat;

import static org.sample.websocket.chat.ChatEndpoint.ENDPOINT_URI;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.SendHandler;
import jakarta.websocket.SendResult;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
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
@SuppressWarnings("unused")
@Slf4j
@Getter
@ServerEndpoint(ENDPOINT_URI)
public class ChatEndpoint {

  protected static final String ENDPOINT_URI = "/ws/chat";
  protected static List<ChatEndpoint> clientEndpoints = new CopyOnWriteArrayList<>();

  private Session session;
  private ByteArrayOutputStream baos;

  /**
   * Method called on open connection from client.
   *
   * @param session websocket session
   */
  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    log.info("server: session opened.");
    clientEndpoints.add(this);
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    log.info("server: session closed {}.Reason {}", session.getId(), reason.getReasonPhrase());
    clientEndpoints.remove(this);
  }


  /**
   * Method called on client sent string message
   *
   * @param message full message sent by websocket peers
   */
  @OnMessage
  public void onMessage(String message) {
    log.info("server: received string.");
    broadcastString(message);
  }


  /**
   * Method called on client sent binary message
   *
   * @param byteBuffer byte buffer sent by websocket peers.
   * @param last       flag to establish if buffer received is the last.
   */
  @OnMessage
  public void onMessage(ByteBuffer byteBuffer, boolean last) {
    log.info("server: received buffer.");
    if (baos == null) {
      baos = new ByteArrayOutputStream();
    }
    try {
      //check if not last buffer
      if (!last) {
        //accumulate byte buffer
        baos.write(byteBuffer.array());

        //broadcast byte buffer
        broadcastBinary(byteBuffer);
      } else {
        //accumulate byte buffer
        baos.write(byteBuffer.array());

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
    //since byte buffers can only be in read or write mode we create a byte buffer on write & rewind() it.
    ByteBuffer sendBuffer = ByteBuffer.allocate(byteBuffer.array().length);
    sendBuffer.put(byteBuffer.array());
    sendBuffer.rewind();

    for (ChatEndpoint clientEndpoint : clientEndpoints) {
      try {
        clientEndpoint.getSession().getAsyncRemote().sendBinary(sendBuffer, new SendHandler() {
          @Override
          public void onResult(SendResult result) {
            log.info("Send result: {}", result.isOK());
          }
        });
      } catch (Exception ioe) {
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
      baos.writeTo(fos);

      //flush fos
      fos.flush();

      //destroy byte array output stream
      baos.reset();
      baos.close();
      baos = null;
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
