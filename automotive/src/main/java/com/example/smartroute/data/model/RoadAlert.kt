package com.example.smartroute.data.model


enum class AlertType {
    TRAFFIC, ROAD_WORK, ACCIDENT
}

data class RoadAlert(
    val type: AlertType,
    val message: String
)
