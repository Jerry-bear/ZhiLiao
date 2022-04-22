package com.permissionx.gzjj.network

import com.permissionx.gzjj.network.service.*
import com.permissionx.gzjj.pojos.network.request.RequestOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetWork {
    // 获取所有菜单
    private val menuService = ServiceCreator.create(MenuService::class.java)
    suspend fun requestMenu() = menuService.requestMenu().await()

    // 获取所有菜品种类
    private val menuTypeService = ServiceCreator.create(MenuTypeService::class.java)
    suspend fun requestMenuType() = menuTypeService.requestMenuType().await()

    //占用桌子
    private val tableFullService = ServiceCreator.create(TableFullService::class.java)
    suspend fun requestTableFull(table_id:String) = tableFullService.requestTableFull(table_id).await()

    // 提交菜单
    private val commitOrderService = ServiceCreator.create(CommitOrderService::class.java)
    suspend fun commitOrder(requestOrder: RequestOrder) = commitOrderService.commitOrder(requestOrder).await()

    // 请求Cookie
    private val loginService = ServiceCreator.create(LoginService::class.java)
    suspend fun login(pwd:String?, phone:String?) = loginService.login(pwd, phone).await()

    // 用cookie登录
    private val customerInfoService = ServiceCreator.create(CustomerInfoService::class.java)
    suspend fun requestCustomerInfo() = customerInfoService.requestCustomer().await()

    //获取折扣
    private val requestDiscount = ServiceCreator.create(RequestDiscount::class.java)
    suspend fun requestDiscount() = requestDiscount.requestDiscount().await()

    private suspend fun <T> Call<T>.await():T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}