package com.example.smartshop.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.WorkManager
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentSettingBinding
import com.example.smartshop.util.snack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private lateinit var binding: FragmentSettingBinding
    private val settingViewModel by viewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingBinding.bind(view)

        settingViewModel.workManager = WorkManager.getInstance(requireContext())

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.three -> {
                    settingViewModel.apply {
                        time = "3"
                        setTime()
                    }
                }
                R.id.five -> {
                    settingViewModel.apply {
                        time = "5"
                        setTime()
                    }
                }
                R.id.eight -> {
                    settingViewModel.apply {
                        time = "8"
                        setTime()
                    }
                }
                R.id.twelve -> {
                    settingViewModel.apply {
                        time = "12"
                        setTime()
                    }
                }

            }
        }

        binding.apply.setOnClickListener {
            if (binding.time.text.toString().isNotBlank()) {
                settingViewModel.time = binding.time.text.toString()
                settingViewModel.setTime()
            } else binding.root.snack(getString(R.string.intered_value_is_not_acceptable))
        }

        binding.stop.setOnClickListener {
            settingViewModel.stop()
        }

        binding.start.setOnClickListener {
            settingViewModel.start()
        }
    }


}