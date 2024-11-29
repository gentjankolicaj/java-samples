package org.sample.jetty_12;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.filter.HeaderFilter;
import org.sample.jetty_12.servlet.HelloWorldServlet;
import org.sample.jetty_12.websocket.annotation.HelloEndpoint;

/**
 * @author gentjan kolicaj
 * @Date: 11/29/24 1:51 PM
 */
class JettyBuilderTest {

  @Test
  void newServletBuilder() {
    ServletContextHandler servletContextHandler1 = JettyBuilder.newServletBuilder()
        .securityOption()
        .contextPath("/")
        .servlet(HelloWorldServlet.class, HelloWorldServlet.SERVLET_PATH)
        .filter(HeaderFilter.class, HelloWorldServlet.SERVLET_PATH)
        .build();
    assertThat(servletContextHandler1).isNotNull();

    ServletContextHandler servletContextHandler2 = JettyBuilder.newServletBuilder()
        .sessionOption()
        .contextPath("/")
        .servlet(HelloWorldServlet.class, HelloWorldServlet.SERVLET_PATH)
        .filter(HeaderFilter.class, HelloWorldServlet.SERVLET_PATH)
        .build();
    assertThat(servletContextHandler2).isNotNull();
  }

  @Test
  void newWebSocketBuilder() {
    ServletContextHandler servletContextHandler1 = JettyBuilder.newWebSocketBuilder()
        .sessionOption()
        .contextPath("/")
        .servlet(HelloWorldServlet.class, HelloWorldServlet.SERVLET_PATH)
        .filter(HeaderFilter.class, HelloWorldServlet.SERVLET_PATH)
        .endpoint(HelloEndpoint.class)
        .build();
    assertThat(servletContextHandler1).isNotNull();

    ServletContextHandler servletContextHandler2 = JettyBuilder.newWebSocketBuilder()
        .securityOption()
        .contextPath("/")
        .servlet(HelloWorldServlet.class, HelloWorldServlet.SERVLET_PATH)
        .filter(HeaderFilter.class, HelloWorldServlet.SERVLET_PATH)
        .endpoint(HelloEndpoint.class)
        .build();
    assertThat(servletContextHandler2).isNotNull();
  }
}