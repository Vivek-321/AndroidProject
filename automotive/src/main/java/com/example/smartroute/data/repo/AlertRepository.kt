package com.example.smartroute.data.repo
import com.example.smartroute.data.model.AlertType
import com.example.smartroute.data.model.RoadAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AlertRepository {

    fun alerts(): Flow<RoadAlert> = flow {
        delay(1000)
        emit(RoadAlert(AlertType.TRAFFIC, "Traffic congestion ahead"))
        delay(5000)
        emit(RoadAlert(AlertType.ROAD_WORK, "Road work ahead"))

        delay(7000)
        emit(RoadAlert(AlertType.ACCIDENT, "Accident reported ahead"))
    }

}