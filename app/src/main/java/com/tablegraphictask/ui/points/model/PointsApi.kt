package com.tablegraphictask.ui.points.model

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {
    @GET("api/test/points")
    fun fetchPoints(@Query("count") count: Int): Observable<PointsResponse>
}