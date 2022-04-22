package com.permissionx.gzjj.pojos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.permissionx.gzjj.pojos.HistoryOrder

class HistoryOrderViewModel:ViewModel() {

    val historyOrderList = MutableLiveData<List<HistoryOrder>>()
    val shouldRefresh = MutableLiveData<Long>()
}