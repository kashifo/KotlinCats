package com.github.kotlincats

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName
    val catUrl = "https://aws.random.cat/meow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "onCreate")

        showRandomCat()

        btnNextCat.setOnClickListener {
            showRandomCat()
        }
    }

    fun showRandomCat() {

        val requestCat = StringRequest(Request.Method.GET, catUrl, Response.Listener { response ->

            try {
                val jsonObject = JSONObject(response)
                val file = jsonObject.getString("file")

                if (!file.isNullOrEmpty()) {
                    Glide.with(this).load(file).placeholder(R.drawable.loading).dontAnimate()
                        .error(R.drawable.ic_warning).into(imgCat)
                    tvUrl.text = file
                } else {
                    showEmpty()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showEmpty()
            }

        }, Response.ErrorListener { error ->
            Log.e(TAG, "onErrorResponse=${error?.message}")
            showEmpty()
        })

        imgCat.setImageResource(R.drawable.loading) //show loading
        MyAppClass.instance?.addToRequestQueue(requestCat)

    }

    fun showEmpty() {
        tvUrl.text = "Something Error"
        Glide.with(this).load(R.drawable.ic_warning).into(imgCat)
    }

}
