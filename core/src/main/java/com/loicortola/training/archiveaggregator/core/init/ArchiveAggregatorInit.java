package com.loicortola.training.archiveaggregator.core.init;

import com.loicortola.training.archiveaggregator.common.dao.ApiKeyDAO;
import com.loicortola.training.archiveaggregator.common.service.ContextService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Loïc Ortola on 14/07/2015.
 */
@Component
public class ArchiveAggregatorInit {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveAggregatorInit.class);

  private enum Environment {
    PROD, STAGING, INTEGRATION, DEV
  }

  @Autowired
  ContextService contextService;

  @Autowired
  ApiKeyDAO apiKeyDAO;

  @Value("${environment}")
  String environment;

  /**
   * Called after Spring context is initialized.
   * Will initialize the archive aggregator application specific dependencies
   */
  @PostConstruct
  public void init() {
    LOGGER.info("----Initializing Archive Aggregator----");
    assertValidEnvironment();
    LOGGER.info("-* Running environment is: " + environment);
    LOGGER.info("-* Init work directories");
    initDirectories();
    if (isDev()) {
      LOGGER.info("-* Clear temporary data");
      clearDirectories();
    }
    LOGGER.info("-* Init apiKey entries");
    initApiKeyEntries();
    LOGGER.info("----Archive Aggregator Init Success----");
  }

  /**
   * Init all ApiKey entries.
   * TODO: should be externalized into static files
   */
  private void initApiKeyEntries() {
    apiKeyDAO.save("default");
    apiKeyDAO.save("e20ae86622bcb64237ae04c250c9d191cbb6dc30");
  }

  /**
   * Check whether the environment property has a valid value or not.
   *
   * @throws IllegalArgumentException if the value is invalid.
   */
  private void assertValidEnvironment() {
    environment = environment.trim().toUpperCase();
    Environment.valueOf(environment);
  }

  /**
   * Check whether we are running on a development environment or not.
   *
   * @return true if development environment, false otherwise
   */
  private boolean isDev() {
    return environment.equals(Environment.DEV.toString());
  }

  /**
   * Clear all previous recorded data. Only called in development environments.
   */
  private void clearDirectories() {
    try {
      FileUtils.cleanDirectory(contextService.getResourceDirectory());
      FileUtils.cleanDirectory(contextService.getTmpDirectory());
    } catch (IOException e) {
      LOGGER.error("Error during directory clean: " + e.getMessage());
      throw new IllegalStateException(e);
    }
  }

  /**
   * Init all base directories if not present.
   */
  private void initDirectories() {
    contextService.getResourceDirectory().mkdirs();
    contextService.getTmpDirectory().mkdirs();
  }
}