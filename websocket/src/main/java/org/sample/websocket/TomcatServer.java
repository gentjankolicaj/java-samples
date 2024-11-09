package org.sample.websocket;

import jakarta.servlet.http.HttpServlet;
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
  private boolean welcomeServlet;
  private Class<? extends AbstractWSConfig> wsConfig;
  private HttpServlet[] servlets;

  public TomcatServer(int port) {
    this.port = port;
    this.welcomeServlet = true;
  }

  public TomcatServer(int port, boolean welcomeServlet) {
    this.port = port;
    this.welcomeServlet = welcomeServlet;
  }

  public TomcatServer(int port, Class<? extends AbstractWSConfig> wsConfig) {
    this.port = port;
    this.wsConfig = wsConfig;
  }

  public TomcatServer(int port, HttpServlet... servlets) {
    this.port = port;
    this.servlets = servlets;
  }

  private void setup() throws LifecycleException {
    tomcat = new Tomcat();
    tomcat.setPort(port); // Listen on port 80, which is used by the built-in Java App Service

    // As of Tomcat 9, the HTTP connector won't start without this call.
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

    //add websocket if config class is present
    if (wsConfig != null) {
      context.addApplicationListener(wsConfig.getName());

      //needed otherwise a default servlet is not create => not created web socket.
      tomcat.addServlet(context, "default", new DefaultServlet());
      context.addServletMappingDecoded("/", "default");
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
