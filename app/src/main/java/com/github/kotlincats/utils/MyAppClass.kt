package com.github.kotlincats.utils

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

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

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

}
