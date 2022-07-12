package com.tablegraphictask.ui.points.model

import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PointsInteractorImpl @Inject constructor(
    private val pointsApi: PointsApi
) : PointsInteractor {

    override fun fetchPoints(count: Int): Observable<PointsResponse> {
        return pointsApi.fetchPoints(count);
    }

}