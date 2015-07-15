package com.loicortola.training.archiveaggregator.common.dao;

import com.loicortola.training.archiveaggregator.common.model.Archive;

import java.util.List;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
public interface ArchiveDAO {
  /**
   * Persist archive.
   *
   * @param archive the archive
   */
  void save(Archive archive);

  /**
   * Find archive by archive uuid.
   *
   * @param archiveUuid the archive uuid
   * @return the archive
   */
  Archive findOne(String archiveUuid);

  /**
   * Find all archives.
   *
   * @return the list of all archives
   */
  List<Archive> findAll();
}
