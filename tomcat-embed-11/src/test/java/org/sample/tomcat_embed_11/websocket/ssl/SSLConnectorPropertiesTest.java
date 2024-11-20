package org.sample.tomcat_embed_11.websocket.ssl;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.ConfigurationException;
import io.jdev.jackson.YamlConfigurations;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/20/24 6:55 PM
 */
class SSLConnectorPropertiesTest {


  @Test
  void load() throws ConfigurationException {
    SSLConnectorProperties connectorProperties = YamlConfigurations.load(
        SSLConnectorProperties.class, "/ssl/connector.yaml");
    assertThat(connectorProperties).isNotNull();

    SSLProperties sslProps = connectorProperties.getSslProperties();
    assertThat(sslProps).isNotNull();
    assertThat(sslProps.getCertAlias()).isEqualTo("tomcat-alias");
    assertThat(sslProps.getCertKeyPassword()).isNullOrEmpty();
    assertThat(sslProps.getKeystorePassword()).isEqualTo("1234567");
    assertThat(sslProps.getKeystoreFile()).isEqualTo("/ssl/keystore.p12");
  }

}