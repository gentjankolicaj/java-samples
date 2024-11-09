package org.sample.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/8/24 5:23 PM
 */
@Slf4j
class WebSocketConfigTest {

  @Test
  void websocket() throws LifecycleException {
    TomcatServer tomcatServer = new TomcatServer(8080, TestWebSocketConfig.class);
    tomcatServer.start();

    //blocking join until close is called.
    tomcatServer.join();
  }



}