package com.asj.gspannassignment.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asj.gspannassignment.R;
import com.asj.gspannassignment.model.FrequentFileModel;
import com.asj.gspannassignment.utils.Constant;

import java.util.ArrayList;

public class FrequentFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Activity activity;
  private ArrayList<FrequentFileModel> alFrequentFileModel;
  private LayoutInflater layoutInflater;

  public FrequentFileAdapter(Activity activity, ArrayList<FrequentFileModel> alFrequentFileModel) {
    this.activity = activity;
    this.alFrequentFileModel = alFrequentFileModel;
    layoutInflater = LayoutInflater.from(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    View v = layoutInflater.inflate(R.layout.row_frequent_file, parent, false);
    return new FrequentFileViewHolder(v);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    FrequentFileModel frequentFileModel = alFrequentFileModel.get(position);

    final FrequentFileViewHolder frequentFileViewHolder = (FrequentFileViewHolder) holder;
    frequentFileViewHolder.tvExtension.setText(frequentFileModel.getFileExtension());
    frequentFileViewHolder.tvFrequency.setText(frequentFileModel.getFileFrequency() + "");
  }

  @Override
  public int getItemCount() {
    return alFrequentFileModel.size() > Constant.FREQUENT_FILE_COUNT ? Constant.FREQUENT_FILE_COUNT : alFrequentFileModel.size();
  }

  public class FrequentFileViewHolder extends RecyclerView.ViewHolder {

    final TextView tvExtension;
    final TextView tvFrequency;

    public FrequentFileViewHolder(View itemView) {
      super(itemView);
      tvExtension = itemView.findViewById(R.id.tv_extension);
      tvFrequency = itemView.findViewById(R.id.tv_frequency);
    }
  }
}
