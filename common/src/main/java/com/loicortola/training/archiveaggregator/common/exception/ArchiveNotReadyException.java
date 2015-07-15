package com.loicortola.training.archiveaggregator.common.exception;

/**
 * Created by Loïc Ortola on 07/07/2015.
 */
public class ArchiveNotReadyException extends IllegalStateException {
  /**
   * Constructor.
   */
  public ArchiveNotReadyException() {
    super("Archive is not ready yet. Please check that the process is finished before attempting to download the output archive.");
  }
}
