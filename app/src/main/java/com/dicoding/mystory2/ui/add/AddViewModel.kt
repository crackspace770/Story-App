package com.dicoding.mystory2.ui.add

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystory2.Event
import com.dicoding.mystory2.api.ApiConfig
import com.dicoding.mystory2.model.UserPreference
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddViewModel(private val context: Context) : ViewModel() {

    private val _toast = MutableLiveData<Event<String>>()

    private val _change = MutableLiveData<Boolean>()
    val change: LiveData<Boolean> = _change

    private val _isLoading = MutableLiveData<Boolean>()


    companion object {
        private const val TAG = "AddStoryUserViewModel"
    }

    var description : String? = null
    var getFile: File? = null

    private  var sharedPreference: UserPreference? = null

    fun getFile(getFiles: File?){
        getFile = getFiles
    }

    fun getDescription(descriptions : String?){
        description = descriptions
    }

    fun uploadImage() {
        _isLoading.value = true
        sharedPreference = UserPreference(context)
        viewModelScope.launch {
            if (getFile != null) {

                val file = reduceFileImage(getFile as File)

                val description = description?.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                ApiConfig.getApiService().uploadImage(sharedPreference?.getPreferenceString("token"),
                    imageMultipart,
                    description
                )
                .enqueue(object : Callback<FileUploadResponse> {
                    override fun onResponse(
                        call: Call<FileUploadResponse>,
                        response: Response<FileUploadResponse>
                    ) {
                        _isLoading.value = false

                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                _change.value = true
                                _isLoading.value = false
                                _toast.value = Event("Success")

                            }

                        } else {
                            _toast.value = Event("Invalid")
                            _change.value = false
                            _isLoading.value = false
                            Log.e(TAG, "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                        if (t.message.equals("Timeout")) {
                            _toast.value = Event("Timeout")
                        } else {
                            _toast.value = Event("Failed")
                        }
                        _change.value = false
                        _isLoading.value = false
                        Log.e(TAG, "onResponse: ${t.message}")
                    }
                })
            } else {
                _toast.value = Event("Input your file")
                _change.value = false
                _isLoading.value = false
            }
        }

    }

}