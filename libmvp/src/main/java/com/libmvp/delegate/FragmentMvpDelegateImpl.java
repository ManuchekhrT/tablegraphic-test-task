package com.libmvp.delegate;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.libmvp.base.view.MvpView;
import com.libmvp.presenter.Presenter;
import com.libmvp.presenter.PresenterBundle;
import com.libmvp.presenter.PresenterBundleUtil;

public class FragmentMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements FragmentMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public FragmentMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().onCreate(PresenterBundleUtil.getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onViewCreated(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().attachView(callback.getMvpView());
    }

    @Override
    public void onStart() {
        callback.getPresenter().onStart();
    }

    @Override
    public void onResume() {
        callback.getPresenter().onResume();
    }

    @Override
    public void onPause(boolean isFinishing) {
        callback.getPresenter().onPause();
        if (isFinishing) {
            callback.getPresenter().detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        PresenterBundle bundle = new PresenterBundle();
        callback.getPresenter().onSaveInstanceState(bundle);
        PresenterBundleUtil.setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroyView() {
        callback.getPresenter().detachView();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        if (!isChangingConfigurations) {
            callback.getPresenter().onDestroy();
        }
    }
}
