package org.sample.jetty_12;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.EnumSet;

/**
 * @author gentjan kolicaj
 * @Date: 11/29/24 1:58 PM
 */
public interface Builder<T> {

  T build();


  interface ServletBuilder<T> extends Builder<T> {

    ServletBuilder<T> sessionOption();

    ServletBuilder<T> securityOption();

    ServletBuilder<T> contextPath(String contextPath);

    ServletBuilder<T> servlet(Class<? extends Servlet> servlet, String pathSpec);

    ServletBuilder<T> filter(Class<? extends Filter> filterClass, String pathSpec);

    ServletBuilder<T> filter(Class<? extends Filter> filterClass, String pathSpec,
        EnumSet<DispatcherType> dispatchers);

  }

  interface WebSocketBuilder<T> extends Builder<T> {

    WebSocketBuilder<T> sessionOption();

    WebSocketBuilder<T> securityOption();

    WebSocketBuilder<T> contextPath(String contextPath);

    WebSocketBuilder<T> servlet(Class<? extends Servlet> servlet, String pathSpec);

    WebSocketBuilder<T> filter(Class<? extends Filter> filterClass, String pathSpec);

    WebSocketBuilder<T> filter(Class<? extends Filter> filterClass, String pathSpec,
        EnumSet<DispatcherType> dispatchers);

    /**
     * Deploys the given annotated endpoint into this ServerContainer.
     *
     * @param endpointClass the class of the annotated endpoint
     */
    WebSocketBuilder<T> endpoint(Class<?> endpointClass);

    /**
     * Deploys the given endpoint described by the provided configuration into this
     * ServerContainer.
     *
     * @param serverConfig the configuration instance representing the logical endpoint that will be
     *                     registered.
     */
    WebSocketBuilder<T> endpoint(ServerEndpointConfig serverConfig);

  }

}
