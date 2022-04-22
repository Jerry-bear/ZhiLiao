package com.permissionx.gzjj.pojos.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "login")
data class LoginEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var cookie: String
)
