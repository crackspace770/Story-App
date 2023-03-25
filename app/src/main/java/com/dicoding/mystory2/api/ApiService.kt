package com.dicoding.mystory2.api

import com.dicoding.mystory2.ui.main.StoriesResponse
import com.dicoding.mystory2.ui.add.FileUploadResponse
import com.dicoding.mystory2.ui.login.LoginResponse
import com.dicoding.mystory2.ui.maps.MapResponse
import com.dicoding.mystory2.ui.signup.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getListStories(
        @Header("Authorization")
        authHeader: String,
    ): Call<StoriesResponse>

    @GET("stories")
    fun getMapStories(
        @Header("Authorization") token: String?,
        @Query("location") page: Int = 1
    ) : Call<MapResponse>

    @GET("stories")
    suspend fun getPageStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") token: String?
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): Call<FileUploadResponse>



}