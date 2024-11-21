package org.sample.jetty_12;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 5:51 PM
 */
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
    HttpConfigProperties httpConfigProperties = connectorProps.getHttpConfig();
    assertThat(httpConfigProperties.getResponseHeaderSize()).isEqualTo(8192);
    assertThat(httpConfigProperties.getRequestHeaderSize()).isEqualTo(8192);
    assertThat(httpConfigProperties.getOutputBufferSize()).isEqualTo(32768);
  }

}