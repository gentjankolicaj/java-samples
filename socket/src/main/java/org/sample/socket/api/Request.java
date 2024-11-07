package org.sample.socket.api;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Request implements Serializable {

  private Integer origin;
  private Integer packets;

}
