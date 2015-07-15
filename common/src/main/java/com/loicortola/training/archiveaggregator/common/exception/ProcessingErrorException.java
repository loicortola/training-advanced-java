/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.common.exception;

/**
 * Created by Loïc Ortola on 07/07/2015.
 */
public class ProcessingErrorException extends RuntimeException {

  /**
   * Default Constructor.
   */
  public ProcessingErrorException() {
  }

  /**
   * Constructor.
   * @param s the error message.
   */
  public ProcessingErrorException(String s) {
    super(s);
  }


  /**
   * Constructor.
   * @param e the wrapped exception.
   */
  public ProcessingErrorException(Exception e) {
    super(e);
  }

  /**
   * Constructor.
   * @param s the error message.
   * @param e the wrapped exception.
   */
  public ProcessingErrorException(String s, Exception e) {
    super(s, e);
  }

}
