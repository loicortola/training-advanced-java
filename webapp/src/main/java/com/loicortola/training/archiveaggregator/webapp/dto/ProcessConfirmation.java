package com.loicortola.training.archiveaggregator.webapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loicortola.training.archiveaggregator.common.model.Archive;

/**
 * Created by Loïc Ortola on 11/07/2015.
 * ProcessConfirmation is sent to the client while
 * the archive is being processed.
 * It contains the info related to the archive currently being processed.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProcessConfirmation {

  private final String message;

  private final Archive archive;

  /**
   * Constructor.
   *
   * @param archive the archive being built
   */
  public ProcessConfirmation(Archive archive) {
    this.archive = archive;
    this.message = "Your app generation was launched successfully. Please wait. You can call [GET]{API}/process/state to check for current status.";
  }

  /*=========================================*/
  /*------------GETTER/SETTER----------------*/
  /*=========================================*/

  public Archive getArchive() {
    return archive;
  }

  public String getMessage() {
    return message;
  }
}
