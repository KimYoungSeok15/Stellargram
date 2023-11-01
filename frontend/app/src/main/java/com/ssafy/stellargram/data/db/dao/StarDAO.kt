package com.ssafy.stellargram.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.stellargram.data.db.entity.Star

@Dao
interface StarDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addStar(item: Star)
    @Query("SELECT * FROM stars")
    fun findAll() : List<Star>

}
