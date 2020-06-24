package com.github.kotlincats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_images")
class ImagePojo(){

    @Expose()
    @SerializedName("url")
    @ColumnInfo(name = "image")
    var imageUrl: String = ""

    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "fav")
    var isFavorite = false

    override fun toString(): String {
        return "ImagePojo(imageUrl=$imageUrl, id=$id, isFavorite=$isFavorite)"
    }

}