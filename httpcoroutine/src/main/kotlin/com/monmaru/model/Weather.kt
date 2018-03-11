package com.monmaru.model

import java.util.Date;

data class Weather (
        val location: Location,
        var title: String = "",
        var link: String = "",
        var publicTime: Date,
        var description: Description,
        var forecasts: List<Forecast>,
        var pinpointLocations: List<PinpointLocation>,
        var copyright: Copyright
)

data class Copyright (
        val title: String = "",
        val link: String = "",
        val image: Image,
        val provider: List<Provider>
)

data class Location (
        val area: String = "",
        val pref: String = "",
        val city: String
)

data class Description (
        val text: String = "",
        val publicTime: Date
)

data class Image (
        val title: String = "",
        val link: String = "",
        val url: String = "",
        val width: Int,
        val height: Int
)

data class MaxMin (
        var celsius: Float?,
        var fahrenheit: Float?
)

data class Temperature (
        val max: MaxMin?,
        val min: MaxMin?
)

data class PinpointLocation (
        val link: String = "",
        val name: String = ""
)

data class Provider (
        val name: String = "",
        val link: String = ""
)

data class Forecast (
        val date: Date?,
        val dateLabel: String = "",
        val telop: String = "",
        val image: Image,
        val temperature: Temperature
)
