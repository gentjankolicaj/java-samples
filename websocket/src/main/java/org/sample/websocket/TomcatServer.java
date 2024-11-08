package org.sample.websocket;

import java.io.File;
import lombok.Getter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 3:43 PM
 */
@Getter
public class TomcatServer {

  protected String contextPath = "";
  protected String docBase = new File(".").getAbsolutePath();
  private Tomcat tomcat;
  private int port = 80;
  private boolean welcomeServlet;

  public TomcatServer(int port) {
    this.port = port;
    this.welcomeServlet = true;
  }

  public TomcatServer(int port, boolean welcomeServlet) {
    this.port = port;
    this.welcomeServlet = welcomeServlet;
  }

  private void setup() throws LifecycleException {
    tomcat = new Tomcat();
    tomcat.setPort(
        port); // Listen on port 80, which is used by the built-in Java images in App Service
    tomcat.getConnector();
    //Add a context
    Context context = tomcat.addContext(contextPath, docBase);

    if (welcomeServlet) {
      //create servlet
      WelcomeServlet welcomeServlet = new WelcomeServlet();

      //add servlet
      tomcat.addServlet(contextPath, welcomeServlet.getServletName(), welcomeServlet);

      //update context
      context.addServletMappingDecoded(welcomeServlet.getUrlPattern(),
          welcomeServlet.getServletName());
    }

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
