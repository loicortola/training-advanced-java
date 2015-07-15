package com.loicortola.training.archiveaggregator.webapp.webservice;

import com.loicortola.training.archiveaggregator.webapp.dto.DownloadLink;
import com.loicortola.training.archiveaggregator.common.dao.ArchiveDAO;
import com.loicortola.training.archiveaggregator.common.model.Archive;
import com.loicortola.training.archiveaggregator.common.service.ArchiveService;
import com.loicortola.training.archiveaggregator.webapp.dto.ProcessConfirmation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
@RestController
@RequestMapping("/process")
public class ProcessingWebService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingWebService.class);

  @Autowired
  private ArchiveService archiveService;

  @Autowired
  private ArchiveDAO archiveDAO;

  /**
   * Process an archive.
   *
   * @param archiveUuid the archive uuid.
   * @return ProcessConfirmation response.
   * @see ResponseEntity
   */
  @RequestMapping(value = "/{archiveUuid}", method = RequestMethod.POST)
  public ResponseEntity<ProcessConfirmation> process(@PathVariable String archiveUuid) {
    LOGGER.info("Process request received for archive " + archiveUuid);
    long startTime = System.nanoTime();
    // Unwrap objects
    Archive archive = archiveDAO.findOne(archiveUuid);

    Assert.notNull(archive);

    // Start building, send an email
    archiveService.process(archive);

    long delta = System.nanoTime() - startTime;
    LOGGER.debug("Took: " + delta / 1000000 + " milliseconds");
    return new ResponseEntity<>(new ProcessConfirmation(archive), HttpStatus.OK);

  }

  /**
   * Get the processing state for an archive uuid.
   *
   * @param archiveUuid the archive uuid
   * @return 200 if archive is ready
   * 503 if archive is still being processed
   * 500 if archive processing did not terminate correctly
   */
  @RequestMapping(value = "/state/{archiveUuid}", method = RequestMethod.GET)
  public ResponseEntity<DownloadLink> getProcessingState(@PathVariable("archiveUuid") String archiveUuid) {
    LOGGER.debug("Send processing state for archiveUuid {}", archiveUuid);
    archiveService.throwIfNotReady(archiveUuid);
    return new ResponseEntity<>(new DownloadLink(archiveService.getHTMLDownloadURL(archiveUuid)), HttpStatus.OK);
  }

  /*=========================================*/
  /*------------EXCEPTION HANDLING-----------*/
  /*=========================================*/


}