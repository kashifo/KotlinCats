package com.github.kotlincats.utils

import com.github.kotlincats.db.ImagePojo
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("v1/images/search")
    fun getRandomCat(): Call<List<ImagePojo>>
}