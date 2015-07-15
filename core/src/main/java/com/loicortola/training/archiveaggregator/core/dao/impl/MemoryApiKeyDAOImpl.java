package com.loicortola.training.archiveaggregator.core.dao.impl;

import com.loicortola.training.archiveaggregator.common.dao.ApiKeyDAO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Loïc Ortola on 16/07/2015.
 */
@Repository
public class MemoryApiKeyDAOImpl implements ApiKeyDAO {

  Set<String> apiKeys;

  /**
   * Default Constructor.
   */
  public MemoryApiKeyDAOImpl() {
    apiKeys = Collections.synchronizedSet(new HashSet<>());
  }

  @Override
  public void save(String apiKey) {
    apiKeys.add(apiKey);
  }

  @Override
  public boolean exists(String apiKey) {
    return apiKeys.contains(apiKey);
  }
}