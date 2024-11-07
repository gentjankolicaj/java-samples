package org.sample.socket.api;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Packet implements Serializable {

  private Integer origin;
  private Integer destination;
  private String message;

}
