package com.asj.gspannassignment.model;

import android.support.annotation.NonNull;

public class FrequentFileModel implements Comparable<FrequentFileModel> {
  private String fileExtension;
  private long fileFrequency;

  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileExtension(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public long getFileFrequency() {
    return fileFrequency;
  }

  public void setFileFrequency(final long fileFrequency) {
    this.fileFrequency = fileFrequency;
  }

  @Override
  public int compareTo(@NonNull final FrequentFileModel another) {
    if (this.getFileFrequency() > another.getFileFrequency()) {
      return -1;
    } else if (this.getFileFrequency() < another.getFileFrequency()) {
      return 1;
    }

    return 0;
  }
}
