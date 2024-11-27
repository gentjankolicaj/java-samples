package org.sample.jetty_12.servlet.bytes;


import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/15/24 9:13 PM
 */
@Slf4j
public class InputStreamServlet extends HttpServlet {

  protected static final String SERVLET_PATH = "/input_stream";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    printRequest(req);
    ServletInputStream sis = req.getInputStream();
    ServletOutputStream sos = resp.getOutputStream();

    //read from request
    inputTask(sis);

    //write  from response
    outputTask(sos);
  }


  private void printRequest(HttpServletRequest req) {
    log.info("request uri: {}", req.getRequestURI());
    log.info("request id: {}", req.getRequestId());
    log.info("request url: {}", req.getRequestURL());
    log.info("request servlet path: {}", req.getServletPath());
    log.info("request headers: {}", req.getHeaderNames());
    log.warn("request path info: {}", req.getPathInfo());
  }


  public void inputTask(ServletInputStream sis) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      while ((sis.read(buffer)) != -1) {
        baos.write(buffer);
      }
      log.info("read from request byte-array : {}", baos.toByteArray());
    } catch (IOException ioe) {
      log.debug("", ioe);
    }
  }


  public void outputTask(ServletOutputStream sos) {
    try {
      final String html = """
          <!DOCTYPE html>
          <html>
          <head>
            <title>jetty-12-html</title>
          </head>
          <body>
          <h1>Testing byte streams from ServletInputStream/ServletOutputStream</h1>
          <h1>Servlet classname: %s</h1>
          <p>Task classname: %s</p>
          <p>DateTime: %s</p>
          </body>
          </html>
          """;
      String formatedHtml = String.format(html, InputStreamServlet.class.getSimpleName(),
          getClass().getSimpleName(),
          LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      byte[] message = formatedHtml.getBytes();
      log.info("writing to response byte-array : {}", message);

      sos.write(message);
      //flush writer after finished writing.
      sos.flush();
    } catch (IOException ioe) {

      log.error("", ioe);

    }
  }


}
