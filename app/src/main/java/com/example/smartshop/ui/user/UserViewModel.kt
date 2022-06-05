package com.example.smartshop.ui.user

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.customer.RetrieveCustomer
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    var firstName = ""
    var lastName = ""
    var userName = ""
    var email = ""
    var id = 0
    lateinit var list: List<RetrieveCustomer>


    private var _user: MutableStateFlow<ResultWrapper<out RetrieveCustomer>> =
        MutableStateFlow(ResultWrapper.Loading)
    val user: StateFlow<ResultWrapper<out RetrieveCustomer>> =
        _user

    private var _userList: MutableStateFlow<ResultWrapper<out List<RetrieveCustomer>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val userList: StateFlow<ResultWrapper<out List<RetrieveCustomer>>> =
        _userList

    private fun createUser(createCustomer: CreateCustomer) {
        launch {
            repository.createUser(createCustomer).collect {
                _user.emit(it)
            }
        }
    }

    private fun retrieveUserList(userName: String) {
        launch {
            repository.retrieveUserList(userName).collect {
                _userList.emit(it)
            }
        }
    }

    fun sighUp(): Boolean {
        return if (
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            userName.isNotBlank() &&
            email.isNotBlank()
        ) {
            createUser(
                CreateCustomer(firstName, lastName, userName, email)
            )
            return true
        } else false
    }

    fun signIn(): Boolean {
        return if (
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            userName.isNotBlank() &&
            email.isNotBlank()
        ) {
            retrieveUserList(firstName)
            return true
        } else false
    }

    fun userValidate(): Boolean {
        for (i in list) {
            if (
                i.last_name == lastName &&
                i.username == userName &&
                i.email == email
            ) {
                firstName = i.first_name
                lastName = i.last_name
                userName = i.username
                email = i.email
                id = i.id
                return true
            }
        }
        return false
    }

}