package com.permissionx.gzjj.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.permissionx.gzjj.R
import com.permissionx.gzjj.utils.UserData
import com.permissionx.gzjj.pojos.dataBase.DatabaseManager
import com.permissionx.gzjj.pojos.dataBase.LoginEntity
import com.permissionx.gzjj.pojos.dataBase.UserEntity
import com.permissionx.gzjj.pojos.network.request.RequestLogin
import com.permissionx.gzjj.pojos.viewModel.ResponseViewModel
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class LoginActivity : AppCompatActivity() {

    private lateinit var signIn:Button
    private lateinit var signUp:Button
    private lateinit var phoneEdt:EditText
    private lateinit var pwdEdt:EditText

    //ViewModel
    private lateinit var viewMode:ResponseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login)
        signIn = findViewById(R.id.sign_in)
        signUp = findViewById(R.id.sign_up)
        phoneEdt = findViewById(R.id.user_phone)
        pwdEdt = findViewById(R.id.user_pwd)
        viewMode = ViewModelProvider(this).get(ResponseViewModel::class.java)
        initListener()
        runBlocking {
            try {
                DatabaseManager.saveApplication(this@LoginActivity)
                if (DatabaseManager.db.loginDao.getSize() > 0){
                    val loginEntity = DatabaseManager.db.loginDao.getAllLogin()
                    UserData.cookie = loginEntity[0].cookie
                    viewMode.requestCustomer(Random(666).nextInt())
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun initListener(){

        signIn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        signUp.setOnClickListener {
            val phone = phoneEdt.text.toString()
            val pwd = pwdEdt.text.toString()
            viewMode.login(RequestLogin(pwd,phone))
        }

        viewMode.responseLogin.observe(this, Observer {
            val response = it.getOrNull()
            if (response != null) {
                if (response.success){
                    UserData.email = response.data.email
                    UserData.id = response.data.id
                    UserData.level = response.data.vipLevel
                    UserData.telPhone = response.data.phone
                    UserData.userName = response.data.name
                    viewMode.refreshCustomerInfo.value = System.currentTimeMillis()
                    runBlocking {
                        try {
                            this@LoginActivity.let { it1 -> DatabaseManager.saveApplication(it1) }
                            if (DatabaseManager.db.loginDao.getSize() != 0){
                                DatabaseManager.db.loginDao.deleteAll()
                            }
                            if (DatabaseManager.db.userDao.getSize() != 0){
                                DatabaseManager.db.userDao.deleteAll()
                            }
                            DatabaseManager.db.loginDao.save(LoginEntity(cookie = UserData.cookie))
                            DatabaseManager.db.userDao.save(UserEntity(userEmail = UserData.email,userId = UserData.id,
                                userName = UserData.userName, userPhone = UserData.telPhone, cookie = UserData.cookie))
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"网络连接失败!",Toast.LENGTH_SHORT).show()
            }
        })

        viewMode.responseCustomer.observe(this, Observer {
            val response = it.getOrNull()
            if (response != null){
                if (response.success){
                    runBlocking {
                        try {
                            val user = DatabaseManager.db.userDao.getAllUserInfo()
                            if (user.isNotEmpty()){
                                UserData.email = user[0].userEmail
                                UserData.id = user[0].userId
                                UserData.level = user[0].level
                                UserData.telPhone = user[0].userPhone
                                UserData.userName = user[0].userName
                                viewMode.refreshCustomerInfo.value = System.currentTimeMillis()
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this,"登陆成功!",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"登录信息失效,请重新登录!",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"网络连接失败!",Toast.LENGTH_SHORT).show()
            }
        })
    }
}