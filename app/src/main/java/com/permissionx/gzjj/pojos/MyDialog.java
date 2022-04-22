package com.permissionx.gzjj.pojos;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.permissionx.gzjj.R;

public class MyDialog extends Dialog {
    /**
     * 构造方法设置样式
     * 最重要的地方
     * @param context
     */
    public MyDialog(final Context context) {
//      设置样式
        super(context, R.style.CustomDialog);
    }

    /**
     *
     *引入布局文件，并显示
     */
    public View createView(Context context, int dialog_layout){
//      放入布局文件
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      布局转成View对象
        View view = inflate.inflate(dialog_layout, null);
//      显示布局
        setContentView(view);
        return view;

    }
}
