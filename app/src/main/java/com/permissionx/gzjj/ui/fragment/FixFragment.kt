package com.permissionx.gzjj.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.permissionx.gzjj.R
import com.permissionx.gzjj.adapter.HAdapter
import com.permissionx.gzjj.pojos.HistoryOrder
import com.permissionx.gzjj.pojos.viewModel.HistoryOrderViewModel
import com.permissionx.gzjj.pojos.dataBase.DatabaseManager
import com.permissionx.gzjj.pojos.dataBase.HistoryOrderEntity
import com.permissionx.gzjj.ui.activity.MainActivity
import kotlinx.coroutines.runBlocking

class FixFragment:Fragment() {

    private lateinit var hAdapter:HAdapter
    private lateinit var rvH:RecyclerView
    private lateinit var hViewModel: HistoryOrderViewModel
    private val hListBean = mutableListOf<HistoryOrder>()
    private lateinit var hOrderList : List<HistoryOrderEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fix,container,false)
        rvH = view.findViewById(R.id.hrv)
        hViewModel = ViewModelProvider(requireActivity()).get(HistoryOrderViewModel::class.java)
        initData()
        initView()
        initObverse()
        return view

    }

    private fun initData(){

        runBlocking {
            try {
                context?.let { it1 -> DatabaseManager.saveApplication(it1) }
                hOrderList = DatabaseManager.db.historyOrderDao.getAllHistoryOrder()
                hOrderList.forEach {
                    hListBean.add(HistoryOrder(it.name,it.num,it.date,it.price,it.content,it.imageUrl))
                }
                hViewModel.historyOrderList.value = hListBean
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun initView(){
        rvH.layoutManager = object :LinearLayoutManager(context){}
        hAdapter = HAdapter(context,R.layout.history_goods,hListBean)
        hAdapter.bindToRecyclerView(rvH)
        rvH.adapter = hAdapter
    }

    private fun initListener(){
        hAdapter.makeOnAgainClickListener(object :HAdapter.OnAgainClickListener{
            override fun again() {
                val activity = activity as MainActivity
                activity.setFragment2Fragment(object :
                    MainActivity.Fragment2Fragment {
                    override fun gotoFragment(viewPager: ViewPager) {
                        viewPager.refreshDrawableState()
                        viewPager.setCurrentItem(1, false)
                    }
                })
                activity.forSkip()
            }

        })
    }

    private fun initObverse(){

        hViewModel.historyOrderList.observe(viewLifecycleOwner, Observer {

            rvH.adapter = hAdapter
            initListener()
        })

        hViewModel.shouldRefresh.observe(viewLifecycleOwner, Observer {
            initData()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}