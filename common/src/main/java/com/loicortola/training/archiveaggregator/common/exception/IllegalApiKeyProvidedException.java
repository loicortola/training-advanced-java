package com.loicortola.training.archiveaggregator.common.exception;

/**
 * Created by Loïc Ortola on 16/07/2015.
 */
public class IllegalApiKeyProvidedException extends IllegalArgumentException {

  /**
   * Default Constructor.
   */
  public IllegalApiKeyProvidedException() {
    super("An invalid ApiKey was provided. Please provide a valid Api-Key to complete your request.");
  }

}