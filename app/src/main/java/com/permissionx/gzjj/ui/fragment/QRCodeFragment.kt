package com.permissionx.gzjj.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.king.zxing.CameraScan
import com.king.zxing.CaptureActivity
import com.king.zxing.util.CodeUtils
import com.permissionx.gzjj.R
import com.permissionx.gzjj.utils.RealPathFromUriUtils
import com.permissionx.gzjj.pojos.viewModel.ResponseViewModel
import com.permissionx.gzjj.pojos.viewModel.TableIdViewModel
import com.permissionx.gzjj.ui.activity.MainActivity
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import jp.wasabeef.glide.transformations.CropCircleTransformation


class QRCodeFragment() : Fragment(), OnBannerListener {
    private lateinit var startQRCode:Button
    private lateinit var selectQRCodePic:Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var banner: Banner
    private lateinit var viewModel: TableIdViewModel
    private lateinit var responseViewModel: ResponseViewModel
    private lateinit var zbLogo : ImageView


    companion object{
        //注：在本地也可以，只不过适配器类型为Int，之后在add中直接输入例如R.drawable.dog即可
        private val listPath:ArrayList<String> = ArrayList()
        //放标题的集合
        private val listTitle:ArrayList<String> =ArrayList()
        const val TAG = "qrcode"
        const val CAMERA_QR_CODE = 1111
        const val PHOTO_QR_CODE = 2222
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qrcode,container,false)
        startQRCode = view.findViewById(R.id.start_qrcode)
        selectQRCodePic = view.findViewById(R.id.select_picture_of_qrcode)
        toolbar = view.findViewById(R.id.toolBar)
        banner = view.findViewById(R.id.banner)
        zbLogo = view.findViewById(R.id.zb_logo)
        initView()
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(TableIdViewModel::class.java)
        responseViewModel = ViewModelProvider(requireActivity()).get(ResponseViewModel::class.java)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let {
            it.title = "餐厅管理"
        }
        toolbar.inflateMenu(R.menu.toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            if (item != null) {
                when (item.itemId) {
                    R.id.scan_qr -> startActivityForResult(
                        Intent(
                            activity,
                            CaptureActivity::class.java
                        ), CAMERA_QR_CODE
                    )
                }
            }
            true
        }

        Glide.with(this).load(R.drawable.zb_logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(zbLogo)

        startQRCode.setOnClickListener {
            startActivityForResult(Intent(activity, CaptureActivity::class.java), CAMERA_QR_CODE)
        }

        selectQRCodePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PHOTO_QR_CODE)
        }

        responseViewModel.responseFullTable.observe(viewLifecycleOwner, Observer { result->
            val response = result.getOrNull()
            if (response != null){
                if (response.success){
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                    //跳转到点餐界面
                    val activity = activity as MainActivity
                    activity.setFragment2Fragment(object :
                        MainActivity.Fragment2Fragment {
                        override fun gotoFragment(viewPager: ViewPager) {
                            viewPager.refreshDrawableState()
                            viewPager.setCurrentItem(1, false)
                        }
                    })
                    activity.forSkip()
                }else{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show()
            }

        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.scan_qr -> startActivityForResult(Intent(activity, CaptureActivity::class.java), CAMERA_QR_CODE)
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取解析结果
        if (resultCode == RESULT_OK){
            when(requestCode){
                CAMERA_QR_CODE -> {
                    val result = CameraScan.parseScanResult(data)
                    if (result != null){
                        val parts = result.split("-")
                        viewModel.tableName.value = parts[0]
                        viewModel.tableId.value = parts[1]
                        // 此处发送网络请求
                        responseViewModel.requestFullTable(parts[1])
                    }
                }
                PHOTO_QR_CODE -> {
                    if (data != null){
                        parsePhoto(data)
                    }
                }
            }
        }
    }

    private fun parsePhoto(data: Intent){
        val uri = data.data
        val realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(activity,uri)
        Log.d(TAG,realPathFromUri)
        try{
            val bitmap= MediaStore.Images.Media.getBitmap(activity?.contentResolver,uri)
            val result = CodeUtils.parseQRCode(bitmap)
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //实现适配banner的适配器
    private fun initView() {

        listPath.add("https://th.bing.com/th/id/R.529dc5f84c868be06154f4813c95b8f1?rik=jTXgjv%2bPzk9V2w&riu=http%3a%2f%2f5b0988e595225.cdn.sohucs.com%2fimages%2f20190804%2ff6d569cfd8ce4a60b82360dfb4f98757.jpeg&ehk=3OBe2uyiBEtDNFdOM%2fEG%2b06EtPPVYscXuj8ML%2fVzSjM%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1")
        listPath.add("https://th.bing.com/th/id/R.ddc0638af0c31bbb4950d8df8223e5b6?rik=Umc%2fx%2fcpJ9kjLQ&riu=http%3a%2f%2fwww.wjtsm.com%2frepository%2fimage%2f4def3926-246f-43d6-b406-e83b133f7e95.jpg&ehk=EG0OoTUvLe8SBHp2dk6CfeJlw6kOAV554ZEDtvA3YsU%3d&risl=&pid=ImgRaw&r=0")
        listPath.add("https://img-u-3.51miz.com/preview/muban/00/00/51/73/M-517364-958CB4FB.jpg-0.jpg")
        listPath.add("http://seopic.699pic.com/photo/50091/9916.jpg_wh1200.jpg")
        listTitle.add("家常菜")
        listTitle.add("海鲜")
        listTitle.add("烧烤")
        listTitle.add("火锅")

        val myLoader=GlideImageLoader()
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(myLoader)
        //设置图片网址或地址的集合
        banner.setImages(listPath)
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default)
        //设置轮播图的标题集合
        banner.setBannerTitles(listTitle)
        //设置轮播间隔时间
        banner.setDelayTime(3000)
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true)
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
            //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
            .setOnBannerListener(this)
            //必须最后调用的方法，启动轮播图。
            .start()

    }
    //图片轮播加载
    class GlideImageLoader : ImageLoader() {
        override fun displayImage(context: Context?, path: Any, imageView: ImageView?) {
            //Glide 加载图片，Fresco也好、加载本地图片也好，这个类功能就是加载图片
            Glide.with(context!!).load(path).into(imageView!!)
        }
    }
    override fun OnBannerClick(position: Int) {
        Toast.makeText(context, listTitle[position]+":"+listPath[position],Toast.LENGTH_SHORT).show()
    }
}

