package com.desafiolatam.weatherlatam.data

import com.desafiolatam.weatherlatam.model.WeatherDto
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {



    suspend fun getWeatherData(): Flow<List<WeatherDto>?>
    suspend fun getWeatherDataById(id: Int): Flow<WeatherDto?>
    suspend fun insertData(weatherDto: WeatherDto)
    suspend fun clearAll()
    suspend fun saveCityName(weatherDto: String)

}

