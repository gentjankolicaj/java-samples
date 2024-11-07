package org.sample.websocket.chat_json.message;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sample.websocket.chat_json.User;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:27 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListMessage extends Message {

  protected List<User> users;


}
