package com.permissionx.gzjj.pojos.network.response

data class LoginResponse(
    val code: Int,
    val `data`: LoginDataItem,
    val message: String,
    val success: Boolean
)