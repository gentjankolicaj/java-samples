package org.sample.websocket;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/8/24 5:10 PM
 */
@RequiredArgsConstructor
@Getter
public class WebSocketEndpoints {

  protected final Set<Class<?>> endpoints;

}
