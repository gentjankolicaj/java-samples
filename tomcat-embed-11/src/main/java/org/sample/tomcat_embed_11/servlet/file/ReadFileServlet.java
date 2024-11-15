package org.sample.tomcat_embed_11.servlet.file;


import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.sample.tomcat_embed_11.servlet.TomcatServlet;

/**
 * @author gentjan kolicaj
 * @Date: 11/15/24 9:13 PM
 */
@Slf4j
public class ReadFileServlet extends TomcatServlet {

  public static String getServerFile() {
    return ReadFileServlet.class.getResource("/file/secret_file.txt").getPath();
  }

  @Override
  public String getPattern() {
    return "/read_file";
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    printRequest(req);
    ServletInputStream sis = req.getInputStream();
    ServletOutputStream sos = resp.getOutputStream();

    //read from request
    inputTask(sis);

    //write to response
    readTask(sos);
  }

  private void printRequest(HttpServletRequest req) {
    log.info("request uri: {}", req.getRequestURI());
    log.info("request id: {}", req.getRequestId());
    log.info("request url: {}", req.getRequestURL());
    log.info("request servlet path: {}", req.getServletPath());
    log.info("request headers: {}", req.getHeaderNames());
  }

  private void inputTask(ServletInputStream sis) {
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

  private void readTask(ServletOutputStream sos) {
    try {
      final String filePath = getServerFile();
      FileInputStream fis = new FileInputStream(filePath);
      byte[] fileBytes = fis.readAllBytes();
      log.info("writing to response file: '{}' byte-array-length : {}", filePath, fileBytes.length);
      sos.write(fileBytes);
      //flush writer after finished writing.
      sos.flush();
      fis.close();
    } catch (IOException ioe) {

      log.error("", ioe);

    }
  }

}
