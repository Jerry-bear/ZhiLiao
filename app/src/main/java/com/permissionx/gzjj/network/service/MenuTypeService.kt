package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.MenuType
import retrofit2.Call
import retrofit2.http.GET

interface MenuTypeService {
    @GET("portal/menu/type/")
    fun requestMenuType():Call<MenuType>
}