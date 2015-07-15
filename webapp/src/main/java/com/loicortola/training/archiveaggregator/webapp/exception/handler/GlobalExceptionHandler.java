package com.loicortola.training.archiveaggregator.webapp.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loicortola.training.archiveaggregator.webapp.dto.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Loïc Ortola on 14/07/2015.
 * This class provides a global Exception Handler for all exceptions unhandled by spring
 *
 */
public class GlobalExceptionHandler extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  @Override
  protected void doTrace(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processError(req, res);
  }

  /**
   * Main Error processing method.
   * This wraps all exceptions into a JSON Message
   * @param req the HttpRequest
   * @param res the HttpResponse
   * @throws IOException if an error occurs
   */
  private void processError(HttpServletRequest req, HttpServletResponse res) throws IOException {

    Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
    String message = (String) req.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    if (message == null) {
      message = "Internal Server Error.";
    }
    LOGGER.error("Error received: {}, code: {}", message, statusCode);

    res.setContentType("application/json");

    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    ServletOutputStream os = res.getOutputStream();
    try {
      mapper.writeValue(os, new ResponseMessage(statusCode, message));
    } finally {
      os.close();
    }
  }
}
