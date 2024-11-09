package org.sample.websocket.chat_json.message;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:21 PM
 */
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private String username;
  private String firstname;
  private String lastname;

}
