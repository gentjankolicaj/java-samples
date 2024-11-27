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

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 9:07 PM
 */
@Slf4j
public class AddResponseHeaderFilter implements Filter {

  public static final String CUSTOM_HEADER_KEY = "X-Jetty-Header";

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

    }
    if (servletResponse instanceof ServletResponse) {
      HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

      //add new header to response
      httpServletResponse.addHeader(CUSTOM_HEADER_KEY, "was-filtered");
    }
    //pass to filter
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

}
