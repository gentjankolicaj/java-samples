package org.sample.websocket.chat_json.message;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:21 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

  private String username;
  private String firstname;
  private String lastname;

}
