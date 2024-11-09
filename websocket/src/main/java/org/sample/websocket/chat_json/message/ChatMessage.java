package org.sample.websocket.chat_json.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:18 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage extends Message {

  protected String message;

}
