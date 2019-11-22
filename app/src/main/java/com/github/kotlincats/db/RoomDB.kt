package com.github.kotlincats.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(ImagePojo::class), version = 1)
abstract class RoomDB : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {

        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomDB::class.java,
                    "roomdb")
                    .build()
            }

            return INSTANCE as RoomDB
        }

    }
}