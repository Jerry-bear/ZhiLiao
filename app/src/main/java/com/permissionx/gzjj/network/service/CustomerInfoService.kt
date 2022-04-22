package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.LoginResponse
import retrofit2.Call
import retrofit2.http.GET

interface CustomerInfoService {

    @GET("/portal/customer/login_customer")
    fun requestCustomer():Call<LoginResponse>
}