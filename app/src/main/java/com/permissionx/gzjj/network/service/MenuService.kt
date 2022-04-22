package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.MenuData
import retrofit2.Call
import retrofit2.http.GET

interface MenuService {
    @GET("portal/menu/list_type/")
    fun requestMenu():Call<MenuData>
}