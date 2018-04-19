package com.asj.gspannassignment.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asj.gspannassignment.R;
import com.asj.gspannassignment.model.BiggestFileModel;
import com.asj.gspannassignment.utils.Constant;
import com.asj.gspannassignment.utils.Utility;

import java.util.ArrayList;

public class BiggestFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Activity activity;
  private ArrayList<BiggestFileModel> alBiggestFileModel;
  private LayoutInflater layoutInflater;

  public BiggestFileAdapter(Activity activity, ArrayList<BiggestFileModel> alBiggestFileModel) {
    this.activity = activity;
    this.alBiggestFileModel = alBiggestFileModel;
    layoutInflater = LayoutInflater.from(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    View v = layoutInflater.inflate(R.layout.row_biggest_file, parent, false);
    return new BiggestFileViewHolder(v);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    BiggestFileModel biggestFileModel = alBiggestFileModel.get(position);

    final BiggestFileViewHolder biggestFileViewHolder = (BiggestFileViewHolder) holder;
    biggestFileViewHolder.tvInitial.setText(Utility.getInitial(biggestFileModel.getFileName()));
    biggestFileViewHolder.tvName.setText(biggestFileModel.getFileName());
    biggestFileViewHolder.tvSize.setText(biggestFileModel.getFileSize() +" KB");
    biggestFileViewHolder.tvExtension.setText(biggestFileModel.getFileExtension());
  }

  @Override
  public int getItemCount() {
    return alBiggestFileModel.size() > Constant.BIGGEST_FILE_COUNT ? Constant.BIGGEST_FILE_COUNT : alBiggestFileModel.size();
  }

  public class BiggestFileViewHolder extends RecyclerView.ViewHolder {

    final TextView tvInitial;
    final TextView tvName;
    final TextView tvSize;
    final TextView tvExtension;

    public BiggestFileViewHolder(View itemView) {
      super(itemView);
      tvInitial = itemView.findViewById(R.id.tv_initial);
      tvName = itemView.findViewById(R.id.tv_name);
      tvSize = itemView.findViewById(R.id.tv_size);
      tvExtension = itemView.findViewById(R.id.tv_extension);
    }
  }
}
