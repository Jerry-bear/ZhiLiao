package com.permissionx.gzjj.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.permissionx.gzjj.R
import com.permissionx.gzjj.pojos.viewModel.ResponseViewModel
import com.permissionx.gzjj.ui.fragment.FixFragment
import com.permissionx.gzjj.ui.fragment.InformationFragment
import com.permissionx.gzjj.ui.fragment.QRCodeFragment
import com.permissionx.gzjj.ui.fragment.UserFragment
import com.permissionx.gzjj.utils.DataUtils
import com.permissionx.gzjj.utils.UserData
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val fragmentList = listOf(QRCodeFragment(), InformationFragment(), FixFragment(), UserFragment())
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    val mPermissionList = ArrayList<String>()
    val mRequestCode = 0x1
    private lateinit var responseViewModel: ResponseViewModel
    private lateinit var totOrder:TextView
    private lateinit var totPay:TextView
    private lateinit var fragment2Fragment:Fragment2Fragment
    private lateinit var contentViewPager: ViewPager
    private lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        initPermission()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("!!!n",UserData.userName)
        responseViewModel = ViewModelProvider(this).get(ResponseViewModel::class.java)
        responseViewModel.requestMenu()
        responseViewModel.requestDiscount(Random(888).nextInt())
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        //隐藏状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        contentViewPager = findViewById(R.id.content_view_pager)
        //设置 fragment页面的缓存数量 , 这里设置成缓存所有的页面！！！！！
        contentViewPager.offscreenPageLimit = fragmentList.size
        contentViewPager.adapter = Adapter(supportFragmentManager)
        bottomNav = findViewById(R.id.bottom_nav)

        initListener()
        initObverse()

    }

    inner class Adapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }
    }

    private fun initListener(){
        // 给底部导航栏的菜单项添加点击事件
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                // smoothScroll=false这个参数能解决切换时的多页闪烁问题
                R.id.nav_qr_code -> contentViewPager.setCurrentItem(0, false)
                R.id.nav_information -> contentViewPager.setCurrentItem(1, false)
                R.id.nav_fix -> contentViewPager.setCurrentItem(2, false)
                R.id.nav_user -> contentViewPager.setCurrentItem(3, false)
            }
            false
        }
        // 当页面切换时，将对应的底部导航栏菜单项设置为选中状态
        contentViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                // 将对应的底部导航栏菜单项设置为选中状态
                bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }

    //动态权限设置
    private fun initPermission() {
        mPermissionList.clear()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionList.add(permission)
            }
        }
        if (mPermissionList.isNotEmpty()) {
            // 后续操作...c
            ActivityCompat.requestPermissions(this@MainActivity, permissions, mRequestCode)
            Toast.makeText(this@MainActivity,"请授予权限",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObverse(){

        responseViewModel.responseDiscount.observe(this, androidx.lifecycle.Observer {
            val response = it.getOrNull()
            if (response != null){
                if (response.success){
                    DataUtils.discount = response.data.toInt()
                }
            }
        })
    }

    fun setFragment2Fragment(fragment2Fragment: Fragment2Fragment){
        this.fragment2Fragment = fragment2Fragment
    }

    fun forSkip(){
        fragment2Fragment.gotoFragment(contentViewPager)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0x1 -> for (element in grantResults) {
                if (element != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "您有未授予的权限，可能影响使用", Toast.LENGTH_SHORT).show()
            }
        }
    }
    interface Fragment2Fragment{
        fun gotoFragment(viewPager: ViewPager)
    }

}