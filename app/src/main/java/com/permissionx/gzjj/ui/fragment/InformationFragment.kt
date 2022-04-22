package com.permissionx.gzjj.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.permissionx.gzjj.R
import com.permissionx.gzjj.utils.DataUtils
import com.permissionx.gzjj.adapter.LAdapter
import com.permissionx.gzjj.adapter.RAdapter
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.permissionx.gzjj.pojos.*
import com.permissionx.gzjj.pojos.dataBase.DatabaseManager
import com.permissionx.gzjj.pojos.dataBase.HistoryOrderEntity
import com.permissionx.gzjj.pojos.network.request.CommitOrderItem
import com.permissionx.gzjj.pojos.network.request.RequestOrder
import com.permissionx.gzjj.pojos.network.response.MenuDataItem
import com.permissionx.gzjj.pojos.viewModel.HistoryOrderViewModel
import com.permissionx.gzjj.pojos.viewModel.ResponseViewModel
import com.permissionx.gzjj.pojos.viewModel.TableIdViewModel
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class InformationFragment : Fragment() {

    private lateinit var linkBean: LinkBean
    private lateinit var lAdapter: LAdapter
    private lateinit var rAdapter: RAdapter
    private lateinit var rvL: RecyclerView
    private lateinit var rvR: RecyclerView
    private lateinit var tvHead: TextView
    private lateinit var totOrder:TextView
    private lateinit var totPay:TextView
    private lateinit var allPay:TextView
    private lateinit var gotoPay:CardView
    private lateinit var box:ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var moveToTop = false
    private lateinit var  mactivity : AppCompatActivity
    private val buyList = mutableListOf<CommitOrderItem>()
    private val orderList = mutableListOf<MenuDataItem>()
    private val historyList = mutableListOf<HistoryOrderEntity>()
    //ViewModel
    private lateinit var viewModel : TableIdViewModel
    private lateinit var responseViewModel: ResponseViewModel
    private lateinit var historyOrderViewModel: HistoryOrderViewModel
    private var index by Delegates.notNull<Int>()

    private fun initData(){
        linkBean = LinkBean()
        linkBean.itemLS = ArrayList()
        linkBean.itemS = ArrayList()
        for (i in (0..12)){
            val itemL = LinkBean.ItemL()
            itemL.title = "分类$i"
            linkBean.itemLS.add(itemL)
            for (j in (0..16)){
                if (i % 2 == 0 && j % 2 == 0){
                    continue
                }else{
                    val item = LinkBean.Item()
                    item.title = "分类$i"
                    item.name = "名称$j"
                    item.price = "100"
                    item.content = "哦以西!好吃!testy!delicious!"
                    item.imageUrl = "https://th.bing.com/th/id/OIP.jGyfVaV4CLyfSQQQjcom7AHaHa?pid=ImgDet&rs=1"
                    item.discount = 100
                    linkBean.itemS.add(item)
                }
            }
        }
    }

    private fun initView(){
        tvHead.text = linkBean.itemLS[0].title
        rvL.layoutManager = object :LinearLayoutManager(context){}
        rvR.layoutManager = object :LinearLayoutManager(context){}
        lAdapter = LAdapter(context, com.permissionx.gzjj.R.layout.item,linkBean.itemLS)
        lAdapter.bindToRecyclerView(rvL)
        rvL.adapter = lAdapter
        rAdapter = RAdapter(context, com.permissionx.gzjj.R.layout.goods,linkBean.itemS)
        rvR.adapter = rAdapter
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initListener(){
        rvL.addOnItemTouchListener(object : OnRecyclerItemClickListener(rvL){
            override fun onItemClick(vh: RecyclerView.ViewHolder?) {
                if (rvR.scrollState != RecyclerView.SCROLL_STATE_IDLE) return
                lAdapter.fromClick = true
                if (vh != null) {
                    lAdapter.setChecked(vh.position)
                    val tag = lAdapter.datas[vh.position].title
                    for (i in (0..rAdapter.datas.size)){
                        if (TextUtils.equals(tag,rAdapter.datas[i].title)){
                            index = i
                            moveToPosition(index)
                            return
                        }
                    }
                }
            }
        })

        rvR.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = rvR.layoutManager as LinearLayoutManager
                if (moveToTop){
                    moveToTop = false
                    val m = index - layoutManager.findFirstVisibleItemPosition()
                    if (m >= 0 && m <= layoutManager.childCount){
                        val top = layoutManager.getChildAt(m)?.top
                        if (top != null) {
                            rvR.smoothScrollBy(0,top)
                        }
                    }
                }else{
                    val index = layoutManager.findFirstVisibleItemPosition()
                    tvHead.text = rAdapter.datas[index].title
                    lAdapter.setToPosition(rAdapter.datas[index].title)
                }
            }
        })

        rvR.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                lAdapter.fromClick = false
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }
        })

        rAdapter.setOnItemClickListener {
            update()
        }

        gotoPay.setOnClickListener {
            myDialog()
        }
    }

    private fun initObverse(){
        viewModel.tableName.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mactivity.supportActionBar?.let {
                it.title = "Hi~" + viewModel.tableName.value
            }
        })

        responseViewModel.responseMenu.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
            val response = result.getOrNull()
            if (response != null){
                if (response.success){
                    if (response.data.isNotEmpty()){
                        linkBean.itemS.clear()
                        orderList.clear()
                        response.data.forEach {
                            orderList.add(it)
                            val item = LinkBean.Item()
                            item.title = it.type
                            item.name = it.name
                            item.price = it.price
                            item.content = it.introduce
                            item.material = it.material
                            item.imageUrl = it.picture
                            item.discount = it.discount
                            linkBean.itemS.add(item)
                        }
                        rvR.adapter = rAdapter
                        initListener()
                    }
                }else{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show()
            }
        })

        responseViewModel.responseMenuType.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
            val response = result.getOrNull()
            if (response != null){
                if (response.success){
                    if (response.data.isNotEmpty()){
                        linkBean.itemLS.clear()
                        response.data.forEach {
                            val item = LinkBean.ItemL()
                            item.title = it
                            linkBean.itemLS.add(item)
                        }
                        rvL.adapter = lAdapter
                        initListener()
                    }
                }else{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show()
            }
        })

        responseViewModel.responseCommitOrder.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            if (response != null){
                if (response.success){
                    refreshRv()
                    runBlocking {
                        try {
                            if (historyList.isNotEmpty()){
                                historyList.forEach { h ->
                                    context?.let { it1 -> DatabaseManager.saveApplication(it1) }
                                    val id = DatabaseManager.db.historyOrderDao.save(h)
                                    id.map { ID ->
                                        Log.d("!!!id","$ID")
                                    }
                                }
                                historyOrderViewModel.shouldRefresh.value = System.currentTimeMillis()
                                historyList.clear()
                                buyList.clear()
                                linkBean.itemS.forEach { it2 ->
                                    if (it2.num != 0){
                                        it2.num = 0
                                    }
                                }
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun moveToPosition(index: Int){
        val layoutManager = rvR.layoutManager as LinearLayoutManager
        val f = layoutManager.findFirstVisibleItemPosition()
        val l = layoutManager.findLastVisibleItemPosition()
        if (index <= f){
            layoutManager.scrollToPosition(index)
        }else if (index <= l){
            val m = index - f
            if (0 <= m && m <= layoutManager.childCount){
                val top = layoutManager.getChildAt(m)?.top
                if (top != null) {
                    rvR.smoothScrollBy(0, top)
                }
            }
        }else{
            moveToTop = true
            layoutManager.scrollToPosition(index)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.permissionx.gzjj.R.layout.fragment_info, container, false)
        tvHead = view.findViewById(com.permissionx.gzjj.R.id.tv_header)
        rvL = view.findViewById(com.permissionx.gzjj.R.id.rv1)
        rvR = view.findViewById(com.permissionx.gzjj.R.id.rv2)
        viewModel = ViewModelProvider(requireActivity()).get(TableIdViewModel::class.java)
        responseViewModel = ViewModelProvider(requireActivity()).get(ResponseViewModel::class.java)
        historyOrderViewModel = ViewModelProvider(requireActivity()).get(HistoryOrderViewModel::class.java)
        toolbar = view.findViewById(com.permissionx.gzjj.R.id.toolBar_order)
        totOrder = view.findViewById(com.permissionx.gzjj.R.id.total_order)
        box = view.findViewById(R.id.box)
        totPay = view.findViewById(com.permissionx.gzjj.R.id.total_money)
        allPay = view.findViewById(R.id.all_money)
        gotoPay = view.findViewById(R.id.goto_pay)
        setHasOptionsMenu(true)
        mactivity = activity as AppCompatActivity
        mactivity.setSupportActionBar(toolbar)
        mactivity.supportActionBar?.let {
            it.title = "Hi~" + viewModel.tableName.value
        }
        initData()
        initView()
        initObverse()
        return view

    }

    @SuppressLint("SetTextI18n")
    private fun update(){
        if (DataUtils.totalOrderLiveData.value == 0){
            totOrder.visibility = View.INVISIBLE
            box.setImageResource(R.drawable.eatbox)
        }else{
            totOrder.text = DataUtils.totalOrderLiveData.value?.let { formatOrderNum(it) }
            totOrder.visibility = View.VISIBLE
            box.setImageResource(R.drawable.box_open)
        }
        //总价格
        allPay.text = "预估价格:${DataUtils.allPayLiveData.value}￥"
        allPay.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        //折后价格
        val price = (DataUtils.totalPayLiveData.value?.times(DataUtils.discount))?.div(100)
        totPay.text = "折后价格:${price}￥"
    }

    @SuppressLint("SimpleDateFormat")
    private fun myDialog() {
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

        // 设置内容
        tvContent.text = "是否确认提交订单?"

        //监听
        tvQuxiao.setOnClickListener {
            //确认
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateString = formatter.format(date)
            val orderId = System.currentTimeMillis()
            linkBean.itemS.forEachIndexed { index, item ->
                val order = orderList[index]
                if (item.num != 0){
                    buyList.add(CommitOrderItem(order.discount,order.name,order.picture,order.price,order.type,"1",item.num))
                    historyList.add(HistoryOrderEntity(
                        userId = "",
                        name = item.name,
                        num = item.num.toString(),
                        date = dateString,
                        price = (item.price.toInt() * item.num).toString(),
                        content = item.material,
                        imageUrl = item.imageUrl,
                    orderId = orderId))
                }
            }
            val requestBody = RequestOrder("",viewModel.tableId.value,"手机付款",
                DataUtils.totalPayLiveData.value.toString(), buyList)
            //此处发送网络请求
            responseViewModel.commitOrder(requestBody)
            Log.d("!!!!",requestBody.menus.size.toString())
            dialog.dismiss()
        }
        tvXianshi.setOnClickListener {
            //取消
            linkBean.itemS.forEach {
                it.num = 0
            }
            refreshRv()
            Toast.makeText(context,"订单取消成功",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    } //myDialog

    private fun refreshRv(){
        rvR.adapter = rAdapter
        DataUtils.totalOrderLiveData.value = 0
        DataUtils.totalPayLiveData.value = 0
        DataUtils.allPayLiveData.value = 0
        update()
    }

    fun formatOrderNum(num:Int) : String{
        if (num >= 10) return " ${DataUtils.totalOrderLiveData.value}"
        return "  ${DataUtils.totalOrderLiveData.value}"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}