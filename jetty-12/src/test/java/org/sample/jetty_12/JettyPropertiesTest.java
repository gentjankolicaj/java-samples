package org.sample.jetty_12;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 5:51 PM
 */
@SuppressWarnings("all")
class JettyPropertiesTest {

  @Test
  void jettyYamlTest() throws ConfigurationException {
    JettyProperties jettyProps = YamlConfigurations.load(JettyProperties.class, "/jetty.yaml");
    assertThat(jettyProps).isNotNull();
    assertThat(jettyProps.getJettyServer()).isNotNull();

    //Thread pool test
    ThreadPoolProperties threadPoolProps = jettyProps.getJettyServer().getThreadPool();
    assertThat(threadPoolProps).isNotNull();
    assertThat(threadPoolProps.poolName()).isEqualTo("jetty-pool");
    assertThat(threadPoolProps.daemonThreads()).isTrue();
    assertThat(threadPoolProps.minThreads()).isEqualTo(4);
    assertThat(threadPoolProps.maxThreads()).isEqualTo(50);
    assertThat(threadPoolProps.reservedThreads()).isEqualTo(1);
    assertThat(threadPoolProps.idleTimeout()).isEqualTo(10000);
    assertThat(threadPoolProps.stopTimeout()).isEqualTo(10000);

    List<ConnectorProperties> connectorPropsList = jettyProps.getJettyServer().getConnectors();
    assertThat(connectorPropsList).isNotNull().hasSize(1);

    //connector properties
    ConnectorProperties connectorProps = connectorPropsList.get(0);
    assertThat(connectorProps).isNotNull();
    assertThat(connectorProps.getName()).isEqualTo("first-connector");
    assertThat(connectorProps.getHost()).isEqualTo("127.0.0.1");
    assertThat(connectorProps.getPort()).isEqualTo(8080);
    assertThat(connectorProps.getIdleTimeout()).isNotNull();
    assertThat(connectorProps.getIdleTimeout().duration()).isEqualTo(11);

    //http config
    HttpConfigProperties httpConfigProperties = connectorProps.getHttpConfig().get();
    assertThat(httpConfigProperties.getResponseHeaderSize().get()).isEqualTo(8192);
    assertThat(httpConfigProperties.getRequestHeaderSize().get()).isEqualTo(8192);
    assertThat(httpConfigProperties.getOutputBufferSize().get()).isEqualTo(32768);
  }

  @Test
  void jettySSLYamlTest() throws ConfigurationException {
    JettyProperties jettyProps = YamlConfigurations.load(JettyProperties.class, "/jetty_ssl.yaml");
    assertThat(jettyProps).isNotNull();
    assertThat(jettyProps.getJettyServer()).isNotNull();

    //Thread pool test
    ThreadPoolProperties threadPoolProps = jettyProps.getJettyServer().getThreadPool();
    assertThat(threadPoolProps).isNotNull();
    assertThat(threadPoolProps.poolName()).isEqualTo("jetty-pool");
    assertThat(threadPoolProps.daemonThreads()).isTrue();
    assertThat(threadPoolProps.minThreads()).isEqualTo(4);
    assertThat(threadPoolProps.maxThreads()).isEqualTo(50);
    assertThat(threadPoolProps.reservedThreads()).isEqualTo(1);
    assertThat(threadPoolProps.idleTimeout()).isEqualTo(10000);
    assertThat(threadPoolProps.stopTimeout()).isEqualTo(10000);

    List<ConnectorProperties> connectorPropsList = jettyProps.getJettyServer().getConnectors();
    assertThat(connectorPropsList).isNotNull().hasSize(1);

    //first connector properties
    ConnectorProperties connectorProps = connectorPropsList.get(0);
    assertThat(connectorProps).isNotNull();
    assertThat(connectorProps.getName()).isEqualTo("first-connector");
    assertThat(connectorProps.getHost()).isEqualTo("127.0.0.1");
    assertThat(connectorProps.getPort()).isEqualTo(8443);
    assertThat(connectorProps.getIdleTimeout()).isNotNull();
    assertThat(connectorProps.getIdleTimeout().duration()).isEqualTo(11);

    //http config
    HttpConfigProperties httpConfigProperties = connectorProps.getHttpConfig().get();
    assertThat(httpConfigProperties.getResponseHeaderSize().get()).isEqualTo(8192);
    assertThat(httpConfigProperties.getRequestHeaderSize().get()).isEqualTo(8192);
    assertThat(httpConfigProperties.getOutputBufferSize().get()).isEqualTo(32768);

    SSLProperties sslProperties = httpConfigProperties.getSsl().get();
    assertThat(sslProperties).isNotNull();
    assertThat(sslProperties.getKeyStoreFile()).isNotNull().isEqualTo("ssl/keystore.p12");
    assertThat(sslProperties.getKeyStorePassword()).isNotNull().isEqualTo("1234567");
  }

  @Test
  void jettyNoHttpConfigYamlTest() throws ConfigurationException {
    JettyProperties jettyProps = YamlConfigurations.load(JettyProperties.class,
        "/jetty_no_httpconfig.yaml");
    assertThat(jettyProps).isNotNull();
    assertThat(jettyProps.getJettyServer()).isNotNull();

    //Thread pool test
    ThreadPoolProperties threadPoolProps = jettyProps.getJettyServer().getThreadPool();
    assertThat(threadPoolProps).isNotNull();
    assertThat(threadPoolProps.poolName()).isEqualTo("jetty-pool");
    assertThat(threadPoolProps.daemonThreads()).isTrue();
    assertThat(threadPoolProps.minThreads()).isEqualTo(4);
    assertThat(threadPoolProps.maxThreads()).isEqualTo(50);
    assertThat(threadPoolProps.reservedThreads()).isEqualTo(1);
    assertThat(threadPoolProps.idleTimeout()).isEqualTo(10000);
    assertThat(threadPoolProps.stopTimeout()).isEqualTo(10000);

    List<ConnectorProperties> connectorPropsList = jettyProps.getJettyServer().getConnectors();
    assertThat(connectorPropsList).isNotNull().hasSize(1);

    //first connector properties
    ConnectorProperties connectorProps = connectorPropsList.get(0);
    assertThat(connectorProps).isNotNull();
    assertThat(connectorProps.getName()).isEqualTo("first-connector");
    assertThat(connectorProps.getHost()).isEqualTo("127.0.0.1");
    assertThat(connectorProps.getPort()).isEqualTo(8080);
    assertThat(connectorProps.getIdleTimeout()).isNotNull();
    assertThat(connectorProps.getIdleTimeout().duration()).isEqualTo(11);

    //http config
    Optional<HttpConfigProperties> optional = connectorProps.getHttpConfig();
    assertThat(optional).isEmpty();

  }

}