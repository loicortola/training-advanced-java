package com.loicortola.training.archiveaggregator.webapp.dto;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
public class UploadResponse {

  private String archiveUuid;
  private String downloadUrl;

  /**
   * Constructor.
   *
   * @param archiveUuid the archive uuid
   * @param downloadUrl the download url
   */
  public UploadResponse(String archiveUuid, String downloadUrl) {
    this.archiveUuid = archiveUuid;
    this.downloadUrl = downloadUrl;
  }

  /**
   * Default constructor.
   */
  public UploadResponse() {
  }

  public String getArchiveUuid() {
    return archiveUuid;
  }

  public void setArchiveUuid(String archiveUuid) {
    this.archiveUuid = archiveUuid;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  /*=========================================*/
  // BEGIN GENERATED CODE
  /*=========================================*/

  @Override
  public String toString() {
    return "UploadResponse{" +
        "archiveUuid='" + archiveUuid + '\'' +
        ", downloadUrl='" + downloadUrl + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UploadResponse that = (UploadResponse) o;

    if (archiveUuid != null ? !archiveUuid.equals(that.archiveUuid) : that.archiveUuid != null) return false;
    if (downloadUrl != null ? !downloadUrl.equals(that.downloadUrl) : that.downloadUrl != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = archiveUuid != null ? archiveUuid.hashCode() : 0;
    result = 31 * result + (downloadUrl != null ? downloadUrl.hashCode() : 0);
    return result;
  }
  /*=========================================*/
  // END GENERATED CODE
  /*=========================================*/

}
