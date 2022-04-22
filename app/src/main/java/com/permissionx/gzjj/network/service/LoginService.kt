package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.BaseResponse
import com.permissionx.gzjj.pojos.network.response.LoginResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    @POST("/portal/customer/login")
    fun login(@Query("password") password:String?, @Query("phone") phone:String?) : Call<LoginResponse>
}