package org.sample.tomcat_embed_11.connectors.single;

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
public class SingleConnectorTomcatServer {

  private final SingleConnectorProperties connectorProps;
  protected String contextPath = "";
  protected String docBase = null;
  private Tomcat tomcat;
  private Connector connector;

  private void setup() throws LifecycleException {
    tomcat = new Tomcat();

    // As of Tomcat 9, the HTTP connector won't start without this call.
    connector = tomcat.getConnector();
    setupConnector(connector);

    //Add a context
    Context rootCtx = tomcat.addContext(contextPath, docBase);

    //needed otherwise a default servlet is not create => not created web socket.
    Wrapper wrapper = Tomcat.addServlet(rootCtx, "default", new DefaultServlet());
    wrapper.setAsyncSupported(true);

    rootCtx.addServletMappingDecoded("/", "default");

    tomcat.start();
  }

  private void setupConnector(Connector connector) {
    if (connectorProps == null) {
      throw new IllegalStateException("ConnectorProperties is null.Can't setup tomcat connector.");
    }
    connector.setScheme(connectorProps.getScheme());
    connector.setSecure(connectorProps.isSecure());
    connector.setPort(connectorProps.getPort());
    connector.setAsyncTimeout(connectorProps.getAsyncTimeout());
    connector.setAllowBackslash(connectorProps.isAllowBackslash());
    connector.setAllowTrace(connectorProps.isAllowTrace());
    connector.setProxyName(connectorProps.getProxyName());
    connector.setProxyPort(connectorProps.getProxyPort());
    connector.setMaxSavePostSize(connectorProps.getMaxSavePostSize());
    connector.setMaxCookieCount(connectorProps.getMaxCookieCount());
    connector.setMaxParameterCount(connectorProps.getMaxParameterCount());
    connector.setMaxPostSize(connectorProps.getMaxPostSize());

    if (connectorProps.getProperties() != null && !connectorProps.getProperties().isEmpty()) {
      connectorProps.getProperties().forEach((k, v) -> {
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
