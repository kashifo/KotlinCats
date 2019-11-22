package com.github.kotlincats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_images")
data class ImagePojo(@ColumnInfo(name = "image") var imageUrl: String){

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "fav")
    var isFavorite = false

}