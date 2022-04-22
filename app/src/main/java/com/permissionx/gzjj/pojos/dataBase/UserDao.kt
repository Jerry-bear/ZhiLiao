package com.permissionx.gzjj.pojos.dataBase

import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface UserDao {
    @Insert
    suspend fun save(vararg userEntity: UserEntity) : List<Long>

    @Query("select * from user")
    suspend fun getAllUserInfo():List<UserEntity>

    @Delete
    suspend fun delete(vararg userEntity: UserEntity)

    @Query("select count(*) from user")
    suspend fun getSize():Int

    @Update
    suspend fun update(vararg userEntity: UserEntity)

    @Query("select * from user where cookie = :cookie")
    suspend fun selectByCookie(cookie:String):UserEntity

    suspend fun deleteByCookie(cookie:String){
        delete(selectByCookie(cookie))
    }

    suspend fun deleteAll(){
        getAllUserInfo().forEach {
            delete(it)
        }
    }
}