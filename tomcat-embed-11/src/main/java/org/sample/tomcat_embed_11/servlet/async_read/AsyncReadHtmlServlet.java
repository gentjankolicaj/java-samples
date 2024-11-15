package org.sample.tomcat_embed_11.servlet.async_read;

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
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sample.tomcat_embed_11.servlet.TomcatServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/14/24 9:13 PM
 */
@Slf4j
public class AsyncReadHtmlServlet extends TomcatServlet {

  @Override
  public String getPattern() {
    return "/async_read_html";
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

    //read async from request
    CompletableFuture.runAsync(new ReadTask(reader));

    //write from response
    new WriteTask(writer).run();
  }


  @RequiredArgsConstructor
  static class ReadTask implements Runnable {

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
        <h1>Servlet classname: %s</h1>
        <p>Task classname: %s</p>
        <p>DateTime: %s</p>
        </body>
        </html>
        """;

    public void run() {
      log.info("writing to response:");
      String formatedHtml = String.format(html, AsyncReadHtmlServlet.class.getSimpleName(),
          getClass().getSimpleName(),
          LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      writer.println(formatedHtml);
      //flush writer after finished writing.
      writer.flush();
    }
  }


}
