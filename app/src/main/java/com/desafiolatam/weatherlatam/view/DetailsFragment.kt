package com.desafiolatam.weatherlatam.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.desafiolatam.weatherlatam.R
import com.desafiolatam.weatherlatam.WeatherApplication
import com.desafiolatam.weatherlatam.data.CELSIUS
import com.desafiolatam.weatherlatam.data.ITEM_ID
import com.desafiolatam.weatherlatam.databinding.FragmentDetailsBinding
import com.desafiolatam.weatherlatam.extension.toFahrenheit
import com.desafiolatam.weatherlatam.extension.toShortDateString
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModel
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class DetailsFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((activity?.application as WeatherApplication).repository)
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var weatherInfoId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        weatherInfoId = savedInstanceState?.getInt(ITEM_ID) ?: arguments?.getInt(ITEM_ID) ?: -1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToSettings()
        getWeatherData(weatherInfoId)
        editCityName()
    }

    private fun getWeatherData(id: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val unit = sharedPref.getString(getString(R.string.settings_temperature_unit), CELSIUS)

        lifecycleScope.launchWhenResumed {
            viewModel.getWeatherDataById(id).observe(viewLifecycleOwner) { weather ->
                if (weather != null) {
                    Log.d("DetailsFragment", "Weather data received: $weather")
                    binding.cityName.text = weather.cityName
                    binding.currentTemp.text = if (unit == CELSIUS) weather.currentTemp.toString() else weather.currentTemp.toFahrenheit().toString()
                    binding.maximumTemp.text = if (unit == CELSIUS) weather.maxTemp.toString() else weather.maxTemp.toFahrenheit().toString()
                    binding.minimumTemp.text = if (unit == CELSIUS) weather.minTemp.toString() else weather.minTemp.toFahrenheit().toString()
                    binding.sunrise.text = weather.sunrise.toShortDateString()
                    binding.sunset.text = weather.sunset.toShortDateString()
                } else {
                    Log.d("DetailsFragment", "No weather data found for ID: $id")
                }
            }
        }
    }

    private fun editCityName() {
        binding.cityName.setOnLongClickListener {
            val dialog = EditCityNameDialogFragment()
            dialog.show(childFragmentManager, "EditCityNameDialogFragment")
            true
        }
    }

    private fun navigateToSettings() {
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_settingsFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ITEM_ID, weatherInfoId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
