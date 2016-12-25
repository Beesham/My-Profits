package com.myprofits.beesham.service;

import android.app.IntentService;
import android.content.Intent;

import com.myprofits.beesham.data.OrderContract;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.myprofits.beesham.Utils.buildUri;
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

        //delete stale data
        this.getContentResolver().delete(OrderContract.OrdersEntry.CONTENT_URI, null, null);

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
                            this.getContentResolver().bulkInsert(OrderContract.OrdersEntry.CONTENT_URI,
                                    ordersJsonToContentValues(data));


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
}
