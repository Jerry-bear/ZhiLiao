package com.permissionx.gzjj.pojos.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryOrderDao {
    @Insert
    suspend fun save(vararg historyOrders: HistoryOrderEntity) : List<Long>

    @Query("select * from historyOrder")
    suspend fun getAllHistoryOrder() : List<HistoryOrderEntity>

    @Delete
    suspend fun delete(vararg historyOrders: HistoryOrderEntity)

    @Query("select count(*) from historyOrder")
    suspend fun size() : Int

    @Query("select * from historyOrder where orderId = :orderId order by orderId asc")
    suspend fun getHistoryOrderByOrderId(orderId : Long) : List<HistoryOrderEntity>

    @Query("select * from historyOrder where userId = :userId order by orderId asc")
    suspend fun getHistoryOrderByUserId(userId : Long) : List<HistoryOrderEntity>

    suspend fun saveAll(list:List<HistoryOrderEntity>){
        list.forEach {
            save(it)
        }
    }

    suspend fun deleteAll(list:List<HistoryOrderEntity>){
        list.forEach {
            delete(it)
        }
    }
}