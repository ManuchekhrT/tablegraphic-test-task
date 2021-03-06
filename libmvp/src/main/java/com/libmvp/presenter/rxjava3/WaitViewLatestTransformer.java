package com.libmvp.presenter.rxjava3;

import java.util.Optional;

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
import io.reactivex.rxjava3.functions.Function;

/**
 * {@link ObservableTransformer} that ties upstream {@link Observable} emission  to Observable representing view status
 *
 * If view is attached (latest emitted value from view observable is true) then values from upstream observable
 * propagate as usual.
 *
 * If view is detached (latest emitted value from view observable is false) then values from upstream
 * Observable propagate using following rules:
 * <ul>
 *     <li>If upstream Observable emits onError then it would be delivered after view is attached</li>
 *     <li>If upstream Observable emits onComplete then after view is attached most recent onNext value from
 *     upstream Observable is delivered followed by onComplete event</li>
 *     <li>If upstream Observable emits multiple values then after view is attached most recent emitted value
 *     is delivered followed by subsequent observed items as usual</li>
 * </ul>
 *
 */
public class WaitViewLatestTransformer<T> implements
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        CompletableTransformer,
        ObservableTransformer<T, T> {

    private final Observable<Boolean> view;

    public WaitViewLatestTransformer(Observable<Boolean> view) {
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
        return Observable
            .combineLatest(
                view,
                upstream
                    // Materialize upstream Observable to handle onError and onComplete events when view is detached
                    .materialize()
                    // If this is onNext notification then emit it immediately
                    // If this is onComplete notification then delay emission of this notification while view is detached
                    .delay((Function<Notification<T>, Observable<Boolean>>) notification -> {
                        if (!notification.isOnComplete()) {
                            return Observable.just(true);
                        } else {
                            return view.filter(value -> value);
                        }
                    }),
                (flag, notification) -> flag ? Optional.of(notification)
                    : Optional.<Notification<T>>ofNullable(null))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .dematerialize(notification -> notification);
    }

}
