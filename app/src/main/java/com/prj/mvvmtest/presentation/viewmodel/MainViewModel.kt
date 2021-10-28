package com.prj.mvvmtest.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.prj.mvvmtest.data.model.Entity
import com.prj.mvvmtest.data.repository.room.AppDatabase
import com.prj.mvvmtest.data.repository.room.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val Repository : Repository =
        Repository(AppDatabase.getDatabase(application, viewModelScope))

    var allUsers : LiveData<List<Entity>> = Repository.allUser

    fun insert(entity: Entity) = viewModelScope.launch(Dispatchers.IO){
        Repository.insert(entity)
    }

    fun deleteAll(entity: Entity) = viewModelScope.launch(Dispatchers.IO){
        Repository.delete(entity)
    }

    fun getAll() : LiveData<List<Entity>>{
        return allUsers
    }

}