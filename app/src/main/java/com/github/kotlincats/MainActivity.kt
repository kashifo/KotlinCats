package com.github.kotlincats

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.kotlincats.db.ImagePojo
import com.github.kotlincats.db.RoomDB
import com.github.kotlincats.favorite.FavoritesActivity
import com.github.kotlincats.utils.ApiInterface
import com.github.kotlincats.utils.MyAppClass
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = javaClass.simpleName
    val catUrl = "https://aws.random.cat/meow"
    var imageList: ArrayList<ImagePojo> = ArrayList()
    var curPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "onCreate")
        initViews()

        showRandomCat()
    }

    fun initViews() {

        tvShowFavorites.setOnClickListener(this)
        btnFavorite.setOnClickListener(this)
        btnPrevious.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnShare.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        Log.e(TAG, "onClick() v=${v?.id}")

        when (v?.id) {
            R.id.btnFavorite -> addFavorite()

            R.id.btnPrevious -> btnPrev()

            R.id.btnNext -> btnNext()

            R.id.btnShare -> btnShare()

            R.id.tvShowFavorites -> gotoFavs()
        }

    }

    fun gotoFavs() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }

    fun addFavorite() {
        //todo: makesure roomdb init'd. then write code to insert record as per Kotlin.
        InsertTask(this, imageList.get(curPosition)).execute()
    }

    private class InsertTask(var context: MainActivity, var img: ImagePojo) :
        AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = RoomDB.getInstance(context)
            db.imageDao().addFav(img)
            return true
        }

        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun btnPrev() {
        Log.e(TAG, "btnPrevious() curPosition=$curPosition, imageList.size=${imageList.size}")

        if (curPosition > 0) {

            curPosition -= 1
            Glide.with(this).load(imageList.get(curPosition).imageUrl)
                .placeholder(R.drawable.loading).dontAnimate()
                .error(R.drawable.ic_warning).into(imgCat)
            tvUrl.text = imageList.get(curPosition).imageUrl

        }
    }

    fun btnNext() {
        Log.e(TAG, "btnNext() curPosition=$curPosition, imageList.size=${imageList.size}")

        if (curPosition < (imageList.size - 1)) {

            curPosition += 1
            Glide.with(this).load(imageList.get(curPosition).imageUrl)
                .placeholder(R.drawable.loading).dontAnimate()
                .error(R.drawable.ic_warning).into(imgCat)
            tvUrl.text = imageList.get(curPosition).imageUrl

        } else {
            showRandomCat()
        }
    }

    fun btnShare() {
        Log.e(TAG, "btnShare() curPosition=$curPosition, imageList.size=${imageList.size}")

        if (curPosition < (imageList.size - 1)) {
            Log.e(TAG, "btnShare() image=${imageList.get(curPosition).imageUrl}")

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, imageList.get(curPosition).imageUrl)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    fun showRandomCat() {

        val apiInterface =
            MyAppClass.instance?.getRetrofitClient()?.create(ApiInterface::class.java)

        apiInterface?.getRandomCat()?.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(
                call: retrofit2.Call<List<ImagePojo>>,
                response: retrofit2.Response<List<ImagePojo>>
            ) {
                Log.e(TAG, "onResponse=${response.body()?.toString()}")

                if (response.body() == null || response.body()!!.isEmpty()) {
                    showEmpty()
                    return
                }

                val imagePojo: ImagePojo = response.body()!!.get(0)

                if (imagePojo == null && imagePojo.imageUrl.isNullOrEmpty()) {
                    showEmpty()
                    return
                }

                tvUrl.text = imagePojo.imageUrl
                imageList.add(imagePojo)
                curPosition += 1
                Glide.with(this@MainActivity)
                    .load(imagePojo.imageUrl)
                    .placeholder(R.drawable.loading).dontAnimate().error(R.drawable.ic_warning)
                    .into(imgCat)
                Log.e(TAG, "showRandomCat() curPosition=$curPosition, imageList.size=${imageList.size}")
            }

            override fun onFailure(call: retrofit2.Call<List<ImagePojo>>, t: Throwable) {
                Log.e(TAG, "onErrorResponse=${t.message}")
                showEmpty()
            }

        })

    }

    fun showEmpty() {
        tvUrl.text = getString(R.string.some_error)
        Glide.with(this).load(R.drawable.ic_warning).into(imgCat)
    }

}
