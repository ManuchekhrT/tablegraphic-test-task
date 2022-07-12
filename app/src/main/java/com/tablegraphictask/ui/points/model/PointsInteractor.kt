package com.tablegraphictask.ui.points.model

import io.reactivex.rxjava3.core.Observable

interface PointsInteractor {
    fun fetchPoints(count: Int): Observable<PointsResponse>
}