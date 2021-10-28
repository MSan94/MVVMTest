package com.prj.mvvmtest.data.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prj.mvvmtest.data.model.Entity

@Dao
interface DAO {

    // DB 불러오기
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAll() : LiveData<List<Entity>>

    // 데이터 추가
    // REPLACE : OnConflictStrategy.IGNORE 을 통해 같은 값이 들어왔을 때 무시한다.
    // ABORT : 트랜잭션을 중단하기 위한 OnConflict 전략 상수입니다.
    // FAIL : 트랜잭션을 실패하는 OnConflict 전략 상수입니다.
    // IGNORE : 충돌을 무시하는 OnConflict 전략 상수입니다.
    // ROLLBACK : 트랜잭션을 롤백하기 위한 OnConflict 전략 상수
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity : Entity)

    // 데이터 전체 삭제
    @Query("DELETE FROM user_table")
    fun deleteAll()

    // 데이터 업데이트
    @Update
    fun update(entity: Entity)

    //데이터 삭제
    @Delete
    fun delete(entity: Entity)
}