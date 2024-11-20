package org.sample.tomcat_embed_11.websocket.ssl;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.sample.tomcat_embed_11.servlet.TomcatServlet;
import org.sample.tomcat_embed_11.websocket.ConnectorProperties;
import org.sample.tomcat_embed_11.websocket.TomcatServer;
import org.sample.tomcat_embed_11.websocket.TomcatWebSocketConfig;

/**
 * @author gentjan kolicaj
 * @Date: 11/20/24 6:49 PM
 */
@Slf4j
public class SSLTomcatServer extends TomcatServer {

  public SSLTomcatServer(ConnectorProperties connectorProperties,
      Class<? extends TomcatWebSocketConfig> websocketConfig) {
    super(connectorProperties, websocketConfig);
  }

  public SSLTomcatServer(ConnectorProperties connectorProperties,
      TomcatServlet... servlets) {
    super(connectorProperties, servlets);
  }

  public SSLTomcatServer(ConnectorProperties connectorProperties,
      Class<? extends TomcatWebSocketConfig> websocketConfig, TomcatServlet... servlets) {
    super(connectorProperties, websocketConfig, servlets);
  }


  @Override
  protected void setupConnector(Connector connector, ConnectorProperties connectorProperties) {
    if (connectorProperties == null) {
      throw new IllegalStateException("ConnectorProperties is null.Can't setup tomcat connector.");
    }
    if (connectorProperties instanceof SSLConnectorProperties sslConnectorProps) {
      connector.setScheme(sslConnectorProps.getScheme());
      connector.setSecure(sslConnectorProps.isSecure());
      connector.setPort(sslConnectorProps.getPort());
      connector.setAsyncTimeout(sslConnectorProps.getAsyncTimeout());
      connector.setAllowBackslash(sslConnectorProps.isAllowBackslash());
      connector.setAllowTrace(sslConnectorProps.isAllowTrace());
      connector.setProxyName(sslConnectorProps.getProxyName());
      connector.setProxyPort(sslConnectorProps.getProxyPort());
      connector.setMaxSavePostSize(sslConnectorProps.getMaxSavePostSize());
      connector.setMaxCookieCount(sslConnectorProps.getMaxCookieCount());
      connector.setMaxParameterCount(sslConnectorProps.getMaxParameterCount());
      connector.setMaxPostSize(sslConnectorProps.getMaxPostSize());
      if (sslConnectorProps.getProperties() != null && !sslConnectorProps.getProperties()
          .isEmpty()) {
        sslConnectorProps.getProperties().forEach((k, v) -> {
          if (v != null) {
            connector.setProperty(k, v);
          }
        });
      }

      //Create SSL config
      SSLHostConfig sslHostConfig = getSslHostConfig(sslConnectorProps);

      //add SSLConfig to connector
      connector.getProtocolHandler().addSslHostConfig(sslHostConfig, true);
      SSLHostConfig[] configs = connector.findSslHostConfigs();

      log.info("setup tomcat-connector with properties : {}", connectorProperties);
    } else {
      this.setupConnector(connector, connectorProperties);
    }
  }

  protected SSLHostConfig getSslHostConfig(SSLConnectorProperties sslConnectorProps) {
    SSLProperties sslProps = sslConnectorProps.getSslProperties();

    // Enable SSL for the connector
    SSLHostConfig sslHostConfig = new SSLHostConfig();

    SSLHostConfigCertificate certificate = new SSLHostConfigCertificate();
    certificate.setCertificateKeystoreFile(sslProps.getKeystoreFile());
    certificate.setCertificateKeystorePassword(sslProps.getKeystorePassword());
    certificate.setCertificateKeyAlias(sslProps.getCertAlias());
    certificate.setCertificateKeyPassword(sslProps.getCertKeyPassword());

    sslHostConfig.addCertificate(certificate);
    return sslHostConfig;
  }

}
