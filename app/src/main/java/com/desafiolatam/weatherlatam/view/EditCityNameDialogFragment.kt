package com.desafiolatam.weatherlatam.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.desafiolatam.weatherlatam.WeatherApplication
import com.desafiolatam.weatherlatam.databinding.DialogEditCityNameBinding
import com.desafiolatam.weatherlatam.extension.setupWidthToMatchParent
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModel
import com.desafiolatam.weatherlatam.view.viewmodel.WeatherViewModelFactory

class EditCityNameDialogFragment : DialogFragment() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((activity?.application as WeatherApplication).repository)
    }

    private lateinit var binding: DialogEditCityNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditCityNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWidthToMatchParent()

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnSave.setOnClickListener {
            saveCityName(binding.etCityName.text.toString())
            dismiss()
        }
    }

    private fun saveCityName(cityName: String) {
        viewModel.saveCityName(cityName)
    }
}
