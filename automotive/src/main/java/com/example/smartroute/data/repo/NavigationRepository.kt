package com.example.smartroute.data.repo

import com.example.smartroute.data.model.NavStep
import com.example.smartroute.data.model.Route
import com.example.smartroute.data.model.TurnDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NavigationRepository {

    fun getRoute(): Route =
        Route(
            name = "Morning Commute",
            start = "Home",
            destination = "Office",
            distanceKm = 12,
            etaMinutes = 25
        )

    fun getSteps(): List<NavStep> {
        return listOf(
            NavStep(
                instruction = "Turn left in 300m",
                distanceToNextMeters = 300,
                remainingDistanceKm = 12.0,
                remainingTimeMin = 25,
                direction = TurnDirection.LEFT
            ),
            NavStep(
                instruction = "Continue straight for 1 km",
                distanceToNextMeters = 1000,
                remainingDistanceKm = 10.5,
                remainingTimeMin = 21,
                direction = TurnDirection.STRAIGHT
            ),
            NavStep(
                instruction = "Turn right in 100m",
                distanceToNextMeters = 100,
                remainingDistanceKm = 4.2,
                remainingTimeMin = 9,
                direction = TurnDirection.RIGHT
            ),
            NavStep(
                instruction = "Turn left in 500m",
                distanceToNextMeters = 500,
                remainingDistanceKm = 6.2,
                remainingTimeMin = 8,
                direction = TurnDirection.LEFT),
            NavStep(
                instruction = "You have arrived at your destination",
                distanceToNextMeters = 0,
                remainingDistanceKm = 0.0,
                remainingTimeMin = 0,
                direction = TurnDirection.ARRIVE,
                isArrival = true
            )
        )
    }

    fun simulateNavigation(startIndex: Int = 0): Flow<Pair<Int, NavStep>> = flow {
        val steps = getSteps()

        for (index in startIndex until steps.size) {
            emit(index to steps[index])
            delay(7000) // longer delay feels more realistic
        }
    }
}
