package com.permissionx.gzjj.network.service

import com.permissionx.gzjj.pojos.network.response.BaseResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface TableFullService {
    @POST("/portal/table/full/{table_id}")
    fun requestTableFull(@Path("table_id") table_id:String):Call<BaseResponse>

}