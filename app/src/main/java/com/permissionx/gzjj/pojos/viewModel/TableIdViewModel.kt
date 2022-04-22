package com.permissionx.gzjj.pojos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TableIdViewModel :ViewModel(){
    val tableName = MutableLiveData<String>("")
    val tableId = MutableLiveData<String>("")
}