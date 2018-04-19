package com.asj.gspannassignment.model;

import android.support.annotation.NonNull;

public class BiggestFileModel implements Comparable<BiggestFileModel> {
  private String fileName;
  private String fileExtension;
  private long fileSize;

  public String getFileName() {
    return fileName;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public void setFileExtension(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(final long fileSize) {
    this.fileSize = fileSize;
  }

  @Override
  public int compareTo(@NonNull final BiggestFileModel another) {

    if (this.getFileSize() > another.getFileSize()) {
      return -1;
    } else if (this.getFileSize() < another.getFileSize()) {
      return 1;
    }

    return 0;
  }
}
