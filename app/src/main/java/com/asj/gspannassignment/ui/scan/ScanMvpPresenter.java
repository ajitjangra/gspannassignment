package com.asj.gspannassignment.ui.scan;

import android.app.NotificationManager;
import android.content.Context;

import com.asj.gspannassignment.model.BiggestFileModel;
import com.asj.gspannassignment.model.FrequentFileModel;
import com.asj.gspannassignment.ui.base.BaseMvpPresenter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ajitjangra on 6/26/17.
 */

public interface ScanMvpPresenter<V extends ScanMvpView> extends BaseMvpPresenter<V> {
  String shareStatics(ArrayList<FrequentFileModel> alFrequentFileModel, String avgSize, ArrayList<BiggestFileModel> alBiggestFileModel);

  void startNotification(Context context, NotificationManager manager);

  void stopNotification(NotificationManager manager);

  ArrayList<FrequentFileModel> updateFrequencyList(final HashMap<String, Long> hmFrequentFileModel);

  long getAvgFileSize(final int totalSize, final ArrayList<BiggestFileModel> alBiggestFileModel);
}
