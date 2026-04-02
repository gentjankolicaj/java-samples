package samples.socket.listeners;

import samples.socket.api.Request;
import samples.socket.api.Response;

//todo: to integrate into sample
public interface ServerSocketListener {

  void onRequest(Request request) throws Exception;

  void onResponse(Response response) throws Exception;

  void onWait(Runnable runnable) throws Exception;

  void onClose() throws Exception;
}
