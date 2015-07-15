package com.loicortola.training.archiveaggregator.core.service.impl;

import com.loicortola.training.archiveaggregator.common.dao.ArchiveDAO;
import com.loicortola.training.archiveaggregator.common.exception.ArchiveNotReadyException;
import com.loicortola.training.archiveaggregator.common.exception.NotFoundException;
import com.loicortola.training.archiveaggregator.common.exception.ProcessingErrorException;
import com.loicortola.training.archiveaggregator.common.model.Archive;
import com.loicortola.training.archiveaggregator.common.service.ArchiveService;
import com.loicortola.training.archiveaggregator.common.service.ContextService;
import com.loicortola.training.archiveaggregator.common.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveServiceImpl.class);

  @Autowired
  private ArchiveDAO archiveDAO;

  @Autowired
  private ContextService contextService;

  /**
   * List of the currently building keys.
   */
  private final List<String> currentlyBuildingKeys = new LinkedList<>();

  /**
   * Create archive directory from uuid.
   *
   * @param archiveUuid the archive uuid
   */
  private void createArchiveDirectory(String archiveUuid) {
    new File(contextService.getResourceDirectory(), archiveUuid).mkdirs();
  }

  /**
   * Clean archive directory from uuid.
   *
   * @param archiveUuid the archive uuid
   */
  private void cleanArchiveDirectory(String archiveUuid) {
    try {
      File dir = new File(contextService.getTmpDirectory(), archiveUuid);
      FileUtils.cleanDirectory(dir);
      dir.delete();
      dir = new File(contextService.getResourceDirectory(), archiveUuid);
      FileUtils.cleanDirectory(dir);
      dir.delete();
    } catch (IOException e) {
      LOGGER.error("Error while cleaning resources for archive {}: {}", archiveUuid, e.getMessage());
      throw new ProcessingErrorException(e);
    }
  }

  /**
   * Create archive from existing sources.
   *
   * @param archiveUuid the archive uuid
   * @throws IOException if an error occurs
   */
  private void createArchive(String archiveUuid) throws IOException {
    File srcDir = new File(contextService.getResourceDirectory(), archiveUuid);
    File destArchive = getFile(archiveUuid);
    ZipUtils.zip(srcDir, destArchive);
  }

  /**
   * Extract a tmp file into an archive tmp directory.
   *
   * @param archiveUuid the archive uuid
   * @param f           the resource file
   * @throws IOException if an error occurs
   */
  private void extract(String archiveUuid, File f) throws IOException {
    File destDir = new File(contextService.getResourceDirectory(), archiveUuid);
    ZipUtils.unzip(f, destDir, null, null);
  }

  /**
   * If an exception occurs during extract, the exception is stored in the database for further analysis and an email is sent to the contact address.
   *
   * @param e the exception
   */
  private void manageProcessingException(Exception e) {
    LOGGER.error("Error during processsing", e);
  }

  @Override
  public void processAsync(final Archive archive) {
    long startTime = System.nanoTime();
    LOGGER.debug("In processAsync");

    // Add building keys
    synchronized (currentlyBuildingKeys) {
      currentlyBuildingKeys.add(archive.getUuid());
    }

    // Update build attempts count in DB
    archive.incBuildAttempts();
    archive.setBuilt(false);
    archiveDAO.save(archive);

    String archiveUuid = archive.getUuid();

    createArchiveDirectory(archiveUuid);

    int archiveCount = archive.getFiles().size();

    CompletableFuture[] futures = new CompletableFuture[archiveCount];


    for (int i = 0; i < archiveCount; i++) {
      String fileName = archive.getFiles().get(i);
      File f = getFile(archiveUuid, fileName);
      futures[i] = CompletableFuture.runAsync(() -> {
        try {
          extract(archiveUuid, f);
        } catch (Exception e) {
          throw new ProcessingErrorException(e);
        }
      });
    }

    CompletableFuture.allOf(futures).thenRunAsync(() -> {
      try {
        long delta = System.nanoTime() - startTime;
        LOGGER.debug("Extraction Took: " + delta / 1000000 + " milliseconds");
        createArchive(archiveUuid);

        archive.setBuilt(true);
        archiveDAO.save(archive);
        LOGGER.debug("processAsync finished successfully");
        LOGGER.debug("Compression Took: " + (System.nanoTime() - startTime - delta) / 1000000 + " milliseconds");
      } catch (Exception e) {
        manageProcessingException(e);
        throw new ProcessingErrorException(e);
      }
      cleanArchiveDirectory(archiveUuid);
      synchronized (currentlyBuildingKeys) {
        currentlyBuildingKeys.remove(archive.getUuid());
      }
    }).exceptionally((e) -> {
      LOGGER.warn("One of the CompletableFutures returned exceptionally: " + e.getMessage());
      // Cancel all running futures
      for (CompletableFuture f : futures) {
        if (!f.isDone()) {
          f.cancel(true);
        }
      }
      // Clear directory
      synchronized (currentlyBuildingKeys) {
        currentlyBuildingKeys.remove(archiveUuid);
      }
      manageProcessingException((Exception) e);
      cleanArchiveDirectory(archiveUuid);
      return null;
    });
  }

  @Override
  public String getHTMLDownloadURL(String archiveUuid) {
    return contextService.getDownloadUrl(archiveUuid);
  }

  @Override
  public Archive findOne(String archiveUuid) {
    final Archive archive = archiveDAO.findOne(archiveUuid);
    return archive;
  }

  /**
   * Checks whether an archive exists or not.
   *
   * @param archive the archive to test
   * @return true if exists, false otherwise
   */
  private boolean archiveExists(Archive archive) {
    File destArchive = new File(contextService.getResourceDirectory(), archive.getUuid() + ".zip");
    return destArchive.exists();
  }

  @Override
  public void throwIfNotReady(String archiveUuid) {
    Archive archive = findOne(archiveUuid);
    // Archive should exists in db
    if (archiveUuid == null || archive == null) {
      throw new NotFoundException("Archive uuid does not exist.");
    }

    // The Archive is still being processed
    if (this.isBeingProcessed(archiveUuid)) {
      throw new ArchiveNotReadyException();
    }

    if (!archive.isBuilt() && archive.getBuildAttempts() > 0) {
      throw new ProcessingErrorException();
    }

    if (!archiveExists(archive)) {
      throw new NotFoundException("Archive was never built");
    }

  }

  /**
   * Check whether an archive is being processed.
   *
   * @param archiveUuid the archive uuid
   * @return true if archive is being processed, false otherwise
   */
  private boolean isBeingProcessed(String archiveUuid) {
    synchronized (currentlyBuildingKeys) {
      return currentlyBuildingKeys.contains(archiveUuid);
    }
  }

  @Override
  public void processUpload(String originalFileName, String archiveUuid, InputStream is) throws IOException {
    // Retrieve archive in DB
    Archive archive = archiveDAO.findOne(archiveUuid);

    Assert.notNull(archive); // Throws IllegalArgumentException if archive does not exist

    // Retrieve dest file
    File destDir = new File(contextService.getTmpDirectory(), archiveUuid);
    File destFile = new File(destDir, originalFileName);

    // Save file
    FileUtils.copyInputStreamToFile(is, destFile);

    // Update archive in DB with new file
    archive.addFile(originalFileName);
    archiveDAO.save(archive);
  }

  @Override
  public File getFile(String archiveUuid) {
    return new File(contextService.getResourceDirectory(), archiveUuid + ".zip");
  }

  @Override
  public File getFile(String archiveUuid, String fileName) {
    File srcDir = new File(contextService.getTmpDirectory(), archiveUuid);
    return new File(srcDir, fileName);
  }
}
