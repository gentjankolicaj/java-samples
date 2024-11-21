package org.sample.jetty_12;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 6:51 PM
 */
@Slf4j
public class JettyServer {

  private static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(5);
  protected JettyServerProperties jettyServerProperties;
  protected Server server;


  private void setup() {
    //Thread pool setup
    QueuedThreadPool threadPool = createThreadPool(jettyServerProperties.getThreadPool());

    //server setup
    server = createServer(threadPool, jettyServerProperties);

    //connector setup
    setupConnectors(server, jettyServerProperties.getConnectors());

    //handlers setup from context
    log.info("jetty setup.");
  }

  protected void setupConnectors(Server server, List<ConnectorProperties> connectorPropertiesList) {
    if (server == null) {
      throw new JettyException("Server instance can't be null.");
    }

    if (connectorPropertiesList == null || connectorPropertiesList.isEmpty()) {
      throw new JettyException("Connector properties can't be null.");
    }
    List<Connector> connectors = new ArrayList<>();

    for (int i = 0, len = connectorPropertiesList.size(); i < len; i++) {
      ConnectorProperties connectorProperties = connectorPropertiesList.get(i);
      connectors.add(createConnector(server, connectorProperties));
    }

    //add connectors to server
    server.setConnectors(connectors.toArray(new Connector[0]));
  }

  protected Connector createConnector(Server server, ConnectorProperties connectorProperties) {
    HttpConfigProperties httpConfigProperties = connectorProperties.getHttpConfig();
    Connector connector = null;
    if (httpConfigProperties.getSecureScheme() == null || httpConfigProperties.getSecureScheme()
        .equals(HttpScheme.HTTP)) {
      connector = httpConnector(server, connectorProperties);
    } else {
      connector = httpsConnector(server, connectorProperties);
    }
    return connector;
  }

  private ServerConnector httpConnector(Server server, ConnectorProperties connectorProperties) {
    HttpConfiguration httpConfig = new HttpConfiguration();

    // The ConnectionFactory for HTTP/1.1.
    HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);

    // The ConnectionFactory for clear-text HTTP/2.
    HTTP2CServerConnectionFactory http2Factory = new HTTP2CServerConnectionFactory(httpConfig);

    //http connector
    ServerConnector connector = new ServerConnector(server, httpFactory, http2Factory);
    if (StringUtils.isNotEmpty(connectorProperties.getName())) {
      connector.setName(connectorProperties.getName());
    }
    if (StringUtils.isNotEmpty(connectorProperties.getHost())) {
      connector.setHost(connectorProperties.getHost());
    }
    if (connectorProperties.getPort() != 0) {
      connector.setPort(connectorProperties.getPort());
    }
    connector.setIdleTimeout(getTimeout(connectorProperties.getIdleTimeout()));
    return connector;
  }

  private ServerConnector httpsConnector(Server server, ConnectorProperties connectorProperties) {
    SSLProperties sslProperties = connectorProperties.getHttpConfig().getSsl();
    if (sslProperties == null) {
      throw new JettyException("SSL configuration not found.");
    }
    Resource keyStoreResource = findKeyStore(sslProperties.getKeyStoreFile(),
        ResourceFactory.of(server));

    // SSL Context Factory
    SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
    sslContextFactory.setKeyStoreResource(keyStoreResource);
    sslContextFactory.setKeyStorePassword(sslProperties.getKeyStorePassword());
    sslContextFactory.setKeyManagerPassword(sslProperties.getKeyPassword());

    // SSL HTTP Configuration
    HttpConfiguration httpsConfig = new HttpConfiguration();
    httpsConfig.addCustomizer(new SecureRequestCustomizer());

    // Configure the connector to speak HTTP/1.1 and HTTP/2.
    HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpsConfig);
    HTTP2ServerConnectionFactory http2Factory = new HTTP2ServerConnectionFactory(httpsConfig);
    ALPNServerConnectionFactory alpnFactory = new ALPNServerConnectionFactory();
    alpnFactory.setDefaultProtocol(httpFactory.getProtocol());
    SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,
        alpnFactory.getProtocol());

    //ssl connector
    ServerConnector connector = new ServerConnector(server, ssl, alpnFactory, http2Factory,
        httpFactory);
    if (StringUtils.isNotEmpty(connectorProperties.getName())) {
      connector.setName(connectorProperties.getName());
    }
    if (StringUtils.isNotEmpty(connectorProperties.getHost())) {
      connector.setHost(connectorProperties.getHost());
    }
    if (connectorProperties.getPort() != 0) {
      connector.setPort(connectorProperties.getPort());
    }
    connector.setIdleTimeout(getTimeout(connectorProperties.getIdleTimeout()));
    return connector;
  }


  protected Resource findKeyStore(String resourceName, ResourceFactory resourceFactory) {
    Resource resource = resourceFactory.newClassLoaderResource(resourceName);
    if (!Resources.isReadableFile(resource)) {
      throw new JettyException("Unable to read " + resourceName);
    }
    return resource;
  }


  private long getTimeout(TimeoutProperties timeoutProperties) {
    if (Objects.isNull(timeoutProperties)) {
      return DEFAULT_TIMEOUT;
    }
    return timeoutProperties.timeUnit().toMillis(timeoutProperties.duration());
  }


  protected HttpConfiguration createHttpConfiguration(HttpConfigProperties httpConfigProperties) {
    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
    httpConfig.setSecurePort(httpConfigProperties.getSecurePort());
    httpConfig.setOutputBufferSize(32768);
    httpConfig.setRequestHeaderSize(8192);
    httpConfig.setResponseHeaderSize(8192);
    httpConfig.setSendServerVersion(false);
    httpConfig.setSendDateHeader(false);
    httpConfig.setSendXPoweredBy(false);
    return httpConfig;
  }

  protected Server createServer(QueuedThreadPool threadPool,
      JettyServerProperties jettyServerProperties) {
    Server server = new Server(threadPool);
    server.setDumpAfterStart(jettyServerProperties.isDumpAfterStart());
    server.setDumpBeforeStop(jettyServerProperties.isDumpBeforeStop());
    server.setStopAtShutdown(jettyServerProperties.isStopAtShutdown());
    server.setStopTimeout(jettyServerProperties.getStopTimeout());
    return server;
  }

  protected QueuedThreadPool createThreadPool(ThreadPoolProperties threadPoolProperties) {
    QueuedThreadPool threadPool = new QueuedThreadPool();
    threadPool.setName(threadPoolProperties.poolName());
    threadPool.setDaemon(threadPoolProperties.daemonThreads());
    threadPool.setMinThreads(threadPoolProperties.minThreads());
    threadPool.setMaxThreads(threadPoolProperties.maxThreads());
    threadPool.setReservedThreads(threadPoolProperties.reservedThreads());
    threadPool.setIdleTimeout(threadPoolProperties.idleTimeout());
    threadPool.setStopTimeout(threadPoolProperties.stopTimeout());
    return threadPool;
  }


  public void start() throws Exception {
    setup();
    if (server != null) {
      server.start();
    }
  }

  public void join() throws Exception {
    if (server != null) {
      server.join();
    }

  }

  public void stop() throws Exception {
    if (server != null) {
      server.stop();
    }
  }

}
