package com.permissionx.gzjj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    public Context context;
    public int layoutRes;
    public List<T> items;
    private RecyclerView recyclerView;

    public BaseRecyclerViewAdapter(Context context, @LayoutRes int layoutRes){
        this.context = context;
        this.layoutRes = layoutRes;
        items = new ArrayList<T>();
    }
    public BaseRecyclerViewAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<T> data) {
        this.context = context;
        this.items = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.layoutRes = layoutResId;
        }
    }


    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerHolder(LayoutInflater.from(context).inflate(layoutRes,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position) {
        convert(holder ,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setNewData(List<T> items) {
        this.items = items == null ? new ArrayList<T>() : items;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return items;
    }

    /**
     * change data
     * 改变某一位置数据
     */
    public void setData(int index, T data) {
        if (index >= this.items.size()) {
            return;
        }
        this.items.set(index , data);
        notifyItemChanged(index);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        items.add(position, data);
        notifyItemInserted(position);
        compatibilityDataSizeChanged(1);
    }
    public void addData( @NonNull T data) {
        items.add(data);
        notifyItemInserted(items.size());
    }


    public void remove(int position) {
        if (position >= items.size()) return;

        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size() - position);
    }

    public void remove(T item) {
        final int position = items.indexOf(item);
        if (-1 == position)
            return;
        remove(position);
    }


    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = items == null ? 0 : items.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }
    public void bindToRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 需要重写的方法
     * @param holder
     * @param position
     */
    public abstract void convert(BaseRecyclerHolder holder , List<T> items, int position);

    public abstract void convert(BaseRecyclerHolder holder, int position);
}
