package org.sample.websocket.server;

import org.sample.websocket.api.Request;
import org.sample.websocket.api.Response;

public interface WebSocketServerListener {


  void onRequest(Request request) throws Exception;

  void onResponse(Response response) throws Exception;

  void onWait(Runnable runnable) throws Exception;

  void onClose() throws Exception;
}
