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

package com.myprofits.beesham.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.myprofits.beesham.data.OrderContract.OrdersEntry;


/**
 * Created by beesham on 23/12/16.
 */

public class OrderDatabase extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    static final String DATABASE_NAME = "orders.db";


    public OrderDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE " + OrdersEntry.TABLE_NAME + "("
                + OrdersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OrdersEntry.COLUMN_CUSTOMER_NAME + " TEXT NOT NULL,"
                + OrdersEntry.COLUMN_ORDER_ID+ " INTEGER NOT NULL, "
                + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + OrdersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
