package com.loicortola.training.archiveaggregator.webapp.exception.handler;

import com.loicortola.training.archiveaggregator.webapp.dto.ResponseMessage;
import com.loicortola.training.archiveaggregator.common.exception.ArchiveNotReadyException;
import com.loicortola.training.archiveaggregator.common.exception.FileUploadException;
import com.loicortola.training.archiveaggregator.common.exception.InvalidFileNameException;
import com.loicortola.training.archiveaggregator.common.exception.NotFoundException;
import com.loicortola.training.archiveaggregator.common.exception.ProcessingErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Loïc Ortola on 14/07/2015.
 */
@Component
@ControllerAdvice
public class SpringMvcExceptionHandler {

  /**
   * Handle IllegalArgumentException.
   * @param e exception handled
   * @return {@link ResponseEntity}
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  private static final String ERROR_CODE_ILLEGAL = "build_failed";
  private static final String ERROR_CODE_NOT_FOUND = "not_found";
  private static final String ERROR_CODE_NOT_READY = "not_ready";

  /**
   * Handle ArchiveNotReadyException.
   *
   * @param e exception handled
   * @return new response entity for a 503 service unavailable
   * @see HttpStatus#SERVICE_UNAVAILABLE
   */
  @ExceptionHandler(value = ArchiveNotReadyException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseEntity<ResponseMessage> handleArchiveNotReadyException(ArchiveNotReadyException e) {
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage(), ERROR_CODE_NOT_READY), HttpStatus.SERVICE_UNAVAILABLE);
  }

  /**
   * Handle NotFoundException.
   *
   * @param e exception handled
   * @return new response entity for 404 not found
   * @see HttpStatus#NOT_FOUND
   */
  @ExceptionHandler(value = NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ResponseMessage> handleNotFoundException(NotFoundException e) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.NOT_FOUND.value(), e.getMessage(), ERROR_CODE_NOT_FOUND), headers, HttpStatus.NOT_FOUND);
  }

  /**
   * Handle UserExistsException.
   *
   * @param e exception handled
   * @return new response entity for internal server error
   * @see HttpStatus#INTERNAL_SERVER_ERROR
   */
  @ExceptionHandler(value = ProcessingErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ResponseMessage> handleBuildErrorException(ProcessingErrorException e) {
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), ERROR_CODE_ILLEGAL), HttpStatus.INTERNAL_SERVER_ERROR);
  }


  /**
   * Handle FileUploadException.
   *
   * @param e the FileUploadException
   * @return {@link ResponseEntity}
   */
  @ExceptionHandler(value = FileUploadException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ResponseMessage> handleFileUploadException(FileUploadException e) {
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handle InvalidFileNameException.
   *
   * @param e the InvalidFileNameException
   * @return {@link ResponseEntity}
   */
  @ExceptionHandler(value = InvalidFileNameException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResponseMessage> handleInvalidFileNameException(InvalidFileNameException e) {
    return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
  }

}