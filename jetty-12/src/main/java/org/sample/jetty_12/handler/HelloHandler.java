package org.sample.jetty_12.handler;

import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;

/**
 * @author gentjan kolicaj
 * @Date: 11/22/24 1:16 PM
 */
@RequiredArgsConstructor
public class HelloHandler extends Handler.Abstract {

  private final String message;


  @Override
  public boolean handle(Request request, Response response, Callback callback) throws Exception {
    response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/plain; charset=utf-8");
    response.write(true, BufferUtil.toBuffer(message), callback);
    return true;
  }

}
