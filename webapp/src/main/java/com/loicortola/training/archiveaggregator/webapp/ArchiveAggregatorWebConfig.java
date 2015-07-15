package com.loicortola.training.archiveaggregator.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loïc Ortola on 07/07/2015.
 * Spring boot MVC App configuration for the Webapp. Replaces spring-servlet.xml
 */
@EnableWebMvc
@Configuration
// Scan beans
@ComponentScan({
    "com.loicortola.training.archiveaggregator.webapp.webservice",
    "com.loicortola.training.archiveaggregator.webapp.exception.handler"
})
public class ArchiveAggregatorWebConfig extends WebMvcConfigurerAdapter {
  /**
   * Bean.
   * Used to delegate unhandled requests by forwarding to the Servlet container's "default" servlet.
   *
   * @param configurer enable default servlet handler configurer
   */
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  /**
   * Bean.
   *
   * @return The property sources configurer
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * Bean.
   * Used to support multipart file uploads
   *
   * @return The Commons multipart resolver
   */
  @Bean
  public CommonsMultipartResolver multipartResolver() {
    return new CommonsMultipartResolver();
  }

  /**
   * Bean.
   * Register jackson converter, and byteArray converter
   *
   * @param converters the message converters
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    // Allow jackson to process text-plain conversions
    List<MediaType> mediaTypes = new ArrayList<>();
    mediaTypes.add(MediaType.APPLICATION_JSON);
    mediaTypes.add(MediaType.TEXT_PLAIN);
    jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);

    // Add converters
    converters.add(new ByteArrayHttpMessageConverter());
    converters.add(jackson2HttpMessageConverter);
  }

}
