package com.prj.mvvmtest.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prj.mvvmtest.data.model.Contact
import com.prj.mvvmtest.data.repository.room.ContactRepository

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application)
    private val contacts = repository.getAll()

    fun getAll() : LiveData<List<Contact>>{
        return this.contacts
    }

    fun insert(contact : Contact){
        repository.insert(contact)
    }

    fun delete(contact: Contact){
        repository.delete(contact)
    }

}