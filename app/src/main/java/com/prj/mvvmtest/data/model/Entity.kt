package com.prj.mvvmtest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class Entity(
    @PrimaryKey(autoGenerate = true)// pk 자동 생성
    val id : Int,
    var number1 : String
)
