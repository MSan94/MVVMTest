package com.prj.mvvmtest.data.repository.room

import androidx.lifecycle.LiveData
import com.prj.mvvmtest.data.model.Entity

class Repository(mDatabase : AppDatabase) {

    private val dao = mDatabase.dao()
    val allUser : LiveData<List<Entity>> = dao.getAll()
    companion object{
        private var sInstance : Repository? = null
        fun getInstance(database: AppDatabase) : Repository{
            return sInstance
                ?: synchronized(this){
                    val instance = Repository(database)
                    sInstance = instance
                    instance //return
                }
        }
    }

    suspend fun insert(entity: Entity){
        dao.insert(entity)
    }

    suspend fun delete(entity : Entity){
        dao.delete(entity)
    }
}