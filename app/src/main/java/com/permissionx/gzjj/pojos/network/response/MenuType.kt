package com.permissionx.gzjj.pojos.network.response

data class MenuType(
    val code: Int,
    val `data`: List<String>,
    val message: String,
    val success: Boolean
)