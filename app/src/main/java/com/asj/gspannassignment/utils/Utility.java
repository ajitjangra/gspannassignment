package com.asj.gspannassignment.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.util.regex.Pattern;

public class Utility {

  public static boolean isNullOrWhiteSpace(String value) {
    return value == null || value.trim().isEmpty();
  }


  public static String getInitial(final String fileName) {
    if (!Utility.isNullOrWhiteSpace(fileName) && fileName.length() > 0) {
      return String.valueOf(fileName.charAt(0)).toUpperCase();
    }

    return "-";
  }

  public static String getFileExtension(final String filePath) {
    if (!Utility.isNullOrWhiteSpace(filePath)) {
      try {
        return filePath.substring(filePath.lastIndexOf("."));
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }

  public static boolean isValidFileExtension(final String extension) {
    if (!isNullOrWhiteSpace(extension) && extension.length() > 1 && Pattern.matches(".*[a-zA-Z]+.*", extension.substring(1))) {
      return true;
    }


    return false;

  }

  public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }

  public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      return true;
    }
    return false;
  }

  public static void shareStatics(final Context context, final String shareMsg) {
    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.addFlags((Intent.FLAG_ACTIVITY_NEW_DOCUMENT));
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
    context.startActivity(Intent.createChooser(shareIntent, "Share Statics"));
  }
}
