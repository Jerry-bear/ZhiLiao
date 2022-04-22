package com.permissionx.gzjj.pojos.dataBase

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "historyOrder")
data class HistoryOrderEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @ColumnInfo(name = "userId")
    var userId :String = "",
    var name: String = "",
    var num:String = "",
    var date:String = "",
    var price:String = "",
    var content:String = "",
    var imageUrl:String = "",
    var orderId:Long = 0
){
    constructor(
        name: String,
        num:String,
        date:String,
        price:String,
        content:String,
        imageUrl:String,
        orderId: Long,
        userId: String
    ):this(){
        this.name = name
        this.num = num
        this.content = content
        this.price = price
        this.imageUrl = imageUrl
        this.date = date
        this.orderId = orderId
        this.userId = userId
    }
}
