package org.sample.websocket;

import java.util.Set;

public class TestWebSocketConfig extends WebSocketConfig {

  public TestWebSocketConfig() {
    super(Set.of(TestEndpoint.class), Set.of());
  }
}
