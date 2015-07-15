/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.webapp.dto;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
public class ResponseMessage {
  private int statusCode;
  private String message;
  private Object result;

  /**
   * Default Constructor.
   */
  public ResponseMessage() {
  }

  /**
   * Constructs a response message without payload.
   *
   * @param statusCode the status code of the message (HttpStatus)
   * @param message    the message
   */
  public ResponseMessage(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  /**
   * Constructs a response message with payload.
   *
   * @param statusCode the status code of the message (HttpStatus)
   * @param message    the message
   * @param result     the payload object (should be Serializable somehow)
   */
  public ResponseMessage(int statusCode, String message, Object result) {
    this.statusCode = statusCode;
    this.message = message;
    this.result = result;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "ResponseMessage{" +
        "statusCode=" + statusCode +
        ", message='" + message + '\'' +
        ", result=" + result +
        '}';
  }
}
