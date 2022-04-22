package com.permissionx.gzjj.pojos.network;


import com.permissionx.gzjj.utils.UserData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            //解析Cookie
            for (String header : originalResponse.headers("Set-Cookie")) {
                if(header.contains("RESTAURANT_SYSTEM_")){
                    UserData.cookie = header.substring(header.indexOf("RESTAURANT_SYSTEM_"), header.indexOf(";"));
                }
            }
        }
        return originalResponse;
    }
}
