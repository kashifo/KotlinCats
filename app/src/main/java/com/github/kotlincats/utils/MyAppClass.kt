package com.github.kotlincats.utils

import android.app.Application
import com.github.kotlincats.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyAppClass : Application() {

    companion object {
        private val TAG = MyAppClass::class.java.simpleName
        @get:Synchronized
        var instance: MyAppClass? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private var retrofit: Retrofit? = null

    fun getRetrofitClient(): Retrofit {

        if (retrofit == null) {
            var client = OkHttpClient.Builder().build()

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                client = OkHttpClient.Builder() //.addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(interceptor).build()
            }

            retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(Constants.BASE_URL)
                .build()
        }

        return retrofit!!
    }

    private fun getGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

}
