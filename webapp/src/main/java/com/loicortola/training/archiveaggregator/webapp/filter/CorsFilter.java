package com.loicortola.training.archiveaggregator.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Loïc Ortola on 15/07/2015.
 */
public class CorsFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(CorsFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    LOGGER.debug("In CorsFilter");

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    // Add CORS Headers to every response
    resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    resp.setHeader("Access-Control-Max-Age", "3600");
    resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Api-Key, Authorization");
    resp.setHeader("Access-Control-Allow-Origin", "*");

    if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
      // Return immediately with 200-OK if OPTIONS for CORS
      LOGGER.debug("OPTIONS Request: immediate return");
    } else {
      // For all other requests, process to next steps
      chain.doFilter(req, resp);
    }

  }

  @Override
  public void destroy() {

  }
}