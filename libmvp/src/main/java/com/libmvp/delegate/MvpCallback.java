package com.libmvp.delegate;

import com.libmvp.base.view.MvpView;
import com.libmvp.presenter.Presenter;

public interface MvpCallback<V extends MvpView, P extends Presenter<V>> {
    V getMvpView();
    P getPresenter();
}
