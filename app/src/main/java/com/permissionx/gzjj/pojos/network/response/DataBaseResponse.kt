package com.permissionx.gzjj.pojos.network.response

data class DataBaseResponse(
    val code: Int,
    val message: String,
    val success: Boolean,
    val data: String
)
