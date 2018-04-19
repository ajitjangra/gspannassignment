package com.asj.gspannassignment.ui.base;

/**
 * Created by ajitjangra on 6/27/17.
 */

public interface BaseMvpPresenter<V extends BaseMvpView> {
  void onAttach(V baseMvpView);

  void onDetach();
}
