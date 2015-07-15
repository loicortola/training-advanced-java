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
public class InvalidFileNameException extends IllegalArgumentException {

  /**
   * Default Constructor.
   */
  public InvalidFileNameException() {
    super("The file name is invalid. Please provide the right file to complete request.");
  }
}
