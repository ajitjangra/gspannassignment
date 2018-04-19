package com.asj.gspannassignment.ui.base;

/**
 * Created by ajitjangra on 6/28/17.
 */

public class BasePresenter<V extends BaseMvpView> implements BaseMvpPresenter<V> {

  private V baseMvpView;

  @Override
  public void onAttach(final V baseMvpView) {
    this.baseMvpView = baseMvpView;
  }

  @Override
  public void onDetach() {
    baseMvpView = null;
  }
}
