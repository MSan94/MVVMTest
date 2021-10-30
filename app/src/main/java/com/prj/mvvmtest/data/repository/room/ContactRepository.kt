package com.prj.mvvmtest.data.repository.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.prj.mvvmtest.data.model.Contact
import java.lang.Exception
// Database, Dao, contacts를 각각 초기화
// Room DB를 메인 스레드에서 접근하려 하면 크래쉬가 발생한다
class ContactRepository(application: Application) {

    private val contactDatabase = ContactDatabase.getInstance(application)!!
    private val contactDao : ContactDao = contactDatabase.contactDao()
    private val contacts : LiveData<List<Contact>> = contactDao.getAll()

    fun getAll() : LiveData<List<Contact>>{
        return contacts
    }

    fun insert(contact : Contact){
        try{
            val thread = Thread(Runnable {
                contactDao.insert(contact)
            })
            thread.start()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun delete(contact: Contact){
        try{
            val thread = Thread(Runnable {
                contactDao.delete(contact)
            })
            thread.start()
        }catch(e : Exception){
            e.printStackTrace()
        }
    }

}