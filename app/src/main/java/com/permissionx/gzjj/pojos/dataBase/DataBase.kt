package com.permissionx.gzjj.pojos.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = false, entities = [HistoryOrderEntity::class, LoginEntity::class,UserEntity::class])
abstract class DataBase : RoomDatabase() {

    val historyOrderDao:HistoryOrderDao by lazy { createHistoryOrderDao() }
    val loginDao:LoginDao by lazy { createLoginDao() }
    val userDao:UserDao by lazy { createUserDao() }

    abstract fun createUserDao():UserDao
    abstract fun createHistoryOrderDao() : HistoryOrderDao
    abstract fun createLoginDao() : LoginDao
}