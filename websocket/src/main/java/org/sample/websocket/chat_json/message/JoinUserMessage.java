package org.sample.websocket.chat_json.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sample.websocket.chat_json.User;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:06 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserMessage extends Message {

  protected User user;

}
