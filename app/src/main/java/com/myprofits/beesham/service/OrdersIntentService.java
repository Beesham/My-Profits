package com.myprofits.beesham.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.myprofits.beesham.data.OrderContract;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.myprofits.beesham.Utils.checkResultsPageSize;
import static com.myprofits.beesham.Utils.ordersJsonToContentValues;


/**
 * Created by beesham on 23/12/16.
 */

public class OrdersIntentService extends IntentService{

    private static final String LOG_TAG = OrdersIntentService.class.getSimpleName();

    public OrdersIntentService() {
        super("OrdersIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String data = null;
        int pageInt = 1;

        try {
            do {

                Request request = new Request.Builder()
                        .url(buildUri(Integer.toString(pageInt)).toString())
                        .build();

                Response response = null;

                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response != null) {
                    try {
                        data = response.body().string();
                        if(checkResultsPageSize(data) > 0) {
                            this.getContentResolver().delete(OrderContract.OrdersEntry.CONTENT_URI, null, null);
                            this.getContentResolver().bulkInsert(OrderContract.OrdersEntry.CONTENT_URI,
                                    ordersJsonToContentValues(data));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pageInt++;
            }while(checkResultsPageSize(data) > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Uri buildUri(String page){
        final String BASE_URL = "https://shopicruit.myshopify.com/admin/orders.json?";
        final String PAGE_PARAM = "page";
        final String ACCESS_TOKEN_PARAM = "access_token";

        Uri orderUrl = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PAGE_PARAM, page)
                .appendQueryParameter(ACCESS_TOKEN_PARAM, "c32313df0d0ef512ca64d5b336a0d7c6")
                .build();

        Log.v(LOG_TAG, "url: " + orderUrl.toString());
        return orderUrl;
    }

}
