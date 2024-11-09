package org.sample.websocket.chat_json.message;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 2:48 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message implements Serializable {

  private int type;

}
