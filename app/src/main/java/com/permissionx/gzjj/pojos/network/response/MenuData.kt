package com.permissionx.gzjj.pojos.network.response

data class MenuData(
    val code: Int,
    val `data`: List<MenuDataItem>,
    val message: String,
    val success: Boolean
)