package org.sample.websocket.chat_json.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import org.sample.websocket.chat_json.message.Message;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 2:43 PM
 */
public class MessageJsonEncoder implements Encoder.Text<Message> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String encode(Message object) throws EncodeException {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new EncodeException(object, e.getMessage(), e);
    }
  }

}
