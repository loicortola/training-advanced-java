package com.loicortola.training.archiveaggregator.webapp.filter;

import com.loicortola.training.archiveaggregator.common.dao.ApiKeyDAO;
import com.loicortola.training.archiveaggregator.common.exception.IllegalApiKeyProvidedException;
import com.loicortola.training.archiveaggregator.common.exception.NoApiKeyProvidedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Loïc Ortola on 16/07/2015.
 */
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyInterceptor.class);

  private static final String HEADER_API_KEY = "Api-Key";
  private static final String PARAM_API_KEY = "apikey";

  @Autowired
  ApiKeyDAO apiKeyDAO;
  
  // step3: add ContextService, set value

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String apiKey = getApiKey(request);
    if (apiKey == null) {
      LOGGER.warn("No ApiKey was provided. Abort request.");
      throw new NoApiKeyProvidedException();
    }
    if (!apiKeyDAO.exists(apiKey)) {
      LOGGER.warn("Illegal ApiKey was provided. Abort request.");
      throw new IllegalApiKeyProvidedException();
    }
    return true;
  }

  /**
   * Retrieve the ApiKey from Http Request.
   * Rules are:
   * - check if header present first.
   * - if no header present, check for param
   * - otherwise, return null
   *
   * @param request the http servlet request
   * @return the api key of request
   */
  private String getApiKey(HttpServletRequest request) {
    String apiKey = request.getHeader(HEADER_API_KEY);
    if (apiKey == null) {
      apiKey = request.getParameter(PARAM_API_KEY);
    }
    return apiKey;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}