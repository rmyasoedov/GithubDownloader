package com.instrument.githubdownloader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "load")
class Load (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "fileName")
    var fileName: String = "",
    @ColumnInfo(name = "userName")
    var userName: String = "",
    @ColumnInfo(name = "repository")
    var repository: String = "",
    @ColumnInfo(name = "size")
    var size: Long = 0,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis(),
)