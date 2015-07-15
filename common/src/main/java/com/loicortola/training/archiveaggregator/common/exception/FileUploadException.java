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
public class FileUploadException extends IllegalStateException {

  /**
   * Constructor.
   * @param e the wrapped exception.
   */
  public FileUploadException(Exception e) {
    super("An unknown error occured during file upload. Please try again.", e);
  }
}
