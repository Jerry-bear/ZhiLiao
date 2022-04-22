package com.permissionx.gzjj.pojos.network.response

data class BaseResponse(
    val code: Int,
    val message: String,
    val success: Boolean
)