package com.desafiolatam.weatherlatam.view.viewmodel

import androidx.lifecycle.*
import com.desafiolatam.weatherlatam.data.WeatherRepositoryImp
import com.desafiolatam.weatherlatam.model.WeatherDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepositoryImp) : ViewModel() {
    val weatherData: LiveData<List<WeatherDto>> = liveData(Dispatchers.IO) {
        repository.getWeatherData().collect { data ->
            emit(data ?: emptyList())
        }
    }

    fun loadWeatherDataFromJson(json: String) {
        viewModelScope.launch {
            try {
                repository.loadWeatherDataFromJson(json)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun saveCityName(cityName: String) {
        viewModelScope.launch {
            repository.saveCityName(cityName)
        }
    }

    fun getWeatherDataById(id: Int): LiveData<WeatherDto?> = liveData {
        repository.getWeatherDataById(id).collect { data ->
            emit(data)
        }
    }
}
