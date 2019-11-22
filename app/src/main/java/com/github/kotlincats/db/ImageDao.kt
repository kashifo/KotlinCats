package com.github.kotlincats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFav(img: ImagePojo)

    @Query("SELECT * from fav_images")
    fun getAll():List<ImagePojo>

    @Query("DELETE from fav_images WHERE id=:id")
    fun removeFav(id: Int)

    @Query("DELETE from fav_images")
    fun clearAll()

}