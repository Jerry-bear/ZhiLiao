package com.permissionx.gzjj.utils

object UserData {
    var userName = ""
    var id = ""
    var email = ""
    var level = ""
    var telPhone = ""
    lateinit var cookie :String

    fun refresh(){
        userName = ""
        id = ""
        email = ""
        level = ""
        telPhone = ""
        cookie = ""
    }
}