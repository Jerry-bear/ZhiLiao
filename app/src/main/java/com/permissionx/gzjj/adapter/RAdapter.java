package com.permissionx.gzjj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.permissionx.gzjj.R;
import com.permissionx.gzjj.utils.DataUtils;
import com.permissionx.gzjj.pojos.LinkBean;
import com.permissionx.gzjj.pojos.MyImageView;

import java.util.List;

public class RAdapter extends BaseRecyclerViewAdapter<LinkBean.Item>{

    public OnItemClickListener onItemClickListener;

    public RAdapter(Context context, int resLayout, List<LinkBean.Item> data) {
        super(context, resLayout, data);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, List<LinkBean.Item> items, int position) {

    }

    public interface OnItemClickListener {
        void update();
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void convert(BaseRecyclerHolder holder, final int position) {
        ((TextView)holder.getView(R.id.tvName)).setText(getDatas().get(position).getName());
        if (getDatas().get(position).getDiscount() == 100){
            ((TextView)holder.getView(R.id.tvPrice)).setText(getDatas().get(position).getPrice()+"￥");
            ((TextView)holder.getView(R.id.disPrice)).setVisibility(View.INVISIBLE);
            ((TextView)holder.getView(R.id.tvPrice)).getPaint().setFlags(0);
        }else {
            ((TextView)holder.getView(R.id.tvPrice)).setText(getDatas().get(position).getPrice()+"￥");
            ((TextView)holder.getView(R.id.disPrice)).setVisibility(View.VISIBLE);
            ((TextView)holder.getView(R.id.disPrice)).setText(String.valueOf((Integer.parseInt(getDatas().get(position).getPrice())
                    * getDatas().get(position).getDiscount() / 100)) + "￥");
            ((TextView)holder.getView(R.id.tvPrice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ((TextView)holder.getView(R.id.tvContent)).setText(getDatas().get(position).getContent());
        ((MyImageView) holder.getView(R.id.tvImage)).setImageURL(getDatas().get(position).getImageUrl());
        ((TextView) holder.getView(R.id.num_order)).setText(String.valueOf(getDatas().get(position).getNum()));
        if (getDatas().get(position).getNum() == 0){
            ((ImageView) holder.getView(R.id.sub_order)).setVisibility(View.GONE);
            ((TextView) holder.getView(R.id.num_order)).setVisibility(View.GONE);
        }else {
            ((ImageView) holder.getView(R.id.sub_order)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.num_order)).setVisibility(View.VISIBLE);
        }
        //悬停的标题头
        FrameLayout headLayout = holder.getView(R.id.stick_header);
        TextView tvHead = holder.getView(R.id.tv_header);
        if (position == 0){
            headLayout.setVisibility(View.VISIBLE);
            tvHead.setText(getDatas().get(position).getTitle());
        }else {
            if (TextUtils.equals(getDatas().get(position).getTitle(),getDatas().get(position-1).getTitle())){
                headLayout.setVisibility(View.GONE);
            }else {
                headLayout.setVisibility(View.VISIBLE);
                tvHead.setText(getDatas().get(position).getTitle());
            }
        }

        holder.getView(R.id.plus_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatas().get(position).addNum();
                int totalOrder = DataUtils.INSTANCE.getTotalOrderLiveData().getValue();
                DataUtils.INSTANCE.setTotalOrderLiveData(new MutableLiveData<>(++totalOrder));
                int totalPrice = DataUtils.INSTANCE.getTotalPayLiveData().getValue();
                int allPrice = DataUtils.INSTANCE.getAllPayLiveData().getValue();
                DataUtils.INSTANCE.setTotalPayLiveData(new MutableLiveData<>(totalPrice
                        + Integer.parseInt(getDatas().get(position).getPrice()) * getDatas().get(position).getDiscount() / 100));
                DataUtils.INSTANCE.setAllPayLiveData(new MutableLiveData<>(allPrice
                        + Integer.parseInt(getDatas().get(position).getPrice())));
                ((ImageView) holder.getView(R.id.sub_order)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.num_order)).setVisibility(View.VISIBLE);
                String totNum = String.valueOf(getDatas().get(position).getNum());
                ((TextView) holder.getView(R.id.num_order)).setText(totNum);
                Log.d("!!!",DataUtils.INSTANCE.getTotalOrderLiveData().getValue().toString());
                onItemClickListener.update();
            }
        });
        holder.getView(R.id.sub_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatas().get(position).subNum();
                int totNum = getDatas().get(position).getNum();
                int totalOrder = DataUtils.INSTANCE.getTotalOrderLiveData().getValue();
                DataUtils.INSTANCE.setTotalOrderLiveData(new MutableLiveData<>(--totalOrder));
                int totalPrice = DataUtils.INSTANCE.getTotalPayLiveData().getValue();
                int allPrice = DataUtils.INSTANCE.getAllPayLiveData().getValue();
                DataUtils.INSTANCE.setAllPayLiveData(new MutableLiveData<>(allPrice
                        - Integer.parseInt(getDatas().get(position).getPrice())));
                DataUtils.INSTANCE.setTotalPayLiveData(new MutableLiveData<>(totalPrice - Integer.parseInt(getDatas().get(position).getPrice())
                        * getDatas().get(position).getDiscount() / 100));
                if (totNum == 0){
                    ((ImageView) holder.getView(R.id.sub_order)).setVisibility(View.GONE);
                    ((TextView) holder.getView(R.id.num_order)).setVisibility(View.GONE);
                }else {
                    ((TextView) holder.getView(R.id.num_order)).setText(String.valueOf(totNum));
                    Log.d("!!!",DataUtils.INSTANCE.getTotalOrderLiveData().getValue().toString());
                }
                onItemClickListener.update();
            }
        });

        holder.getView(R.id.tvImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyImageView) holder.getView(R.id.tvImage)).setImageURL(getDatas().get(position).getImageUrl());
                Log.d("!!!",getDatas().get(position).getImageUrl());
            }
        });
    }
}
