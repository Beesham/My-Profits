/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myprofits.beesham;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.myprofits.beesham.data.OrderContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Beesham on 12/24/2016.
 */

public class Utils {
    private static String LOG_TAG = Utils.class.getSimpleName();

    public static String formatDouble(double revenue){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(revenue);
    }

    public static int checkResultsPageSize(String ordersJsonStr) throws JSONException {
        JSONObject ordersJSON = new JSONObject(ordersJsonStr);
        JSONArray ordersArrayJSON = ordersJSON.getJSONArray("orders");

        return ordersArrayJSON.length();
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

    public static Uri buildUri(String page){
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
