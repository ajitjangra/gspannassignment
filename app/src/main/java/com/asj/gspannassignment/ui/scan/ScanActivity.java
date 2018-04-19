package com.asj.gspannassignment.ui.scan;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asj.gspannassignment.R;
import com.asj.gspannassignment.adapter.BiggestFileAdapter;
import com.asj.gspannassignment.adapter.FrequentFileAdapter;
import com.asj.gspannassignment.model.BiggestFileModel;
import com.asj.gspannassignment.model.FrequentFileModel;
import com.asj.gspannassignment.ui.ScanState;
import com.asj.gspannassignment.ui.base.BaseActivity;
import com.asj.gspannassignment.utils.SearchRippleAnimation;
import com.asj.gspannassignment.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ScanActivity extends BaseActivity implements ScanMvpView {
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.fab_scan)
  FloatingActionButton fabScan;

  @BindView(R.id.rl_scanning)
  RelativeLayout rlScanning;

  @BindView(R.id.rl_scanned)
  RelativeLayout rlScanned;

  @BindView(R.id.rv_frequent_file)
  RecyclerView rvFrequentFile;

  @BindView(R.id.tv_avg_file_size)
  TextView tvAvgFileSize;

  @BindView(R.id.rv_biggest_file)
  RecyclerView rvBiggestFile;

  @BindView(R.id.search_ripple_animation)
  SearchRippleAnimation searchRippleAnimation;

  private FrequentFileAdapter frequentFileAdapter;
  private HashMap<String, Long> hmFrequentFileModel = new HashMap<>();
  private ArrayList<FrequentFileModel> alFrequentFileModel = new ArrayList<>();

  private BiggestFileAdapter biggestFileAdapter;
  private ArrayList<BiggestFileModel> alBiggestFileModel = new ArrayList<>();

  int scanState = ScanState.START;
  ScanAsyncTask scanAsyncTask;
  private int totalSize;
  private NotificationManager manager;

  private static final int PERMISSION_REQUEST_CODE = 200;

  private ScanPresenter<ScanMvpView> mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    setSupportActionBar(toolbar);
    initRecyclerView();
    handleState();
  }

  private void init() {
    ButterKnife.bind(this);

    mPresenter = new ScanPresenter<>();
    mPresenter.onAttach(ScanActivity.this);
  }

  private void initRecyclerView() {
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    rvFrequentFile.setLayoutManager(mLayoutManager);
    frequentFileAdapter = new FrequentFileAdapter(ScanActivity.this, alFrequentFileModel);
    rvFrequentFile.setAdapter(frequentFileAdapter);

    LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
    rvBiggestFile.setLayoutManager(mLayoutManager1);
    biggestFileAdapter = new BiggestFileAdapter(ScanActivity.this, alBiggestFileModel);
    rvBiggestFile.setAdapter(biggestFileAdapter);
  }

  private void handleState() {
    switch (scanState) {
      case ScanState.START:
        fabScan.setImageResource(R.drawable.ic_start_scan);
        rlScanning.setVisibility(View.VISIBLE);
        rlScanned.setVisibility(View.GONE);
        searchRippleAnimation.setVisibility(View.GONE);
        searchRippleAnimation.stopRippleAnimation();
        fabScan.setVisibility(View.VISIBLE);
        break;

      case ScanState.SCANNING:
        fabScan.setImageResource(R.drawable.ic_stop_scan);
        rlScanning.setVisibility(View.VISIBLE);
        rlScanned.setVisibility(View.GONE);
        searchRippleAnimation.setVisibility(View.VISIBLE);
        searchRippleAnimation.startRippleAnimation();
        startScan();
        fabScan.setVisibility(View.VISIBLE);
        break;

      case ScanState.SCANNED:
        fabScan.setImageResource(R.drawable.ic_start_scan);
        rlScanning.setVisibility(View.GONE);
        rlScanned.setVisibility(View.VISIBLE);
        searchRippleAnimation.setVisibility(View.GONE);
        searchRippleAnimation.stopRippleAnimation();
        fabScan.setVisibility(View.GONE);
        break;
    }

    invalidateOptionsMenu();
  }

  private void startScan() {
    scanAsyncTask = new ScanAsyncTask();
    scanAsyncTask.execute();
  }

  private void scanData(File dir) {
    File[] listFile = dir.listFiles();

    if (listFile != null && !scanAsyncTask.isCancelled()) {
      for (int i = 0; i < listFile.length && !scanAsyncTask.isCancelled(); i++) {

        if (listFile[i].isDirectory()) {
          scanData(listFile[i]);
        } else {
          File file = listFile[i];
          if (file != null) {
            String fileName = file.getName();
            String fileExtension = Utility.getFileExtension(file.getAbsolutePath());

            if (Utility.isValidFileExtension(fileExtension)) {

              long fileSize = file.length() / 1024;

              totalSize += fileSize;
              if (hmFrequentFileModel.containsKey(fileExtension)) {
                long frequency = hmFrequentFileModel.get(fileExtension);
                hmFrequentFileModel.put(fileExtension, ++frequency);
              } else {
                hmFrequentFileModel.put(fileExtension, 1l);
              }

              BiggestFileModel biggestFileModel = new BiggestFileModel();
              biggestFileModel.setFileName(fileName);
              biggestFileModel.setFileExtension(fileExtension);
              biggestFileModel.setFileSize(fileSize);

              alBiggestFileModel.add(biggestFileModel);
            }
          }
        }
      }
    }

    Collections.sort(alBiggestFileModel);
    alFrequentFileModel.addAll(mPresenter.updateFrequencyList(hmFrequentFileModel));

    tvAvgFileSize.setText(getString(R.string.avg_file_size) + " " + mPresenter.getAvgFileSize(totalSize, alBiggestFileModel) + " KB");

    frequentFileAdapter.notifyDataSetChanged();
    biggestFileAdapter.notifyDataSetChanged();
  }

  @OnClick(R.id.fab_scan)
  void scanFab() {
    if (scanState == ScanState.START || scanState == ScanState.SCANNED) {
      if (!checkPermission()) {
        requestPermission();
      } else {
        permissionGranted();
      }
    } else {
      // Scanning
      scanState = ScanState.SCANNED;
      scanAsyncTask.cancel(true);
      mPresenter.stopNotification(manager);
    }

    handleState();
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    if (scanState == ScanState.SCANNED) {
      menu.findItem(R.id.action_share).setEnabled(true);
      changeColorOfShare(menu, R.drawable.ic_share_white_24dp);
    } else {
      menu.findItem(R.id.action_share).setEnabled(false);
      changeColorOfShare(menu, R.drawable.ic_share_gray_24dp);
    }
    return true;
  }

  private void changeColorOfShare(final Menu menu, final int icon) {
    MenuItem refreshMenuItem = menu.findItem(R.id.action_share);
    VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), icon, null);
    refreshMenuItem.setIcon(vectorDrawableCompat);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    changeColorOfShare(menu, R.drawable.ic_share_gray_24dp);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_share) {
      Utility.shareStatics(ScanActivity.this, mPresenter.shareStatics(alFrequentFileModel, tvAvgFileSize.getText().toString(), alBiggestFileModel));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public class ScanAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(final Void... voids) {
      alFrequentFileModel.clear();
      alBiggestFileModel.clear();
      hmFrequentFileModel.clear();
      totalSize = 0;
      scanData(Environment.getExternalStorageDirectory());
      return null;
    }

    @Override
    protected void onPostExecute(final Void aVoid) {
      super.onPostExecute(aVoid);
      if (!isFinishing()) {
        if (!scanAsyncTask.isCancelled()) {
          scanState = ScanState.SCANNED;
        } else {
          scanState = ScanState.START;
        }
        handleState();
      }
      mPresenter.stopNotification(manager);
    }
  }

  @Override
  protected void onDestroy() {
    if (scanAsyncTask != null && !scanAsyncTask.isCancelled()) {
      scanAsyncTask.cancel(true);
    }

    if (mPresenter != null) {
      mPresenter.onDetach();
    }

    super.onDestroy();
  }

  private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
    return result == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

          if (locationAccepted) {
            permissionGranted();
          } else {
            Snackbar.make(rlScanned, "Permission Denied, You cannot perform file scan. please allow from setting.", Snackbar.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to read external storage for file scan.",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                              PERMISSION_REQUEST_CODE);
                        }
                      }
                    });
                return;
              }
            }
          }
        }

        break;
    }
  }

  private void permissionGranted() {
    scanState = ScanState.SCANNING;
    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mPresenter.startNotification(ScanActivity.this, manager);
  }
}
