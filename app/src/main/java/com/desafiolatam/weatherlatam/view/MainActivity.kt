package com.desafiolatam.weatherlatam.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.desafiolatam.weatherlatam.R
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModel
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModelFactory
import com.desafiolatam.weatherlatam.WeatherApplication

class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((application as WeatherApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = loadJsonFromAssets("weather_data.json")
        json?.let {
            viewModel.loadWeatherDataFromJson(it)
            Log.d("MainActivity", "JSON data loaded")
        }
    }

    private fun loadJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            Log.e("MainActivity", "Error reading JSON file", ex)
            null
        }
    }
}
