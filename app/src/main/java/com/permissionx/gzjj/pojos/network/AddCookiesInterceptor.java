package com.permissionx.gzjj.pojos.network;

import android.text.TextUtils;

import com.permissionx.gzjj.utils.UserData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();

        if(!TextUtils.isEmpty(UserData.cookie)){
            builder.addHeader("Cookie", UserData.cookie);
        }
        return chain.proceed(builder.build());
    }
}