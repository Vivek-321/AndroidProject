package com.example.smartroute.data.model

    data class Route(
        val name: String,
        val start: String,
        val destination: String,
        val distanceKm: Int,
        val etaMinutes: Int
    )

