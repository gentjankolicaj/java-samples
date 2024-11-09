package org.sample.tomcat_embed_11.simple;

import lombok.Getter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:43 PM
 */
@Getter
public class TomcatServer {

  protected String contextPath = "";
  protected String docBase = null;
  private Tomcat tomcat;
  private int port = 80;

  public TomcatServer(int port) {
    this.port = port;
  }

  private void setup() throws LifecycleException {
    tomcat = new Tomcat();
    tomcat.setPort(port); // Listen on port 80, which is used by the built-in Java App Service

    // As of Tomcat 9, the HTTP connector won't start without this call.
    tomcat.getConnector();

    //Add a context
    Context context = tomcat.addContext(contextPath, docBase);

    //needed otherwise a default servlet is not create => not created web socket.
    tomcat.addServlet(context, "default", new DefaultServlet());
    context.addServletMappingDecoded("/", "default");

    tomcat.start();
  }


  public void start() throws LifecycleException {
    if (tomcat == null) {
      setup();
    }
  }

  public void join() {
    tomcat.getServer().await();
  }

  public void stop() throws LifecycleException {
    tomcat.stop();
  }

}
