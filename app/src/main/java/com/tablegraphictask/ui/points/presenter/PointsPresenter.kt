package com.tablegraphictask.ui.points.presenter

import com.libmvp.presenter.Presenter
import com.tablegraphictask.ui.points.view.SelectPointsView

interface PointsPresenter : Presenter<SelectPointsView> {
    fun fetchPoints(count: Int)
}