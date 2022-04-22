package com.permissionx.gzjj.pojos.network.request

data class CommitOrderItem(
    val discount:Int,
    val name:String,
    val picture:String,
    val price:String,
    val type:String,
    val sever:String,
    val salesVolume:Int
)
