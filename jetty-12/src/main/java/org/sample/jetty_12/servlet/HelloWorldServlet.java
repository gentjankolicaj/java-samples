package org.sample.jetty_12.servlet;

import jakarta.servlet.http.HttpServlet;
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

/**
 * @author gentjan kolicaj
 * @Date: 11/14/24 9:13 PM
 */
@Slf4j
public class HelloWorldServlet extends HttpServlet {

  public static final String SERVLET_PATH = "/hello";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    private final String html = "Hello World";

    public void run() {
      log.info("writing to response:");
      String formatedHtml = String.format(html, HelloWorldServlet.class.getSimpleName(),
          getClass().getSimpleName(),
          LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      writer.println(formatedHtml);
      //flush writer after finished writing.
      writer.flush();
    }
  }


}
