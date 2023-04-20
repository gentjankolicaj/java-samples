package org.sample.socket.listeners;

import org.sample.socket.api.Request;
import org.sample.socket.api.Response;
import org.sample.socket.exceptions.WaitException;

public interface ServerSocketListener {

  void onRequest(Request request);

  void onResponse(Response response);

  void onWait() throws WaitException;

  void onClose() throws Exception;
}
