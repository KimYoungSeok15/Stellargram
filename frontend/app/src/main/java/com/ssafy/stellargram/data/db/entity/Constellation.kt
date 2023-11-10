package com.ssafy.stellargram.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constellations")
class Constellation (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo
    val ra : Double,
    @ColumnInfo
    val dec : Double,
    @ColumnInfo
    val con: Int
)