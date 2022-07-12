package com.tablegraphictask.ui.points.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Point(
    @SerializedName("x")
    val pX: Float,
    @SerializedName("y")
    val pY: Float
) : Serializable