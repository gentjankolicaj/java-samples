package org.sample.tomcat_embed_11.servlet.html;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sample.tomcat_embed_11.servlet.TomcatServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/14/24 9:13 PM
 */
@Slf4j
public class HtmlServlet extends TomcatServlet {

  @Override
  public String getPattern() {
    return "/html";
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Reader reader = req.getReader();
    PrintWriter writer = resp.getWriter();

    //read from request
    new ReadTask(reader).run();

    //write  from response
    new WriteTask(writer).run();
  }


  @RequiredArgsConstructor
  static class ReadTask {

    private final Reader reader;

    public void run() {
      try {
        List<Integer> characters = new ArrayList<>();
        int character;
        while ((character = reader.read()) != -1) {
          characters.add(character);
        }
        log.info("read from request : {}", characters);
      } catch (IOException ioe) {
        log.debug("", ioe);
      }
    }
  }


  @RequiredArgsConstructor
  static class WriteTask {

    private final PrintWriter writer;
    private final String html = """
        <!DOCTYPE html>
        <html>
        <head>
        <title>tomcat-embed-11-html</title>
        </head>
        <body>
        <h1>HtmlServlet</h1>
        <p>Classname: %s</p>
        <p>DateTime: %s</p>
        </body>
        </html>
        """;

    public void run() {
      log.info("writing to response:");
      String formatedHtml = String.format(html, getClass().getName(),
          LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      writer.println(formatedHtml);
      //flush writer after finished writing.
      writer.flush();
    }
  }


}
