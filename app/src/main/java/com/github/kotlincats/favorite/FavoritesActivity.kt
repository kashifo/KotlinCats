package com.github.kotlincats.favorite

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.github.kotlincats.db.ImagePojo
import com.github.kotlincats.R
import com.github.kotlincats.db.RoomDB
import com.github.kotlincats.favorite.FavListAdapter.FavClickListener
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName
    lateinit var adapter: FavListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        Log.e(TAG, "onCreate")

        rvFavList.setHasFixedSize(true);
        val layoutManager = GridLayoutManager(this, 2)
        rvFavList.setLayoutManager(layoutManager)
        rvFavList.setItemAnimator(DefaultItemAnimator())

        adapter = FavListAdapter(this, favClick = object : FavClickListener {
            override fun onFavClick(url: String) {
                Log.e(TAG, "onFavClick url="+url)
                val frag = ImageDialogFragment.newInstance( url )
                frag.show( supportFragmentManager, "" )
            }
        })
        rvFavList.adapter = adapter

        GetDataFromDb(this).execute()
    }

    fun setData(imgList: List<ImagePojo>){

        adapter.addData(imgList)
        adapter.notifyDataSetChanged()

    }

    private class GetDataFromDb(var context: FavoritesActivity) : AsyncTask<Void, Void, List<ImagePojo>>() {

        override fun doInBackground(vararg params: Void?): List<ImagePojo> {
            val db = RoomDB.getInstance(context)
            return db.imageDao().getAll()
        }

        override fun onPostExecute(imgList: List<ImagePojo>?) {

            if ( !imgList.isNullOrEmpty() ) {
                context.setData(imgList)
            }
        }
    }


}
