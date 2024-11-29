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
public class JWTFilter implements Filter {

  public static final String JWT_HEADER_KEY = "Authorization";

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
    //validate jwt from header
    String jwtValue = httpServletRequest.getHeader(JWT_HEADER_KEY);
    if (isValidJWT(jwtValue)) {
      log.info("JWT valid: {}", jwtValue);

      //Proceed to the next filter in the chain to be invoked
      chain.doFilter(request, response);
    } else {
      httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT invalid.");
      // returning immediately without invoking filter chain
    }
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  private boolean isValidJWT(String jwtValue) {
    //to split from bearer and validate token.
    return true;
  }
}
