package com.libmvp.delegate;

import android.os.Parcelable;
import android.view.View;

import com.libmvp.base.view.MvpView;
import com.libmvp.presenter.Presenter;

public class ViewGroupMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ViewGroupMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public ViewGroupMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onAttachedToWindow() {
        callback.getPresenter().onCreate(null);
        callback.getPresenter().attachView(callback.getMvpView());
    }

    @Override
    public void onDetachedFromWindow() {
        callback.getPresenter().detachView();
        callback.getPresenter().onDestroy();
    }

    @Override
    public void onWindowVisibilityChanges(int visibility) {
        if (visibility == View.VISIBLE) {
            callback.getPresenter().onStart();
            callback.getPresenter().onResume();
        } else if (visibility == View.GONE) {
            callback.getPresenter().onPause();
            callback.getPresenter().onStop();
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }
}
