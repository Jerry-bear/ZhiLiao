package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.BaseResponse
import com.permissionx.gzjj.pojos.network.response.DataBaseResponse
import retrofit2.Call
import retrofit2.http.GET

interface RequestDiscount {

    @GET("/portal/customer/discount")
    fun requestDiscount(): Call<DataBaseResponse>
}