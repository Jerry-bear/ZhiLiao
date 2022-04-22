package com.permissionx.gzjj.pojos.network.request

data class RequestOrder(
    val customerId: String? = "",
    val seatId:String?,
    val type:String,
    val price:String,
    val menus:List<CommitOrderItem>
)
