package com.tablegraphictask.ui.points.view

import com.libmvp.base.view.ErrorMvpView
import com.libmvp.base.view.ProgressMvpView
import com.tablegraphictask.ui.points.model.Point

interface SelectPointsView : ErrorMvpView, ProgressMvpView {
    fun displayAllPoints(points: List<Point>)
}