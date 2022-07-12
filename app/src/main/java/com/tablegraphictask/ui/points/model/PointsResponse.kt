package com.tablegraphictask.ui.points.model

import com.google.gson.annotations.SerializedName

data class PointsResponse(
    @SerializedName("points")
    val points: List<Point>
)