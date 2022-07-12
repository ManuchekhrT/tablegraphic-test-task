package com.libmvp.base.view;

import com.libmvp.presenter.Presenter;

public abstract class BaseMvpFragment<V extends MvpView, P extends Presenter<V>> extends MvpFragment<V, P> {}
