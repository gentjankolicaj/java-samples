package org.sample.jetty_12.servlet.bytes;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/14/24 9:13 PM
 */
@Slf4j
public class BytesServlet extends HttpServlet {

  public static final String SERVLET_PATH = "/bytes";


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    printRequest(req);
    BufferedReader br = req.getReader();
    PrintWriter pw = resp.getWriter();

    //read from request
    readTask(br);

    //write  from response
    writeTask(pw);
  }


  private void printRequest(HttpServletRequest req) {
    log.info("request uri: {}", req.getRequestURI());
    log.info("request id: {}", req.getRequestId());
    log.info("request url: {}", req.getRequestURL());
    log.info("request servlet path: {}", req.getServletPath());
    log.info("request headers: {}", req.getHeaderNames());
    log.warn("request path info: {}", req.getPathInfo());
  }


  public void readTask(BufferedReader reader) {
    try {
      CharArrayWriter caw = new CharArrayWriter();
      char[] cbuf = new char[100];
      while ((reader.read(cbuf)) != -1) {
        caw.write(cbuf);
      }
      log.info("read from request : {}", caw.toCharArray());
    } catch (IOException ioe) {
      log.debug("", ioe);
    }
  }


  public void writeTask(PrintWriter writer) {
    final String html = """
        <!DOCTYPE html>
        <html>
        <head>
        <title>tomcat-embed-11-html</title>
        </head>
        <body>
        <h1> Testing reading bytes from reader/writer </h1>
        <h1>Servlet classname: %s</h1>
        <p>Task classname: %s</p>
        <p>DateTime: %s</p>
        </body>
        </html>
        """;
    log.info("writing to response:");
    String formatedHtml = String.format(html, BytesServlet.class.getSimpleName(),
        getClass().getSimpleName(),
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    writer.println(formatedHtml);
    //flush writer after finished writing.
    writer.flush();
  }


}
