package com.loicortola.training.archiveaggregator.common.service;

import com.loicortola.training.archiveaggregator.common.model.Archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
public interface ArchiveService {

  /**
   * Process an archive asynchronously.
   *
   * @param archive the archive object. See {@link com.loicortola.training.archiveaggregator.common.model.Archive}
   */
  void processAsync(Archive archive);

  /**
   * Get download url from an archive uuid.
   *
   * @param archiveUuid the archive uuid
   * @return the url
   */
  String getHTMLDownloadURL(String archiveUuid);

  /**
   * Retrieve Archive from database.
   *
   * @param archiveUuid the archiveUuid
   * @return the archive or null if none is found
   */
  Archive findOne(String archiveUuid);

  /**
   * Check whether an archive processing is ready or not.
   *
   * @param archiveUuid the archive Uuid.
   * @throws com.loicortola.training.archiveaggregator.common.exception.NotFoundException if archive is null
   */
  void throwIfNotReady(String archiveUuid);

  /**
   * Save the file input stream to a tmp file for the requested archive.
   * @param originalFileName the original uploaded file name
   * @param archiveUuid the archive uuid
   * @param is the file input stream
   * @throws IOException if an error occurs
   */
  void processUpload(String originalFileName, String archiveUuid, InputStream is) throws IOException;

  /**
   * Retrieve the archive file from an archive uuid.
   * @param archiveUuid the archive uuid
   * @return the File or null if none was found
   */
  File getFile(String archiveUuid);

  /**
   * Retrieve the archive tmp file from an archive uuid.
   * @param archiveUuid the archive uuid
   * @param fileName the original uploaded file name
   * @return the File or null if none was found
   */
  File getFile(String archiveUuid, String fileName);
}
