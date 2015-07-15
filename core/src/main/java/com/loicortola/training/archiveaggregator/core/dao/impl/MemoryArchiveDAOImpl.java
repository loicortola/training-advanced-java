package com.loicortola.training.archiveaggregator.core.dao.impl;

import com.loicortola.training.archiveaggregator.common.dao.ArchiveDAO;
import com.loicortola.training.archiveaggregator.common.model.Archive;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Loïc Ortola on 07/07/2015.
 */
@Repository
public class MemoryArchiveDAOImpl implements ArchiveDAO {

  Set<Archive> archives;

  /**
   * Default Constructor.
   */
  public MemoryArchiveDAOImpl() {
    archives = Collections.synchronizedSet(new HashSet<>());
  }

  @Override
  public void save(Archive archive) {
    if (archive.getUuid() == null) {
      archive.setUuid(UUID.randomUUID().toString());
    }
    archives.add(archive);
  }

  @Override
  public Archive findOne(String archiveUuid) {
    for (Archive g : archives) {
      if (g.getUuid().equals(archiveUuid)) {
        return g;
      }
    }
    return null;
  }

  @Override
  public List<Archive> findAll() {
    return Arrays.asList((Archive[]) archives.toArray());
  }

}
