package org.sample.websocket.chat_json.message;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:18 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesMessage extends Message {

  protected List<Message> messages;

}
