package org.sample.websocket;

import jakarta.websocket.Endpoint;

/**
 * @author gentjan kolicaj
 * @Date: 11/9/24 9:56 AM
 */
public record ProgrammaticEndpoint(Class<? extends Endpoint> endpointClass, String path) {

}