package com.ssafy.stellargram.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stars")
data class Star(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo
    val hip : Int?,
    @ColumnInfo
    val hd : Int?,
    @ColumnInfo
    val hr : Int?,
    @ColumnInfo
    val gl : String?,
    @ColumnInfo
    val bf : String?,
    @ColumnInfo
    val proper : String?,
    @ColumnInfo
    val ra : Double?,
    @ColumnInfo
    val dec : Double?,
    @ColumnInfo
    val dist : Double?,
    @ColumnInfo
    val pmra : Double?,
    @ColumnInfo
    val pmdec : Double?,
    @ColumnInfo
    val rv : Double?,
    @ColumnInfo
    val mag : Double?,
    @ColumnInfo
    val absmag : Double?,
    @ColumnInfo
    val spect : String?,
    @ColumnInfo
    val ci : Double?,
    @ColumnInfo
    val x : Double?,
    @ColumnInfo
    val y : Double?,
    @ColumnInfo
    val z : Double?,
    @ColumnInfo
    val vx : Double?,
    @ColumnInfo
    val vy : Double?,
    @ColumnInfo
    val vz : Double?,
    @ColumnInfo
    val rarad : Double?,
    @ColumnInfo
    val decrad : Double?,
    @ColumnInfo
    val pmrarad : Double?,
    @ColumnInfo
    val pmdecrad : Double?,
    @ColumnInfo
    val bayer : String?,
    @ColumnInfo
    val flam : Int?,
    @ColumnInfo
    val con : String?,
    @ColumnInfo
    val comp : Int?,
    @ColumnInfo
    val comp_primary : Int?,
    @ColumnInfo
    val base : String?,
    @ColumnInfo
    val lum : Double?,
    @ColumnInfo
    val variable : String?,
    @ColumnInfo
    val var_min : Double?,
    @ColumnInfo
    val var_max : Double?
)