package org.sample.jetty_12.websocket.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/29/24 1:10 PM
 */
@Slf4j
public class ApiKeyFilter implements Filter {

  public static final String API_KEY_HEADER_KEY = "X-API-KEY";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (!(request instanceof HttpServletRequest httpServletRequest
        && response instanceof HttpServletResponse httpServletResponse)) {
      throw new ServletException("non-HTTP request or response");
    }

    //==================================================
    //Do something with request
    //validate api-key from header
    String apiKeyValue = httpServletRequest.getHeader(API_KEY_HEADER_KEY);
    if (isValidAPIKEY(apiKeyValue)) {
      log.info("API-KEY valid: {}", apiKeyValue);

      //Proceed to the next filter in the chain to be invoked
      chain.doFilter(request, response);
    } else {
      httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "api-key invalid.");
      log.warn("API-KEY invalid");
    }


  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  private boolean isValidAPIKEY(String apiKey) {
    //validate api key with a db
    return true;
  }

}
