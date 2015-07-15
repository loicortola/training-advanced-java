/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.core.service.impl;

import com.loicortola.training.archiveaggregator.common.service.ContextService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;


/**
 * Created by Loïc Ortola on 11/07/2015.
 */
@Service
public class ContextServiceImpl implements ContextService {

  private static final String RESOURCES_RELATIVE_PATH = "/resources";
  private static final String TMP_RELATIVE_PATH = "/tmp";

  @Value("${application.setup.dir}")
  private String setupDir;

  @Value("${environment.base.url}")
  private String environmentBaseUrl;

  @Override
  public File getResourceDirectory() {
    return new File(setupDir + RESOURCES_RELATIVE_PATH);
  }

  @Override
  public File getTmpDirectory() {
    return new File(setupDir + TMP_RELATIVE_PATH);
  }

  @Override
  public String getDownloadUrl(String archiveUuid, String fileName) {
    return new StringBuilder(environmentBaseUrl)
        .append("/api/file/")
        .append(archiveUuid)
        .append("/")
        .append(fileName)
        .toString();
  }

  @Override
  public String getDownloadUrl(String archiveUuid) {
    return new StringBuilder(environmentBaseUrl)
        .append("/api/file/")
        .append(archiveUuid)
        .toString();
  }
  
  // step6: implement methods

}
