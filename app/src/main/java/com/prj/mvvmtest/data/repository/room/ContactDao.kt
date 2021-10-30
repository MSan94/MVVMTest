package com.prj.mvvmtest.data.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prj.mvvmtest.data.model.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact ORDER BY name ASC")
    fun getAll(): LiveData<List<Contact>>

    //OnConflictStrategy 인터페이스를 호출해 REPLACE, IGNORE, ABORT, FAIL, ROLLBACK 등의 액션이 지정 가능하다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact : Contact)

    @Delete
    fun delete(contact : Contact)

}
