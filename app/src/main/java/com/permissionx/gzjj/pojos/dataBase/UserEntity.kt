package com.permissionx.gzjj.pojos.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @ColumnInfo(name = "userId")
    var userId :String = "",
    var cookie:String = "",
    var userName: String = "",
    var userPhone:String = "",
    var level: String = "",
    var userEmail: String = ""
)
