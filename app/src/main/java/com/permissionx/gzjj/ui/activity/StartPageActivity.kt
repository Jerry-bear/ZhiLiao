package com.permissionx.gzjj.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.permissionx.gzjj.R
import kotlinx.coroutines.delay
import java.lang.Exception

class StartPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread{
            try {
                Thread.sleep(2000)
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }.start()
    }
}