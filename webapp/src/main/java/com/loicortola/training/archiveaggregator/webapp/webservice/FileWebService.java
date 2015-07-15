/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.webapp.webservice;

import com.loicortola.training.archiveaggregator.common.service.ArchiveService;
import com.loicortola.training.archiveaggregator.common.service.ContextService;
import com.loicortola.training.archiveaggregator.webapp.dto.UploadResponse;
import com.loicortola.training.archiveaggregator.common.exception.ArchiveNotReadyException;
import com.loicortola.training.archiveaggregator.common.exception.FileUploadException;
import com.loicortola.training.archiveaggregator.common.exception.NotFoundException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Loïc Ortola on 11/07/15.
 */
@RestController
@RequestMapping(value = "/file")
public class FileWebService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileWebService.class);

  @Autowired
  private ArchiveService archiveService;

  @Autowired
  private ContextService contextService;

  /**
   * Download the processed archive file located in the application resource directory.
   *
   * @param archiveUuid the archiveUuid
   * @return the content file
   * @throws java.io.IOException if file cannot be read
   * will throw @link{com.loicortola.training.archiveaggregator.common.exception.ArchiveNotReadyException} if archive is not ready yet
   */
  @RequestMapping(value = "/{archiveUuid}", method = RequestMethod.GET)
  public byte[] downloadArchive(@PathVariable("archiveUuid") String archiveUuid) throws IOException {
    LOGGER.debug("In DownloadFile");
    long startTime = System.nanoTime();

    File archive = archiveService.getFile(archiveUuid);

    if (!archive.exists()) {
      LOGGER.warn("Archive not found for archiveUuid: " + archiveUuid);
      throw new ArchiveNotReadyException();
    } else {
      byte[] result = IOUtils.toByteArray(new FileInputStream(archive));
      long delta = System.nanoTime() - startTime;
      LOGGER.debug("Took: " + delta / 1000000 + " milliseconds");
      return result;
    }
  }

  /**
   * Download a particular archive already uploaded into the tmp directory.
   *
   * @param archiveUuid the archiveUuid
   * @param fileName    the archive name (+ extension)
   * @return with the content file
   * @throws com.loicortola.training.archiveaggregator.common.exception.NotFoundException if no file is found
   * @throws java.io.IOException                                                          if file cannot be read
   */
  @RequestMapping(value = "/{archiveUuid}/{fileName:.*}", method = RequestMethod.GET)
  public byte[] downloadFile(@PathVariable String archiveUuid, @PathVariable String fileName) throws IOException {
    LOGGER.debug("In DownloadFile");
    long startTime = System.nanoTime();

    File srcFile = archiveService.getFile(archiveUuid, fileName);

    if (!srcFile.exists()) {
      LOGGER.warn("File {} not found for archiveUuid: {}", fileName, archiveUuid);
      throw new NotFoundException("File requested does not exist. Please provide a valid filename and archive id.");
    } else {
      byte[] result = IOUtils.toByteArray(new FileInputStream(srcFile));
      long delta = System.nanoTime() - startTime;
      LOGGER.debug("Took: " + delta / 1000000 + " milliseconds");
      return result;
    }
  }

  /**
   * Upload a file archive to the tmp directory.
   *
   * @param inputPart   the file
   * @param archiveUuid the archive uuid
   * @return with the upload response DTO {@link com.loicortola.training.archiveaggregator.webapp.dto.UploadResponse}
   * @throws java.lang.IllegalArgumentException                                             if archive uuid does not match any existing archive
   * @throws com.loicortola.training.archiveaggregator.common.exception.FileUploadException if an error occurs during file upload.
   */
  @RequestMapping(value = "/{archiveUuid}", method = RequestMethod.POST)
  public ResponseEntity<UploadResponse> uploadFile(@RequestParam("files[]") MultipartFile inputPart, @PathVariable String archiveUuid) {
    LOGGER.debug("In UploadFile");
    long startTime = System.nanoTime();

    String originalFileName = inputPart.getOriginalFilename();

    try {
      InputStream is = inputPart.getInputStream();
      archiveService.processUpload(originalFileName, archiveUuid, is);
    } catch (IOException e) {
      LOGGER.warn("Error in file upload:" + e);
      throw new FileUploadException(e);
    }

    // Put downloadUrl and archiveUuid into the response DTO
    UploadResponse response = new UploadResponse(archiveUuid, contextService.getDownloadUrl(archiveUuid, originalFileName));
    LOGGER.info("File saved in: " + response);
    long delta = System.nanoTime() - startTime;
    LOGGER.debug("Took: " + delta / 1000000 + " milliseconds");
    return new ResponseEntity<>(response, HttpStatus.OK);

  }

}
