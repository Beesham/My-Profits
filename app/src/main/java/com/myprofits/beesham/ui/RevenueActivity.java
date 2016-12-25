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

package com.myprofits.beesham.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.myprofits.beesham.R;
import com.myprofits.beesham.data.OrderContract;
import com.myprofits.beesham.service.OrdersIntentService;

public class RevenueActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RevenueActivity.class.getSimpleName();
    private Intent mServiceIntent;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        setContentView(R.layout.activity_revenue);

        mServiceIntent = new Intent(this, OrdersIntentService.class);
        if (savedInstanceState == null){
            if (isConnected){
                startService(mServiceIntent);
            } else{
                networkToast();
            }
        }
    }

    public void networkToast(){
        Toast.makeText(this, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                OrderContract.OrdersEntry._ID,
                OrderContract.OrdersEntry.COLUMN_ORDER_ID,
                OrderContract.OrdersEntry.COLUMN_CUSTOMER_NAME,
                OrderContract.OrdersEntry.COLUMN_SUBTOTAL_PRICE
        };

        CursorLoader loader = null;
        switch(id) {
            case 1:
                Log.v(LOG_TAG, "Loading from movies");
                loader = new CursorLoader(this,
                        OrderContract.OrdersEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
                break;

            case 2:
                Log.v(LOG_TAG, "Loading from movies favorite");
                loader = new CursorLoader(this,
                        OrderContract.OrdersEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
