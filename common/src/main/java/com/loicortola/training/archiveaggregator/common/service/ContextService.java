package com.loicortola.training.archiveaggregator.common.service;

import java.io.File;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
public interface ContextService {

  /**
   * @return the resource directory
   */
  File getResourceDirectory();

  /**
   * @return the tmp directory
   */
  File getTmpDirectory();

  /**
   * Compute the archive DownloadUrl for the current environment.
   * N.B.: This url represents one of the tmp uploaded files.
   *
   * @param archiveUuid the archive uuid
   * @param fileName    the original tmp file name.
   * @return the download url
   */
  String getDownloadUrl(String archiveUuid, String fileName);

  /**
   * Compute the archive DownloadUrl for the current environment.
   * N.B.: This url represents the final archive once processed.
   *
   * @param archiveUuid the archive uuid
   * @return the download url
   */
  String getDownloadUrl(String archiveUuid);
  
  // step2: use tl1 here
}
