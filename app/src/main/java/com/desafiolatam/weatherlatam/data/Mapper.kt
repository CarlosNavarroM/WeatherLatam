package com.desafiolatam.weatherlatam.data

import com.desafiolatam.weatherlatam.data.local.WeatherEntity
import com.desafiolatam.weatherlatam.model.WeatherDto

fun WeatherDto.toEntity(): WeatherEntity {
    return WeatherEntity(
        id = id,
        currentTemp = currentTemp,
        maxTemp = maxTemp,
        minTemp = minTemp,
        pressure = pressure,
        humidity = humidity,
        windSpeed = windSpeed,
        sunrise = sunrise,
        sunset = sunset,
        cityName = cityName ?: "Unknown" // Provide a default value if cityName is null
    )
}

fun WeatherEntity.toDto(): WeatherDto {
    return WeatherDto(
        id = id,
        currentTemp = currentTemp,
        maxTemp = maxTemp,
        minTemp = minTemp,
        pressure = pressure,
        humidity = humidity,
        windSpeed = windSpeed,
        sunrise = sunrise,
        sunset = sunset,
        cityName = cityName
    )
}