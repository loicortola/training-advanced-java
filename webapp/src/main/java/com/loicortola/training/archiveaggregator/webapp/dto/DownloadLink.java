package com.loicortola.training.archiveaggregator.webapp.dto;

/**
 * Created by Loïc Ortola on 14/07/2015.
 */
public class DownloadLink {
  private String downloadUrl;

  /**
   * Default Constructor.
   */
  public DownloadLink() {

  }

  /**
   * Constructor.
   *
   * @param downloadUrl the download url
   */
  public DownloadLink(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
