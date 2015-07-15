package com.loicortola.training.archiveaggregator.common.exception;

/**
 * Created by Loïc Ortola on 16/07/2015.
 */
public class NoApiKeyProvidedException extends IllegalArgumentException {

  /**
   * Default Constructor.
   */
  public NoApiKeyProvidedException() {
    super("No ApiKey was provided. Please provide either an Api-Key header or an apikey parameter to complete your request.");
  }

}