/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Archive {

  private String uuid;

  private List<String> files;

  private int buildAttempts;

  private boolean built;

  /**
   * Default constructor.
   */
  public Archive() {
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

  public boolean isBuilt() {
    return built;
  }

  public void setBuilt(boolean built) {
    this.built = built;
  }

  public int getBuildAttempts() {
    return buildAttempts;
  }

  public void setBuildAttempts(int buildAttempts) {
    this.buildAttempts = buildAttempts;
  }

  /**
   * Increments the build attemps of 1.
   */
  public void incBuildAttempts() {
    this.buildAttempts += 1;

  }

  /**
   * Add a file to the files list.
   * Will initialize the list if not set before.
   *
   * @param file the file to add
   */
  public void addFile(String file) {
    if (files == null) {
      files = new ArrayList<>();
    }
    files.add(file);
  }

  /*=========================================*/
  // BEGIN GENERATED CODE
  /*=========================================*/

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Archive that = (Archive) o;

    if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return uuid != null ? uuid.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Archive{" +
        "uuid='" + uuid + '\'' +
        ", files=" + files +
        ", buildAttempts=" + buildAttempts +
        ", built=" + built +
        '}';
  }

  /*=========================================*/
  // END GENERATED CODE
  /*=========================================*/

}
