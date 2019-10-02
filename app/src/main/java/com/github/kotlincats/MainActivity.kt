package com.github.kotlincats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = javaClass.simpleName
    val catUrl = "https://aws.random.cat/meow"
    var imageList: ArrayList<String> = ArrayList()
    var curPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "onCreate")
        initViews()

        showRandomCat()
    }

    fun initViews(){

        btnPrevious.setOnClickListener(this)
        btnNext.setOnClickListener (this)
        btnShare.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        Log.e(TAG, "onClick() v=${v?.id}")

        when(v?.id){

            R.id.btnPrevious -> {
                Log.e(TAG, "btnPrevious() curPosition=$curPosition, imageList.size=${imageList.size}")

                if(curPosition>0){

                    curPosition-=1
                    Glide.with(this).load( imageList.get(curPosition) ).placeholder(R.drawable.loading).dontAnimate()
                        .error(R.drawable.ic_warning).into(imgCat)
                    tvUrl.text = imageList.get(curPosition)

                }
            }

            R.id.btnNext -> {
                Log.e(TAG, "btnNext() curPosition=$curPosition, imageList.size=${imageList.size}")

                if( curPosition < (imageList.size-1) ){

                    curPosition+=1
                    Glide.with(this).load( imageList.get(curPosition) ).placeholder(R.drawable.loading).dontAnimate()
                        .error(R.drawable.ic_warning).into(imgCat)
                    tvUrl.text = imageList.get(curPosition)

                }else {
                    showRandomCat()
                }
            }

            R.id.btnShare -> {
                Log.e(TAG, "btnShare() image=${imageList.get(curPosition)}")

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, imageList.get(curPosition))
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

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
                    imageList.add( file )
                    curPosition+=1
                    Log.e(TAG, "showRandomCat() curPosition=$curPosition, imageList.size=${imageList.size}")
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
        tvUrl.text = getString(R.string.some_error)
        Glide.with(this).load(R.drawable.ic_warning).into(imgCat)
    }

}
