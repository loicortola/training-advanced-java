package com.loicortola.training.archiveaggregator.common.util;

/**
 * Created by Loïc Ortola on 14/07/2015.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to automatically describe a zip operation.
 * The scope of this class is package to prevent any external use
 */
class ZipDescriptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZipDescriptor.class);

  final File srcDir;
  final int absolutePathOffset;
  List<String> zipEntries;

  /**
   * Constructor.
   *
   * @param srcDir the source Directory which content will be compressed
   */
  public ZipDescriptor(File srcDir) {
    this.srcDir = srcDir;
    this.absolutePathOffset = srcDir.getAbsolutePath().length() + 1;
    this.zipEntries = new LinkedList<>();
  }


  /**
   * Retrieve the zip entries.
   *
   * @return the zip entries for the current descriptor
   * @throws java.io.IOException if an error occurs
   */
  public List<String> getZipEntries() throws IOException {
    try {
      listZipEntries(srcDir.toPath());
    } catch (IllegalStateException e) {
      throw new IOException(e);
    }
    return zipEntries;
  }

  /**
   * Traverse a directory and get all zip entries.
   *
   * @param nodeDir source file or directory
   */
  private void listZipEntries(Path nodeDir) {
    LOGGER.trace("List zip entries for path: " + nodeDir.toString());
    DirectoryStream<Path> ds = null;
    try {
      ds = Files.newDirectoryStream(nodeDir);
      ds.forEach((p) -> {
        if (Files.isDirectory(p)) {
          listZipEntries(p);
        } else {
          zipEntries.add(getEntry(p));
        }
      });
    } catch (IOException e) {
      LOGGER.warn("Error while listing Zip Entries: " + e.getMessage());
      throw new IllegalStateException(e);
    } finally {
      closeDirectoryStream(ds);
    }
  }

  /**
   * Secure close of the directory stream.
   *
   * @param ds the directory stream
   */
  private void closeDirectoryStream(DirectoryStream ds) {
    if (ds != null) {
      try {
        ds.close();
      } catch (IOException e) {
        LOGGER.error("Failed to close directory stream: " + e.getMessage());
      }
    }
  }

  /**
   * Retrieve the string representation for a zip entry (= relative path).
   *
   * @param p the path
   * @return the entry string representation
   */
  private String getEntry(Path p) {
    String absolutePath = p.toAbsolutePath().toString();
    return absolutePath.substring(absolutePathOffset, absolutePath.length());
  }

}
