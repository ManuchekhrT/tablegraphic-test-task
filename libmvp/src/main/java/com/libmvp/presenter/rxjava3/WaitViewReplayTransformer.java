package com.libmvp.presenter.rxjava3;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Notification;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class WaitViewReplayTransformer<T> implements
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        CompletableTransformer,
        ObservableTransformer<T, T> {

    private final Observable<Boolean> view;

    public WaitViewReplayTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return ((Observable<T>) apply(upstream.toObservable())).singleElement();
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return ((Observable<T>) apply(upstream.toObservable())).singleOrError();
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return ((Observable<T>) apply(upstream.<T>toObservable())).ignoreElements();
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        final ReplaySubject<Notification<T>> subject = ReplaySubject.create();
        final DisposableObserver<Notification<T>> observer = upstream.materialize()
            .subscribeWith(new DisposableObserver<Notification<T>>() {
                @Override
                public void onComplete() {
                    subject.onComplete();
                }

                @Override
                public void onError(Throwable e) {
                    subject.onError(e);

                }
                @Override
                public void onNext(Notification<T> value) {
                    subject.onNext(value);
                }
            });

        return view
            .switchMap(flag -> {
                if (flag) {
                    return subject;
                } else {
                    return Observable.empty();
                }
            })
            .doOnDispose(observer::dispose)
            .dematerialize(notification -> notification);
    }

}
