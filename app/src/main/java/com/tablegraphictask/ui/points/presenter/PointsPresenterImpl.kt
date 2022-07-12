package com.tablegraphictask.ui.points.presenter

import com.libmvp.presenter.rxjava3.RxPresenter
import com.tablegraphictask.ui.points.model.PointsInteractor
import com.tablegraphictask.ui.points.view.SelectPointsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PointsPresenterImpl @Inject constructor(
    private val pointsInteractor: PointsInteractor,
) : RxPresenter<SelectPointsView>(), PointsPresenter {

    override fun fetchPoints(count: Int) {
        addDisposable(
            pointsInteractor
                .fetchPoints(count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.onProgress() }
                .subscribe(
                    { response -> view.displayAllPoints(response.points) },
                    { error -> view.onError(error) }
                )
        )
    }

}