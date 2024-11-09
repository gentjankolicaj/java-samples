package org.sample.websocket.chat_json;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.SendHandler;
import jakarta.websocket.SendResult;
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
import org.sample.websocket.chat_json.codec.MessageJsonDecoder;
import org.sample.websocket.chat_json.codec.MessageJsonEncoder;
import org.sample.websocket.chat_json.message.ChatMessage;
import org.sample.websocket.chat_json.message.ChatMessagesMessage;
import org.sample.websocket.chat_json.message.GetUsersMessage;
import org.sample.websocket.chat_json.message.JoinUserMessage;
import org.sample.websocket.chat_json.message.Message;
import org.sample.websocket.chat_json.message.User;
import org.sample.websocket.chat_json.message.UserListMessage;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 2:42 PM
 */
@Slf4j
@Getter
@ServerEndpoint(value = JSONChatEndpoint.ENDPOINT_URI,
    encoders = MessageJsonEncoder.class,
    decoders = MessageJsonDecoder.class)
public class JSONChatEndpoint {

  protected static final String ENDPOINT_URI = "/ws/chat_json";
  protected static List<JSONChatEndpoint> clientEndpoints = new CopyOnWriteArrayList<>();
  private static List<User> chatUsers = new CopyOnWriteArrayList<>();
  private static List<Message> chatMessages = new CopyOnWriteArrayList<>();

  private Session session;
  private ServerEndpointConfig config;
  private ByteArrayOutputStream baos;

  /**
   * Method called on open connection from client.
   *
   * @param session websocket session
   * @param config  endpoint config
   */
  @OnOpen
  public void onOpen(Session session, ServerEndpointConfig config) {
    this.session = session;
    this.config = config;
    clientEndpoints.add(this);
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    log.info("Session {} closed.Reason {}", session.getId(), reason.getReasonPhrase());
    clientEndpoints.remove(this);
  }


  /**
   * Method called when client sends message type
   *
   * @param message message type sent by websocket peers
   */
  @OnMessage
  public void onMessage(Message message) {
    log.info("JSONChatEndpoint: received message-type {}", message);
    if (message instanceof GetUsersMessage) {
      processGetUsersMessage((GetUsersMessage) message);
    } else if (message instanceof JoinUserMessage) {
      processJoinUserMessage((JoinUserMessage) message);
    } else if (message instanceof ChatMessage) {
      processChatMessage((ChatMessage) message);
    }
  }


  /**
   * Method called on client sends binary
   *
   * @param byteBuffer byte buffer sent by websocket peers.
   * @param last       flag to establish if buffer received is the last.
   */
  @OnMessage
  public void onMessage(ByteBuffer byteBuffer, boolean last) {
    log.info("JSONChatEndpoint: received buffer {}", byteBuffer);
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

  private void processGetUsersMessage(GetUsersMessage message) {
    chatMessages.add(message);
    broadcast(new UserListMessage(chatUsers));
  }

  private void processJoinUserMessage(JoinUserMessage message) {
    chatUsers.add(message.getUser());
    broadcast(message);
  }

  private void processChatMessage(ChatMessage message) {
    chatMessages.add(message);
    broadcast(new ChatMessagesMessage(chatMessages));
  }

  private void broadcast(Message message) {
    for (JSONChatEndpoint clientEndpoint : clientEndpoints) {
      try {
        clientEndpoint.getSession().getBasicRemote().sendObject(message);
      } catch (IOException | EncodeException ioe) {
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

    for (JSONChatEndpoint clientEndpoint : clientEndpoints) {
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
