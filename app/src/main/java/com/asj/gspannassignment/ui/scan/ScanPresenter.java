package com.asj.gspannassignment.ui.scan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.asj.gspannassignment.R;
import com.asj.gspannassignment.model.BiggestFileModel;
import com.asj.gspannassignment.model.FrequentFileModel;
import com.asj.gspannassignment.ui.base.BasePresenter;
import com.asj.gspannassignment.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ajitjangra on 6/26/17.
 */

public class ScanPresenter<V extends ScanMvpView> extends BasePresenter<V> implements ScanMvpPresenter<V> {
  @Override
  public String shareStatics(final ArrayList<FrequentFileModel> alFrequentFileModel, final String avgSize, final ArrayList<BiggestFileModel> alBiggestFileModel) {
    StringBuilder msg = new StringBuilder();

    msg.append(Constant.FREQUENT_FILE_COUNT).append(" frequent file extension are \n");
    for (int i = 0; i < Constant.FREQUENT_FILE_COUNT && i < alFrequentFileModel.size(); i++) {
      msg.append(i).append(". ").append(alFrequentFileModel.get(i).getFileExtension()).append(" ( ").append(alFrequentFileModel.get(i).getFileFrequency()).append(" ) ").append("\n");
    }

    msg.append("\n\n").append(avgSize).append("\n\n");

    msg.append(Constant.BIGGEST_FILE_COUNT).append(" biggest file are \n");
    for (int i = 0; i < Constant.BIGGEST_FILE_COUNT && i < alBiggestFileModel.size(); i++) {
      msg.append(i).append(". ").append(alBiggestFileModel.get(i).getFileName()).append(" ( ").append(alBiggestFileModel.get(i).getFileSize()).append(" ) ").append("\n");
    }

    return msg.toString();
  }

  @Override
  public void startNotification(Context context, NotificationManager manager) {
    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "");
    mBuilder.setSmallIcon(R.mipmap.ic_launcher);
    mBuilder.setContentTitle("File Scanning is in progress");
    mBuilder.setContentText("File scanning for frequent extension, biggest file, avg size is in progress.");

    Intent notificationIntent = new Intent(context, ScanActivity.class);
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(contentIntent);

    manager.notify(Constant.NOTIFICATION_ID, mBuilder.build());
  }

  @Override
  public void stopNotification(NotificationManager manager) {
    if (manager != null) {
      manager.cancel(Constant.NOTIFICATION_ID);
    }
  }

  @Override
  public ArrayList<FrequentFileModel> updateFrequencyList(final HashMap<String, Long> hmFrequentFileModel) {
    Iterator it = hmFrequentFileModel.entrySet().iterator();
    final ArrayList<FrequentFileModel> alFrequentFileModel = new ArrayList<>();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();

      FrequentFileModel frequentFileModel = new FrequentFileModel();
      frequentFileModel.setFileExtension((String) pair.getKey());
      frequentFileModel.setFileFrequency((Long) pair.getValue());
      it.remove();

      alFrequentFileModel.add(frequentFileModel);
    }

    Collections.sort(alFrequentFileModel);

    return alFrequentFileModel;
  }

  @Override
  public long getAvgFileSize(final int totalSize, final ArrayList<BiggestFileModel> alBiggestFileModel) {
    return totalSize / alBiggestFileModel.size();
  }
}
