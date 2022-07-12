package com.libmvp.delegate;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.libmvp.base.view.MvpView;
import com.libmvp.presenter.Presenter;


public interface FragmentMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onViewCreated(@Nullable Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause(boolean isFinishing);
    void onSaveInstanceState(Bundle outState);
    void onStop();
    void onDestroyView();
    void onDestroy(boolean isChangingConfigurations);
}
