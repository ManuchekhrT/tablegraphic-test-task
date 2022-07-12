package com.libmvp.delegate;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.libmvp.base.view.MvpView;
import com.libmvp.presenter.Presenter;
import com.libmvp.presenter.PresenterBundle;
import com.libmvp.presenter.PresenterBundleUtil;


public class ActivityMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ActivityMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public ActivityMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().onCreate(
                PresenterBundleUtil.getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onContentChanged() {
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
        // If activity is finishing then mark view as detached.
        // Otherwise there is possibility that view will still receive
        // some event while another activity already in foreground.
        if (isFinishing) {
            callback.getPresenter().detachView();
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        PresenterBundle bundle = new PresenterBundle();
        // Get presenter bundle
        callback.getPresenter().onSaveInstanceState(bundle);
        // Write presenter bundle to view bundle
        PresenterBundleUtil.setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        callback.getPresenter().detachView();
        // If activity is destroyed then notify presenter about that.
        if (!isChangingConfigurations) {
            callback.getPresenter().onDestroy();
        }
    }
}
