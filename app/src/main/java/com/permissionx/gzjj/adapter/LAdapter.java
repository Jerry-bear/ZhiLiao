package com.permissionx.gzjj.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.permissionx.gzjj.R;
import com.permissionx.gzjj.pojos.LinkBean;

import java.util.List;

public class LAdapter extends BaseRecyclerViewAdapter<LinkBean.ItemL>{

    public LAdapter(Context context, int resLayout, List<LinkBean.ItemL> data) {
        super(context, resLayout, data);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, List<LinkBean.ItemL> items, int position) {

    }

    @Override
    public void convert(BaseRecyclerHolder holder, int position) {
        TextView tv = ((TextView)holder.getView(R.id.tv));
        tv.setText(getDatas().get(position).getTitle());
        if (checked == position){
            tv.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            tv.setBackgroundResource(R.color.white);
        }else {
            tv.setTextColor(ContextCompat.getColor(context,R.color.black));
            tv.setBackgroundResource(R.color.white);
        }
    }


    private int checked; //当前选中项
    public boolean fromClick; //是否是自己点击的

    public void setChecked(int checked) {
        this.checked = checked;
        notifyDataSetChanged();
    }


    //让左边的额条目选中
    public void setToPosition(String title){
        if (fromClick) return;
        if (TextUtils.equals(title,getDatas().get(checked).getTitle()))return;
        if (TextUtils.isEmpty(title))return;
        for (int i = 0; i < getDatas().size(); i++) {
            if (TextUtils.equals(getDatas().get(i).getTitle(),title)){
                setChecked(i);
                moveToPosition(i);
                return;
            }
        }
    }

    private void moveToPosition(int index){
        //如果选中的条目不在显示范围内，要滑动条目让该条目显示出来
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
        int f = linearLayoutManager.findFirstVisibleItemPosition();
        int l = linearLayoutManager.findLastVisibleItemPosition();
        if (index<=f || index >= l){
            linearLayoutManager.scrollToPosition(index);
        }

    }

}

