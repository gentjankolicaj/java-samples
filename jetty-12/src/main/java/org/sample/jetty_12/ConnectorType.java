package org.sample.jetty_12;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConnectorType {
  SERVER_CONNECTOR("ServerConnector");
  private final String className;

}