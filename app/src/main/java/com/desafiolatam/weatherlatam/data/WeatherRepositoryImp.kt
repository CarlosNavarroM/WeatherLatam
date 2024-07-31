package com.desafiolatam.weatherlatam.data

import android.util.Log
import com.desafiolatam.weatherlatam.data.local.WeatherDao
import com.desafiolatam.weatherlatam.data.local.WeatherEntity
import com.desafiolatam.weatherlatam.data.local.WeatherWrapper
import com.desafiolatam.weatherlatam.model.WeatherDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WeatherRepositoryImp(private val weatherDao: WeatherDao) : WeatherRepository {

    override suspend fun getWeatherData(): Flow<List<WeatherDto>?> =
        weatherDao.getWeatherData().map { it.map { entity -> entity.toDto() } }

    override suspend fun getWeatherDataById(id: Int): Flow<WeatherDto?> =
        weatherDao.getWeatherDataById(id).map { entity -> entity?.toDto() }

    override suspend fun insertData(weatherDto: WeatherDto) =
        weatherDao.insertData(weatherDto.toEntity())

    override suspend fun clearAll() = weatherDao.clearAll()

    override suspend fun saveCityName(cityName: String) {
        val weatherEntity = weatherDao.getWeatherData().firstOrNull()?.firstOrNull()
        weatherEntity?.let {
            val updatedEntity = it.copy(cityName = cityName)
            weatherDao.insertData(listOf(updatedEntity))  // Cambiar a lista
        }
    }

    suspend fun loadWeatherDataFromJson(json: String) {
        withContext(Dispatchers.IO) {
            try {
                val weatherWrapper: WeatherWrapper = Gson().fromJson(json, WeatherWrapper::class.java)
                val weatherEntity = WeatherEntity(
                    id = weatherWrapper.id.toInt(), // convertir Long a Int
                    currentTemp = weatherWrapper.main.temp,
                    maxTemp = weatherWrapper.main.tempMax, // nombre correcto
                    minTemp = weatherWrapper.main.tempMin, // nombre correcto
                    pressure = weatherWrapper.main.pressure.toDouble(),
                    humidity = weatherWrapper.main.humidity.toDouble(),
                    windSpeed = weatherWrapper.wind.speed,
                    sunrise = weatherWrapper.sys.sunrise,
                    sunset = weatherWrapper.sys.sunset,
                    cityName = weatherWrapper.name
                )
                weatherDao.insertData(listOf(weatherEntity))  // Cambiar a lista
                Log.d("WeatherRepositoryImp", "Data loaded successfully: $weatherEntity")
            } catch (e: Exception) {
                Log.e("WeatherRepositoryImp", "Error loading data from JSON", e)
            }
        }
    }
}
