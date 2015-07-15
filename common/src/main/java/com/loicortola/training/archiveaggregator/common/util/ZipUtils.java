/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Loïc Ortola on 13/07/2015.
 */
public abstract class ZipUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

  /**
   * Extract Zip file in destDir folder.
   *
   * @param zipFile         input zip File
   * @param destDir         output directory
   * @param validExtensions the list of valid extensions
   * @param errorFiles      a reference to a list of errorFiles if any
   * @throws java.io.IOException in case of an I/O error
   */
  public static void unzip(File zipFile, File destDir, List<String> validExtensions, List<String> errorFiles) throws IOException {
    LOGGER.debug("Entering unzip for file " + zipFile.getName());
    byte[] buffer = new byte[1024];

    ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFile));
    String extRegex = null;
    if (validExtensions != null && !validExtensions.isEmpty()) {
      extRegex = StringUtils.stringListBuilder(validExtensions, "(", "|", ")");
    }

    boolean validFile = false;
    ZipEntry ze = null;
    FileOutputStream fos = null;
    try {
      while ((ze = zip.getNextEntry()) != null) {

        String fileName = ze.getName();
        String fExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // If file is not at the root of the zip file, retrieve its fileName
        if ((fileName.lastIndexOf("/")) != -1) {
          fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }

        File newFile = new File(destDir, ze.getName());

        LOGGER.trace("Extracting " + newFile.getAbsolutePath());

        if (ze.isDirectory()) {
          LOGGER.trace("Mkdir to " + newFile.getAbsolutePath());
          newFile.mkdirs();
        } else {
          validFile = StringUtils.isValidFileName(StringUtils.getNameWithoutExtension(fileName))
              && (extRegex == null || fExtension.matches(extRegex));

          if (!validFile && errorFiles != null) {
            errorFiles.add(fileName);
          } else {
            LOGGER.trace("Writing only valid files to " + newFile.getAbsolutePath());
            if (newFile.exists()) {
              newFile.delete();
            }
            new File(newFile.getParent()).mkdirs();
            fos = new FileOutputStream(newFile);
            try {
              int len;
              while ((len = zip.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
              }
              zip.closeEntry();
            } finally {
              fos.close();
            }
          }
        }
      }
    } finally {
      closeZipInputStream(zip);
    }
    LOGGER.debug("Leaving unzip");
  }

  /**
   * Compress a temp folder to a zip file.
   *
   * @param srcDir      the source directory
   * @param destZipFile the output zip File
   * @throws java.io.IOException in case of an I/O error
   */
  public static void zip(File srcDir, File destZipFile) throws IOException {
    LOGGER.debug("Entering zip");
    byte[] buffer = new byte[1024];

    FileOutputStream fos = null;
    ZipOutputStream zos = null;
    FileInputStream in = null;

    try {
      fos = new FileOutputStream(destZipFile);
      zos = new ZipOutputStream(fos);
      ZipDescriptor zd = new ZipDescriptor(srcDir);
      String srcAbsolutePath = srcDir.getAbsolutePath();
      List<String> zipEntries = zd.getZipEntries();

      for (String entry : zipEntries) {
        LOGGER.trace("File Added : " + entry);
        ZipEntry ze = new ZipEntry(entry);
        zos.putNextEntry(ze);
        in = new FileInputStream(srcAbsolutePath + File.separator + entry);
        int len;
        while ((len = in.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
        in.close();
        zos.closeEntry();
      }
    } finally {
      closeFileInputStream(in);
      closeZipEntry(zos);
      closeZipOutputStream(zos);
    }
    LOGGER.debug("Leaving zip");
  }


  /**
   * Secure close of a zip input stream.
   *
   * @param zip the zip input stream
   */
  private static void closeZipInputStream(ZipInputStream zip) {
    if (zip != null) {
      try {
        zip.close();
      } catch (IOException e) {
        LOGGER.error("Failed to close zip input stream: " + e.getMessage());
      }
    }
  }

  /**
   * Secure close of a zip entry.
   *
   * @param zos the zip output stream
   */
  private static void closeZipEntry(ZipOutputStream zos) {
    try {
      if (zos != null) {
        zos.closeEntry();
      }
    } catch (IOException e) {
      LOGGER.error("Failed to close zip entry: " + e.getMessage());
    }
  }

  /**
   * Secure close of the zip output stream.
   *
   * @param zos the zip output stream
   */
  private static void closeZipOutputStream(ZipOutputStream zos) {
    try {
      if (zos != null) {
        zos.close();
      }
    } catch (IOException e) {
      LOGGER.error("Failed to close zip output stream: " + e.getMessage());
    }
  }

  /**
   * Secure close of the file input stream.
   *
   * @param in the file input stream
   */
  private static void closeFileInputStream(FileInputStream in) {
    try {
      if (in != null) {
        in.close();
      }
    } catch (IOException e) {
      LOGGER.error("Failed to close inputstream: " + e.getMessage());
    }
  }


}
