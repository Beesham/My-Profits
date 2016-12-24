package com.myprofits.beesham.service;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by beesham on 23/12/16.
 */

public class OrdersIntentService extends IntentService{

    public OrdersIntentService() {
        super("OrdersIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();

        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null){
            try {
                String data = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
