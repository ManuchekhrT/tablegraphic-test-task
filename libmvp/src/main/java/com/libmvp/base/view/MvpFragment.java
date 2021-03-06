package com.libmvp.base.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.libmvp.delegate.FragmentMvpDelegate;
import com.libmvp.delegate.FragmentMvpDelegateImpl;
import com.libmvp.delegate.MvpCallback;
import com.libmvp.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpFragment<V extends MvpView, P extends Presenter<V>>
        extends Fragment
        implements MvpCallback<V, P>, MvpView {
    @Inject
    P presenter;
    private FragmentMvpDelegate<V, P> mvpDelegate = new FragmentMvpDelegateImpl<>(this);

    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpDelegate.onViewCreated(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        mvpDelegate.onPause(getActivity().isFinishing());
        super.onPause();
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mvpDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    public void onStop() {
        mvpDelegate.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        mvpDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        mvpDelegate.onDestroy(getActivity().isChangingConfigurations());
        super.onDestroy();
    }
}
