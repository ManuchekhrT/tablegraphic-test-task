package com.libmvp.base.view;

import androidx.annotation.NonNull;

public interface ErrorMvpView extends MvpView {
    void onError(@NonNull Throwable error);
}
