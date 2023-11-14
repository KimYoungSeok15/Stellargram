package com.ssafy.stellargram.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.stellargram.data.db.entity.Constellation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstellationDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addConstellation(item: Constellation)
    @Query(value =  "SELECT * FROM constellations")
    fun findAll() : List<Constellation>

    @Query(value =  "SELECT * FROM constellations")
    fun readAll() : Flow<List<Constellation>>
}