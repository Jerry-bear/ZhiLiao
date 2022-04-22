package com.permissionx.gzjj.pojos.dataBase

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@SuppressLint("StaticFieldLeak")
object DatabaseManager {
    private const val DB_NAME = "database.db"
    private val MIGRATIONS = arrayOf(Migration1)
    private lateinit var context: Context
    val db: DataBase by lazy {
        Room.databaseBuilder(context.applicationContext, DataBase::class.java, DB_NAME)
            .addCallback(CreatedCallBack)
            .addMigrations(*MIGRATIONS)
            .build()
    }

    fun saveApplication(context: Context) {
        DatabaseManager.context = context
    }

    private object CreatedCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
            Log.d("!!!","create")
            MIGRATIONS.map {
                it.migrate(db)
            }
        }
    }

    private object Migration1 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 数据库的升级语句
            // database.execSQL("")
        }
    }
}