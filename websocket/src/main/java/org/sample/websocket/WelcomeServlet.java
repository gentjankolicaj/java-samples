package org.sample.websocket;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.Getter;

@Getter
public class WelcomeServlet extends HttpServlet {

  protected String servletName = getClass().getSimpleName();
  protected String urlPattern = "/welcome";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    PrintWriter writer = res.getWriter();
    writer.println("<html><title>Servlet :" + servletName + "</title><body>");
    writer.println("<h1>Servlet name :" + servletName + "</h1>");
    writer.println("<h3>Have a great day !!!</h3>");
    writer.println("</body></html>");
  }
}
