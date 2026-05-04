package com.example.smartroute.data.model


enum class TurnDirection {
    LEFT,
    RIGHT,
    STRAIGHT,
    ARRIVE
}

data class NavStep(
    val instruction: String,
    val distanceToNextMeters: Int,
    val remainingDistanceKm: Double,
    val remainingTimeMin: Int,
    val direction: TurnDirection,
    val isArrival: Boolean = false
)
