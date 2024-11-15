package org.sample.tomcat_embed_11.servlet;

import jakarta.servlet.http.HttpServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/15/24 6:59 PM
 * <p>
 * Interface to be used when setting up tomcat servlet mapping decoded.
 */
public abstract class TomcatServlet extends HttpServlet {

  /**
   * @return servlet pattern for http path (ex. /html)
   */
  public abstract String getPattern();

  /**
   * @return servlet name for container
   */
  public abstract String getName();

}
