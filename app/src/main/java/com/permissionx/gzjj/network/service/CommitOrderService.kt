package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.BaseResponse
import com.permissionx.gzjj.pojos.network.request.RequestOrder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommitOrderService {
    @POST("/portal/order/commit")
    fun commitOrder(@Body requestOrder: RequestOrder):Call<BaseResponse>
}