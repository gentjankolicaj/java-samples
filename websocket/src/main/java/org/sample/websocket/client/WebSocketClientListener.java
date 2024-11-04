package org.sample.websocket.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sample.websocket.api.Request;
import org.sample.websocket.api.Response;

//todo: to integrate into sample
public interface WebSocketClientListener {

  void onRequest(Request request) throws Exception;

  void onResponse(Response response) throws Exception;

  void onWait(Runnable runnable) throws Exception;

  void onClose() throws Exception;

  @RequiredArgsConstructor
  @Slf4j
  class Default implements WebSocketClientListener {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    @Override
    public void onRequest(Request request) throws Exception {

    }

    @Override
    public void onResponse(Response response) throws Exception {

    }

    @Override
    public void onWait(Runnable runnable) throws Exception {
      runnable.run();
    }

    @Override
    public void onClose() throws Exception {

    }
  }

}
