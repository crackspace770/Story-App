package com.dicoding.mystory2.ui.login


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.mystory2.Event
import com.dicoding.mystory2.api.ApiConfig
import com.dicoding.mystory2.model.UserModel
import com.dicoding.mystory2.model.UserPreference
import com.dicoding.mystory2.ui.login.LoginActivity.Companion.IS_LOGIN
import com.dicoding.mystory2.ui.login.LoginActivity.Companion.NAME
import com.dicoding.mystory2.ui.login.LoginActivity.Companion.TOKEN
import com.dicoding.mystory2.ui.login.LoginActivity.Companion.USER_ID
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val context: Context) : ViewModel() {

    private val _noConnections = MutableLiveData<Boolean>()
    val noConnections: LiveData<Boolean> = _noConnections
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<Event<Boolean>>()
    val toast: LiveData<Event<Boolean>> = _toast

    private var sharedPreference: UserPreference? = null
    private lateinit var sharedPreferences: SharedPreferences


    fun userLogin(email: String, password: String) {
        sharedPreference = UserPreference(context)
        _isLoading.value = true
        viewModelScope.launch {
            val service = ApiConfig.getApiService().login(
                email,
                password
            )

            service.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        sharedPreference?.saveString(
                            "token",
                            "bearer " + (responseBody?.loginResult?.token)
                        )

                        if (responseBody?.error == false && responseBody.message == "success") {
                            _noConnections.value = true
                            _toast.value = Event(false)
                        }

                        if (responseBody?.error == true) {
                            _isLoading.value = false
                            _noConnections.value = false
                        }


                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                        _isLoading.value = false
                        _noConnections.value = false
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _noConnections.value = false
                    _isLoading.value = false
                    _toast.value = Event(true)
                }
            })
        }
    }

    private fun validateLogin(userId: String, name: String, token: String, isLogin: Boolean){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(NAME, name)
        editor.putString(USER_ID, userId)
        editor.putString(TOKEN, token)
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.apply()
    }

    companion object {
        val SHARED_PREFERENCES = "shared_preferences"
        val NAME = "name"
        val USER_ID = "user_id"
        val TOKEN = "token"
        val IS_LOGIN = "is_login"

        private const val TAG = "LoginViewModel"
    }

}