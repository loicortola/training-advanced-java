package com.loicortola.training.archiveaggregator.common.dao;

/**
 * Created by Loïc Ortola on 16/07/2015.
 */
public interface ApiKeyDAO {
  /**
   * Save apiKey.
   *
   * @param apiKey the apiKey
   */
  void save(String apiKey);

  /**
   * Check whether the apiKey exists in db or not.
   *
   * @param apiKey the apiKey
   * @return true if it exists, false otherwise
   */
  boolean exists(String apiKey);
}