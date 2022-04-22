package com.permissionx.gzjj.pojos.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface LoginDao {

    @Insert
    suspend fun save(vararg loginEntity: LoginEntity):List<Long>

    @Query("select * from login")
    suspend fun getAllLogin():List<LoginEntity>

    @Query("select count(*) from login")
    suspend fun getSize():Int

    @Delete
    suspend fun delete(vararg loginEntity: LoginEntity)

    suspend fun deleteAll(){
        getAllLogin().forEach {
            delete(it)
        }
    }
}