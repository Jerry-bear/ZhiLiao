package com.permissionx.gzjj.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BaseRecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> mViews;
    public BaseRecyclerHolder(View itemView) {
        super(itemView);
        mViews = new SparseArrayCompat<>();
    }
    public <V extends View> V getView(@IdRes int res){
        View v = mViews.get(res);
        if (v == null){
            v = itemView.findViewById(res);
            mViews.put(res,v);
        }
        return (V)v;
    }
    /**
     * 直接赋值textview
     * @param TvRes
     * @param text
     */
    public BaseRecyclerHolder setText(@IdRes int TvRes,CharSequence text){
        TextView textView = getView(TvRes);
        textView.setText(text);
        return this;
    }
}
