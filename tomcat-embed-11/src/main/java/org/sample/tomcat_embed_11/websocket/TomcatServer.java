package org.sample.tomcat_embed_11.websocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.sample.tomcat_embed_11.servlet.TomcatServlet;


@Slf4j
@Getter
public class TomcatServer {

  protected String contextPath = "";
  protected String docBase = null;
  private Tomcat tomcat;
  private final ConnectorProperties connectorProperties;
  private Connector connector;
  private Class<? extends TomcatWebSocketConfig> websocketConfig;
  private TomcatServlet[] servlets;

  public TomcatServer(ConnectorProperties connectorProperties,
      Class<? extends TomcatWebSocketConfig> websocketConfig) {
    this.connectorProperties = connectorProperties;
    this.websocketConfig = websocketConfig;
  }

  public TomcatServer(ConnectorProperties connectorProperties, TomcatServlet... servlets) {
    this.connectorProperties = connectorProperties;
    this.servlets = servlets;
  }

  public TomcatServer(ConnectorProperties connectorProperties,
      Class<? extends TomcatWebSocketConfig> websocketConfig, TomcatServlet... servlets) {
    this.connectorProperties = connectorProperties;
    this.websocketConfig = websocketConfig;
    this.servlets = servlets;
  }

  protected void setup() throws LifecycleException {
    tomcat = new Tomcat();

    // As of Tomcat 9, the HTTP connector won't start without this call.
    connector = tomcat.getConnector();

    //setup connector
    setupConnector(connector, connectorProperties);

    //Add a context
    Context rootCtx = tomcat.addContext(contextPath, docBase);

    //setup servlets 
    setupServlets(rootCtx, servlets);

    //setup websockets
    setupWebSocket(rootCtx, websocketConfig);

    //start tomcat.
    tomcat.start();
  }

  protected void setupWebSocket(Context rootCtx,
      Class<? extends TomcatWebSocketConfig> websocketConfig) {
    if (websocketConfig != null && rootCtx != null) {
      rootCtx.addApplicationListener(websocketConfig.getName());
      log.info("websocket setup with config class: {}", websocketConfig.getCanonicalName());
    }
  }


  protected void setupServlets(Context rootCtx, TomcatServlet[] servlets) {
    //needed otherwise a default servlet is not create => not created web socket.
    Wrapper defaultWrapper = Tomcat.addServlet(rootCtx, "default", new DefaultServlet());
    defaultWrapper.setAsyncSupported(true);
    rootCtx.addServletMappingDecoded("/", "default");

    if (servlets != null && servlets.length != 0) {

      StringBuilder sb = new StringBuilder();
      for (int i = 0, len = servlets.length; i < len; i++) {
        TomcatServlet servlet = servlets[i];

        //add servlet
        Tomcat.addServlet(rootCtx, servlet.getName(), servlet);
        //setup servlet mapping
        rootCtx.addServletMappingDecoded(servlet.getPattern(), servlet.getName());
        //add servlet info
        sb.append("servlet name: ").append(servlet.getName()).append(" pattern: ")
            .append(servlet.getPattern()).append("\n");
      }
      log.info("total servlet setup: {}", 1 + servlets.length);
      log.info("servlet infos: ");
      log.info(sb.toString());
    }
  }

  protected void setupConnector(Connector connector, ConnectorProperties connectorProperties) {
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
    log.info("setup tomcat-connector with properties : {}", connectorProperties);
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
