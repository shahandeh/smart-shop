package com.example.smartshop.ui.user

import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartshop.R
import com.example.smartshop.constant.SET_USER_ID
import com.example.smartshop.data.CurrentUser
import com.example.smartshop.data.CurrentUser.email
import com.example.smartshop.data.CurrentUser.first_name
import com.example.smartshop.data.CurrentUser.last_name
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.CurrentUser.user_name
import com.example.smartshop.data.DataStoreObject.dataStore
import com.example.smartshop.databinding.FragmentUserBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.gone
import com.example.smartshop.util.snack
import com.example.smartshop.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var binding: FragmentUserBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view)

        binding.setting.setOnClickListener { navigateToSetting() }

        if (user_id != 0) {
            isLogInViewInit()
        }

        binding.signUp.setOnClickListener {
            retrieveInputText()
            if (userViewModel.sighUp()) collectUser()
            else binding.root.snack("Please fill all field!")
        }

        binding.signIn.setOnClickListener {
            retrieveInputText()
            if (userViewModel.signIn()) collectUserList()
            else binding.root.snack("Please fill all field!")
        }

        binding.signOut.setOnClickListener {
            user_id = 0
            isLogOutViewInit()
            lifecycleScope.launch {
                saveUserToDataStore(0)
            }
        }
    }

    private fun navigateToSetting() {
        val action = UserFragmentDirections.actionGlobalSettingFragment()
        findNavController().navigate(action)
    }

    private fun retrieveInputText() {
        binding.apply {
            userViewModel.firstName = firstName.editableText.toString()
            userViewModel.lastName = lastName.editableText.toString()
            userViewModel.userName = userName.editableText.toString()
            userViewModel.email = email.editableText.toString()
        }
    }

    private fun collectUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.user.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                user_id = it.value.id
                                first_name = it.value.first_name
                                last_name = it.value.last_name
                                user_name = it.value.username
                                email = it.value.email
                                isLogInViewInit()
                                saveUserToDataStore()
                                binding.root.snack("Your account is created")
                            }

                            is ResultWrapper.Failure -> {
                                binding.root.snack(it.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectUserList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.userList.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                userViewModel.list = it.value
                                if (userViewModel.userValidate()) {
                                    user_id = userViewModel.id
                                    first_name = userViewModel.firstName
                                    last_name = userViewModel.lastName
                                    user_name = userViewModel.userName
                                    email = userViewModel.email
                                    isLogInViewInit()
                                    saveUserToDataStore()
                                    binding.root.snack("You are signedIn")
                                } else binding.root.snack("User not found!")
                            }

                            is ResultWrapper.Failure -> {
                                binding.root.snack(it.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isLogInViewInit() {
        binding.apply {
            firstNameLayout.gone()
            firstName.gone()
            lastNameLayout.gone()
            lastName.gone()
            userNameLayout.gone()
            userName.gone()
            emailLayout.gone()
            email.gone()
            signIn.gone()
            signUp.gone()
            showFirstName.visible()
            showFirstName.text = first_name
            showLastName.visible()
            showLastName.text = last_name
            showUserName.visible()
            showUserName.text = user_name
            showEmail.visible()
            showEmail.text = CurrentUser.email
            signOut.visible()
        }
    }

    private fun isLogOutViewInit() {
        binding.apply {
            firstNameLayout.visible()
            firstName.visible()
            lastNameLayout.visible()
            lastName.visible()
            userNameLayout.visible()
            userName.visible()
            emailLayout.visible()
            email.visible()
            signIn.visible()
            signUp.visible()
            showFirstName.gone()
            showLastName.gone()
            showUserName.gone()
            showEmail.gone()
            signOut.gone()
        }
    }

    private suspend fun saveUserToDataStore(id: Int? = null) {
        val dataStoreKey = intPreferencesKey(SET_USER_ID)
        requireContext().dataStore.edit { setting ->
            if (id != null)
                setting[dataStoreKey] = id
            else
                setting[dataStoreKey] = userViewModel.id
        }
    }

}