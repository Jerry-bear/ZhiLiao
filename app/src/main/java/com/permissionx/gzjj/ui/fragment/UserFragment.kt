package com.permissionx.gzjj.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.permissionx.gzjj.R
import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.permissionx.gzjj.utils.UserData
import com.permissionx.gzjj.pojos.*
import com.permissionx.gzjj.pojos.dataBase.DatabaseManager
import com.permissionx.gzjj.pojos.dataBase.HistoryOrderEntity
import com.permissionx.gzjj.pojos.viewModel.HistoryOrderViewModel
import com.permissionx.gzjj.pojos.viewModel.ResponseViewModel
import com.permissionx.gzjj.ui.activity.LoginActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.coroutines.runBlocking


class UserFragment:Fragment() {

    private lateinit var imageBack:ImageView
    private lateinit var imageHead:ImageView
    private lateinit var userId:TextView
    private lateinit var userLevel:TextView
    private lateinit var userEMail:TextView
    private lateinit var userName:TextView
    private lateinit var userTelPhone:TextView
    private lateinit var logOut:LinearLayout
    private lateinit var deleteCache:LinearLayout
    private lateinit var historyList:List<HistoryOrderEntity>
    private lateinit var historyOrderViewModel: HistoryOrderViewModel
    private lateinit var responseViewModel: ResponseViewModel

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user,container,false)
        imageBack = view.findViewById(R.id.h_back)
        imageHead = view.findViewById(R.id.h_head)
        userId = view.findViewById(R.id.user_id)
        userLevel = view.findViewById(R.id.user_level)
        userEMail = view.findViewById(R.id.user_email)
        userName = view.findViewById(R.id.user_name)
        userTelPhone = view.findViewById(R.id.user_val)
        logOut = view.findViewById(R.id.log_out)
        deleteCache = view.findViewById(R.id.delete_cache)
        historyOrderViewModel = ViewModelProvider(this).get(HistoryOrderViewModel::class.java)
        responseViewModel = ViewModelProvider(this).get(ResponseViewModel::class.java)

        initView()
        initListener()

        return view
    }

    private fun initListener(){

        logOut.setOnClickListener {
            myDialog("确定退出登录?",{
                runBlocking {
                    try {
                        context?.let { it1 -> DatabaseManager.saveApplication(it1) }
                        Log.d("!!!!","1")
                        if (DatabaseManager.db.loginDao.getSize() != 0){
                            Log.d("!!!!", DatabaseManager.db.loginDao.getSize().toString() + "ls")
                            DatabaseManager.db.loginDao.deleteAll()
                            Log.d("!!!!", DatabaseManager.db.loginDao.getSize().toString() + "ls")
                        }
                        if (DatabaseManager.db.userDao.getSize() != 0){
                            Log.d("!!!!", DatabaseManager.db.userDao.getSize().toString() + "us")
                            DatabaseManager.db.userDao.deleteAll()
                            Log.d("!!!!", DatabaseManager.db.loginDao.getSize().toString() + "ls")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                UserData.refresh()
                refreshView()
                startActivity(Intent(context,LoginActivity::class.java))
                requireActivity().finish()
            })
        }
        deleteCache.setOnClickListener {
            myDialog("确定清除缓存?",{
                runBlocking {
                    try {
                        context?.let { it1 -> DatabaseManager.saveApplication(it1) }
                        historyList = DatabaseManager.db.historyOrderDao.getAllHistoryOrder()
                        DatabaseManager.db.historyOrderDao.deleteAll(historyList)
                        historyOrderViewModel.shouldRefresh.value = System.currentTimeMillis() + 1
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                activity?.recreate()
            })
        }
    }

    private fun initObverse(){

        responseViewModel.refreshCustomerInfo.observe(viewLifecycleOwner, Observer {
            refreshView()
        })

    }

    private fun initView(){

        userId.text = UserData.id
        userLevel.text = UserData.level
        userEMail.text = UserData.email
        userName.text = UserData.userName
        userTelPhone.text = formatTelPhone(UserData.telPhone)
        //设置背景磨砂效果
        Glide.with(this).load(R.drawable.curry)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
            .apply(RequestOptions.bitmapTransform(BlurTransformation(14,3)))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageBack)
        //设置圆形图像
        Glide.with(this).load(R.drawable.curry)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageHead)
    }

    private fun formatTelPhone(num:String) : String{
        val sb :StringBuilder = StringBuilder(num)
        if (sb.length >= 8) return sb.replace(3, 7, "****").toString()
        return num
    }

    private fun refreshView(){

        userId.text = UserData.id
        userLevel.text = UserData.level
        userEMail.text = UserData.email
        userName.text = UserData.userName
        userTelPhone.text = formatTelPhone(UserData.telPhone)

    }

    private fun myDialog(content: String, positiveButtonClicked:() -> Unit, negativeButtonClicked: (() -> Unit)? = null) {
//     实例化对象
        val dialog = MyDialog(context)
        //      显示布局
        val view = dialog.createView(context, com.permissionx.gzjj.R.layout.dialog_layout)
        //      显示
        dialog.show()

//      拿到子控件
        val tvContent = view.findViewById<TextView>(R.id.dialog_content)
        val tvQuxiao = view.findViewById<View>(com.permissionx.gzjj.R.id.tvQuxiao) as TextView
        val tvXianshi = view.findViewById<View>(com.permissionx.gzjj.R.id.tvXianshi) as TextView

        tvContent.text = content
        //      监听
        tvQuxiao.setOnClickListener {
            positiveButtonClicked()
            dialog.dismiss()
        }
        tvXianshi.setOnClickListener {
            negativeButtonClicked?.invoke()
            dialog.dismiss()
        }
    } //myDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}