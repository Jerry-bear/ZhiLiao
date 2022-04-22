package com.permissionx.gzjj.pojos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.permissionx.gzjj.network.Repository
import com.permissionx.gzjj.pojos.network.request.RequestLogin
import com.permissionx.gzjj.pojos.network.request.RequestOrder
import kotlin.random.Random

class ResponseViewModel:ViewModel() {

    private val request = MutableLiveData(1)
    private val tableId = MutableLiveData<String>()
    private val requestOrderLiveData = MutableLiveData<RequestOrder>()
    private val requestLogin = MutableLiveData<RequestLogin>()
    private val requestCustomer = MutableLiveData<Int>()
    val refreshCustomerInfo = MutableLiveData<Long>()
    val requestDiscount = MutableLiveData<Int>()

    val responseMenu = Transformations.switchMap(request){ request
        Repository.requestMenu()
    }

    val responseMenuType = Transformations.switchMap(request){ request
        Repository.requestMenuType()
    }

    val responseFullTable = Transformations.switchMap(tableId){ tableId ->
        Repository.requestTableFull(tableId)
    }

    val responseCommitOrder = Transformations.switchMap(requestOrderLiveData){ requestOrderLiveData ->
        Repository.commitOrder(requestOrderLiveData)
    }

    val responseLogin = Transformations.switchMap(requestLogin){ requestLogin ->
        Repository.login(requestLogin.password, requestLogin.phone)
    }

    val responseCustomer = Transformations.switchMap(requestCustomer){ requestCustomer
        Repository.requestCustomer()
    }

    val responseDiscount = Transformations.switchMap(requestDiscount){requestDiscount
        Repository.requestDiscount()
    }

    fun requestMenu(){
        request.value = request.value?.plus(Random(666).nextInt())
    }

    fun requestFullTable(tableId:String){
        this.tableId.value = tableId
    }

    fun commitOrder(requestOrder: RequestOrder){
        requestOrderLiveData.value = requestOrder
    }

    fun login(requestLogin: RequestLogin){
        this.requestLogin.value = requestLogin
    }

    fun requestCustomer(request:Int){
        this.requestCustomer.value = request
    }

    fun requestDiscount(request: Int){
        this.requestDiscount.value = request
    }

}