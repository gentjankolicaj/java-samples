package org.sample.tomcat_embed_11.servlet;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

/**
 * @author gentjan kolicaj
 * @Date: 11/13/24 7:16 PM
 */
@Getter
@RequiredArgsConstructor
public class TomcatServer {

  private final ConnectorProperties connectorProperties;
  private final List<? extends TomcatServlet> servlets;
  protected String contextPath = "";
  protected String docBase = null;
  private Tomcat tomcat;
  private Connector connector;

  private void setup() throws LifecycleException {
    tomcat = new Tomcat();

    // As of Tomcat 9, the HTTP connector won't start without this call.
    connector = tomcat.getConnector();
    setupConnector(connector, connectorProperties);

    //Add a context
    Context rootCtx = tomcat.addContext(contextPath, docBase);

    //setup servlets
    setupServlets(rootCtx, this.servlets);

    tomcat.start();
  }

  private void setupServlets(Context rootCtx, List<? extends TomcatServlet> servlets) {
    //needed otherwise a default servlet is not create => not created web socket.
    Wrapper defaultWrapper = Tomcat.addServlet(rootCtx, "default", new DefaultServlet());
    defaultWrapper.setAsyncSupported(true);
    rootCtx.addServletMappingDecoded("/", "default");

    if (servlets == null || servlets.isEmpty()) {
      throw new IllegalStateException("No servlets found besides DefaultServlet.");
    }
    for (int i = 0, len = servlets.size(); i < len; i++) {
      TomcatServlet servlet = servlets.get(i);

      //add servlet
      Tomcat.addServlet(rootCtx, servlet.getName(), servlet);
      //setup servlet mapping
      rootCtx.addServletMappingDecoded(servlet.getPattern(), servlet.getName());
    }
  }

  private void setupConnector(Connector connector, ConnectorProperties connectorProperties) {
    if (connectorProperties == null) {
      throw new IllegalStateException("ConnectorProperties is null.Can't setup tomcat connector.");
    }
    connector.setScheme(connectorProperties.getScheme());
    connector.setSecure(connectorProperties.isSecure());
    connector.setPort(connectorProperties.getPort());
    connector.setAsyncTimeout(connectorProperties.getAsyncTimeout());
    connector.setAllowBackslash(connectorProperties.isAllowBackslash());
    connector.setAllowTrace(connectorProperties.isAllowTrace());
    connector.setProxyName(connectorProperties.getProxyName());
    connector.setProxyPort(connectorProperties.getProxyPort());
    connector.setMaxSavePostSize(connectorProperties.getMaxSavePostSize());
    connector.setMaxCookieCount(connectorProperties.getMaxCookieCount());
    connector.setMaxParameterCount(connectorProperties.getMaxParameterCount());
    connector.setMaxPostSize(connectorProperties.getMaxPostSize());
    if (connectorProperties.getProperties() != null && !connectorProperties.getProperties()
        .isEmpty()) {
      connectorProperties.getProperties().forEach((k, v) -> {
        if (v != null) {
          connector.setProperty(k, v);
        }
      });
    }
  }


  public void start() throws LifecycleException {
    if (tomcat == null) {
      setup();
    }
  }

  public void join() {
    tomcat.getServer().await();
  }

  public void stop() throws LifecycleException {
    tomcat.stop();
  }

}
