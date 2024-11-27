package org.sample.jetty_12.servlet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.StringUtil;

/**
 * The Servlet designated responsible for Error Handling. This Servlet is designed to only respond
 * during a Dispatch with {@link DispatcherType#ERROR}
 */
public class ErrorServlet extends HttpServlet {

  public static final String SERVLET_PATH = "/error";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    if (req.getDispatcherType() != DispatcherType.ERROR) {
      // direct access of errors servlet is a 404.
      // it should only be accessed by ERROR dispatch.
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    int statusCode = (int) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    switch (statusCode) {
      case HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
        resp.setStatus(statusCode);
        String badRange = (String) req.getAttribute("Error.Bad-Range");
        if (StringUtil.isNotBlank(badRange)) {
          resp.setHeader("Content-Range", badRange);
        }
        resp.setHeader("X-Example", "Proof that Error Dispatch did what it's designed to do");
        return;
      default:
        resp.setStatus(statusCode);
    }
  }
}