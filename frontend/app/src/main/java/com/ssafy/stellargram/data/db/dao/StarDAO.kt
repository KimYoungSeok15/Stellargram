package com.ssafy.stellargram.data.db.dao

import androidx.compose.ui.input.pointer.PointerId
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.stellargram.data.db.entity.Star
import kotlinx.coroutines.flow.Flow

@Dao
interface StarDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addStar(item: Star)
    @Query("SELECT * FROM stars")
    fun findAll() : List<Star>

    @Query("SELECT * FROM stars WHERE id = :id")
    fun findstar(id: Int): Flow<Star>

    @Query(value =  "SELECT * FROM stars")
    fun readAll() : Flow<List<Star>>
}
