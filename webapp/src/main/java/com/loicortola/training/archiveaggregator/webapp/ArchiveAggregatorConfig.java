package com.loicortola.training.archiveaggregator.webapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Loïc Ortola on 07/07/2015.
 * Spring boot App configuration for the Webapp. Replaces applicationContext.xml
 */
// Scan beans
// Warning: resourcePattern is NOT recursive. You will only scan the declared packages.
// We do this to delegate the recursive scan to specific extensions, to avoid NoClassDefFoundError on cross-dependencies (typically with persistence)
@Configuration
@ComponentScan(basePackages = {
    "com.loicortola.training.archiveaggregator.common.util.impl",
    "com.loicortola.training.archiveaggregator.core.dao.impl",
    "com.loicortola.training.archiveaggregator.core.init",
    "com.loicortola.training.archiveaggregator.core.service.impl"
}, resourcePattern = "*.class")
// Load properties
@PropertySources({
    @PropertySource("classpath:webapp-profile.properties"),
    @PropertySource("classpath:archive-aggregator-webapp.properties"),
    @PropertySource(value = "classpath:env-archive-aggregator-webapp.properties", ignoreResourceNotFound = true)
})
public class ArchiveAggregatorConfig extends SpringBootServletInitializer {

  /**
   * Bean.
   * Used for property mapping support
   * @return The property sources configurer
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * Spring application launcher.
   * Useful when multiple sources are used (only one here)
   * @param application the springApplicationBuilder
   * @return the springApplicationBuilder
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ArchiveAggregatorConfig.class);
  }

}
