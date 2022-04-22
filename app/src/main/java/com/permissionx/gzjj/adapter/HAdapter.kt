package com.permissionx.gzjj.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.permissionx.gzjj.R
import com.permissionx.gzjj.pojos.HistoryOrder
import com.permissionx.gzjj.pojos.LinkBean
import com.permissionx.gzjj.pojos.MyImageView
import java.security.AccessControlContext

class HAdapter(context: Context?, resLayout: Int, data:List<HistoryOrder>)
    : BaseRecyclerViewAdapter<HistoryOrder>(context,resLayout,data) {

    private lateinit var onAgainClickListener: OnAgainClickListener

    fun makeOnAgainClickListener(onAgainClickListener: OnAgainClickListener){
        this.onAgainClickListener = onAgainClickListener
    }
    override fun convert(
        holder: BaseRecyclerHolder?,
        items: MutableList<HistoryOrder>?,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseRecyclerHolder?, position: Int) {
        (holder?.getView<TextView>(R.id.hname))?.text = datas[position].name
        (holder?.getView<MyImageView>(R.id.hImage))?.setImageURL(datas[position].imageUrl)
        (holder?.getView<TextView>(R.id.hcontent))?.text = datas[position].content
        (holder?.getView<TextView>(R.id.htotnum))?.text = "共" + datas[position].num + "件"
        (holder?.getView<TextView>(R.id.htotprice))?.text = "￥" + datas[position].price
        (holder?.getView<TextView>(R.id.hdate))?.text = datas[position].date
        holder?.getView<androidx.cardview.widget.CardView>(R.id.hagain)?.setOnClickListener {
            onAgainClickListener.again()
        }
    }
    interface OnAgainClickListener{
        fun again()
    }
}