package org.sample.jetty_12;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.conscrypt.OpenSSLProvider;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
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
@RequiredArgsConstructor
public class JettyServer {

  private static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(5);
  protected final JettyServerProperties serverProperties;
  protected Server server;

  private void setup() {
    //Thread pool setup
    QueuedThreadPool threadPool = createThreadPool(serverProperties.getThreadPool());

    //server setup
    server = createServer(threadPool, serverProperties);

    //connector setup
    createConnectors(server, serverProperties.getConnectors());

  }

  protected void createConnectors(Server server,
      List<ConnectorProperties> connectorPropertiesList) {
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
    //add all connectors to server
    server.setConnectors(connectors.toArray(new Connector[0]));
  }

  protected Connector createConnector(Server server, ConnectorProperties connectorProps) {
    Optional<HttpConfigProperties> optionalHttpConfig = connectorProps.getHttpConfig();
    Connector connector;
    if (optionalHttpConfig.isPresent()) {
      HttpConfigProperties httpConfigProps = optionalHttpConfig.get();
      Optional<SSLProperties> optionalSSL = httpConfigProps.getSsl();
      connector = optionalSSL.map(
              sslProperties -> getConnectorHttpsConfig(server, connectorProps,
                  httpConfigProps, sslProperties)).
          orElseGet(() -> getConnectorHttpConfig(server, connectorProps, httpConfigProps));
    } else {
      connector = getConnectorNoHttpConfig(server, connectorProps);
    }
    return connector;
  }


  private ServerConnector getConnectorNoHttpConfig(Server server,
      ConnectorProperties connectorProperties) {
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

  private ServerConnector getConnectorHttpConfig(Server server,
      ConnectorProperties connectorProperties, HttpConfigProperties httpConfigProperties) {
    HttpConfiguration httpConfig = createHttpConfiguration(httpConfigProperties);

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

  private ServerConnector getConnectorHttpsConfig(Server server,
      ConnectorProperties connectorProperties, HttpConfigProperties httpConfigProperties,
      SSLProperties sslProperties) {
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

    //Configure TLS
    //Because of java.lang.IllegalStateException: Connection rejected: No ALPN Processor
    // for sun.security.ssl.SSLEngineImpl from [org.eclipse.jetty.alpn.conscrypt.server.ConscryptServerALPNProcessor@ce5a68e]
    configureTLS(sslContextFactory);


    // SSL HTTP Configuration
    HttpConfiguration httpsConfig = createHttpConfiguration(httpConfigProperties);
    httpsConfig.addCustomizer(new SecureRequestCustomizer());

    // Configure the Connector to speak HTTP/1.1 and HTTP/2.
    HttpConnectionFactory h1 = new HttpConnectionFactory(httpsConfig);
    HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfig);
    ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
    alpn.setDefaultProtocol(h1.getProtocol());
    SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

    ServerConnector connector = new ServerConnector(server, ssl, alpn, h2, h1);
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

  protected void configureTLS(SslContextFactory.Server sslContextFactory) {
    // https://jetty.org/docs/jetty/12/programming-guide/server/http.html#connector-protocol-tls-conscrypt
    Security.addProvider(new OpenSSLProvider());
    sslContextFactory.setProvider("Conscrypt");
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

    //set values
    httpConfigProperties.getSecureScheme().ifPresent(httpConfig::setSecureScheme);
    httpConfigProperties.getSecurePort().ifPresent(httpConfig::setSecurePort);
    httpConfigProperties.getOutputBufferSize().ifPresent(httpConfig::setOutputBufferSize);
    httpConfigProperties.getRequestHeaderSize().ifPresent(httpConfig::setRequestHeaderSize);
    httpConfigProperties.getResponseHeaderSize().ifPresent(httpConfig::setResponseHeaderSize);
    httpConfigProperties.getSendServerVersion().ifPresent(httpConfig::setSendServerVersion);
    httpConfigProperties.getSendDateHeader().ifPresent(httpConfig::setSendDateHeader);
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
