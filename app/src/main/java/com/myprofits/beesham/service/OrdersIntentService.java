package com.myprofits.beesham.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.myprofits.beesham.Utils;
import com.myprofits.beesham.data.OrderContract;
import com.myprofits.beesham.data.OrderProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.text.TextUtils.concat;
import static java.security.AccessController.getContext;

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
                this.getContentResolver().delete(OrderContract.OrdersEntry.CONTENT_URI, null, null);
                this.getContentResolver().bulkInsert(OrderContract.OrdersEntry.CONTENT_URI, ordersJsonToContentValues(data));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ContentValues[] ordersJsonToContentValues(String ordersJsonStr) throws JSONException {

        JSONObject ordersJSON = new JSONObject(ordersJsonStr);
        JSONArray ordersArrayJSON = ordersJSON.getJSONArray("orders");

        Vector<ContentValues> contentValuesVector = new Vector<>(ordersArrayJSON.length());

        for(int i=0; i < ordersArrayJSON.length(); i++){
            String order_id;
            String customer_name;
            double subtotal_price;

            JSONObject customerOrderJson = ordersArrayJSON.getJSONObject(i);

            order_id = customerOrderJson.getString("id");
            customer_name = customerOrderJson.getJSONObject("customer").getString("first_name");
            customer_name = customer_name.concat(
                    customerOrderJson.getJSONObject("customer").getString("last_name"));
            subtotal_price =customerOrderJson.getDouble("subtotal_price");

            ContentValues contentValues = new ContentValues();
            contentValues.put(OrderContract.OrdersEntry.COLUMN_CUSTOMER_NAME, customer_name);
            contentValues.put(OrderContract.OrdersEntry.COLUMN_ORDER_ID, order_id);
            contentValues.put(OrderContract.OrdersEntry.COLUMN_SUBTOTAL_PRICE, subtotal_price);

            contentValuesVector.add(contentValues);
        }

        ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
        contentValuesVector.toArray(contentValuesArray);
        return contentValuesArray;
    }

}
