package com.permissionx.gzjj.network

import androidx.lifecycle.liveData
import com.permissionx.gzjj.pojos.network.request.RequestOrder
import kotlinx.coroutines.Dispatchers

object Repository {

    //请求菜单
    fun requestMenu() = liveData(Dispatchers.IO){
        val result = try {
            val menuResponse = NetWork.requestMenu()
            Result.success(menuResponse)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //请求种类
    fun requestMenuType() = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.requestMenuType()
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //占用桌子
    fun requestTableFull(table_id:String) = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.requestTableFull(table_id)
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //提交订单
    fun commitOrder(requestOrder: RequestOrder) = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.commitOrder(requestOrder)
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //获得cookie
    fun login(pwd:String?, phone:String?) = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.login(pwd,phone)
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //cookie 获取用户信息
    fun requestCustomer() = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.requestCustomerInfo()
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

    //获取折扣信息
    fun requestDiscount() = liveData(Dispatchers.IO){
        val result = try {
            val response = NetWork.requestDiscount()
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }

}