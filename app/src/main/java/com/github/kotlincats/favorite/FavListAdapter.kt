package com.github.kotlincats.favorite

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kotlincats.db.ImagePojo
import com.github.kotlincats.R
import kotlinx.android.synthetic.main.item_fav_list.view.*

/**
 * Created by Kashif on 10/9/2019.
 */
class FavListAdapter(val context: Context? = null) : RecyclerView.Adapter<FavListAdapter.ViewHolder>() {

    val TAG = javaClass.simpleName
    var imgList: List<ImagePojo>

    init {
        imgList = ArrayList()
    }

    fun addData(arrList: List<ImagePojo>){
        this.imgList = arrList
        Log.e(TAG, "addData-"+ imgList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_fav_list,
                parent,
                false
            )
        )
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var imageView = view.imageView
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        Log.e(TAG, "onBindViewHolder-"+imgList.get(pos).imageUrl )
        Glide.with(context!!).load( imgList.get(pos).imageUrl ).override(256).centerCrop().into( holder.imageView )

    }//onBind


}