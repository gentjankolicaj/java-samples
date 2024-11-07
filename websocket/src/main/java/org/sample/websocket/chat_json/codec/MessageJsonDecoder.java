package org.sample.websocket.chat_json.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import org.sample.websocket.chat_json.message.Message;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 2:44 PM
 */
public class MessageJsonDecoder implements Decoder.Text<Message> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Message decode(String s) throws DecodeException {
    try {
      return objectMapper.readValue(s, Message.class);
    } catch (JsonProcessingException e) {
      throw new DecodeException(s, e.getMessage(), e);
    }
  }

  @Override
  public boolean willDecode(String s) {
    return false;
  }
}
