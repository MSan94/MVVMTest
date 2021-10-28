package com.prj.mvvmtest.data.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prj.mvvmtest.data.model.Entity
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Entity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun dao() : DAO

    companion object{
        // 동기화 방법중의 하나
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getDatabase(
            context : Context,
            scope : CoroutineScope
        ): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance //return
            }
        }
    }

}