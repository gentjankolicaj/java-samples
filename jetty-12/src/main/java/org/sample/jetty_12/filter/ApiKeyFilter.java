package org.sample.jetty_12.filter;

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
import org.apache.commons.lang3.StringUtils;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 9:09 PM
 */
@Slf4j
public class ApiKeyFilter implements Filter {

  public static final String CUSTOM_API_KEY_HEADER_KEY = "X-API-KEY";
  public static final String CUSTOM_JETTY_HEADER_KEY = "X-Jetty-Header";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    if (servletRequest instanceof HttpServletRequest httpServletRequest) {
      //do something with http request
      log.info("servlet-path {}", httpServletRequest.getServletPath());
      log.info("request-id {}", httpServletRequest.getRequestId());
      log.info("request-uri {}", httpServletRequest.getRequestURI());
      log.info("request-url {}", httpServletRequest.getRequestURL());
      log.info("path-info {}", httpServletRequest.getPathInfo());

      //finding API_KEY header
      String apiKey = httpServletRequest.getHeader(CUSTOM_API_KEY_HEADER_KEY);
      boolean authenticated = authenticateApiKey(apiKey);

      if (servletResponse instanceof HttpServletResponse httpServletResponse) {
        if (authenticated) {
          //add new header to response
          httpServletResponse.addHeader(CUSTOM_JETTY_HEADER_KEY, "authenticated");

          //pass to filter chain
          filterChain.doFilter(servletRequest, servletResponse);
        } else {
          //add new header to response
          httpServletResponse.addHeader(CUSTOM_JETTY_HEADER_KEY, "not-authenticated");

          //do not pass to filter chain
          // filterChain.doFilter(servletRequest, servletResponse);
        }
      } else {
        throw new RuntimeException("Unknown type of response.");
      }

    } else {
      throw new RuntimeException("Unknown type of request.");
    }
  }


  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  private boolean authenticateApiKey(String apiKey) {
    log.info("API-KEY: {}", apiKey);
    return !StringUtils.isEmpty(apiKey);
  }


}
