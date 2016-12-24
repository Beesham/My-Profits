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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static android.media.tv.TvContract.Programs.Genres.MOVIES;

/**
 * Created by beesham on 23/12/16.
 */

public class OrderProvider extends ContentProvider{

    private static final String LOG_TAG = OrderProvider.class.getSimpleName();

    private OrderDatabase mOrderDatabase;

    private static final int ORDERS = 100;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_ORDERS, ORDERS);
    }

    @Override
    public boolean onCreate() {
        mOrderDatabase = new OrderDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case ORDERS:
                retCursor = mOrderDatabase.getReadableDatabase().query(
                        OrderContract.OrdersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case ORDERS:
                return OrderContract.OrdersEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mOrderDatabase.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case ORDERS:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues contentValues : values){
                        long _id = mOrderDatabase.getWritableDatabase().insert(
                                OrderContract.OrdersEntry.TABLE_NAME,
                                null,
                                contentValues);
                        if(_id != -1) returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;

        //If selection is null, delete all rows
        if(selection == null){
            selection = "1";
        }

        switch (sUriMatcher.match(uri)){
            case ORDERS:
                rowsDeleted = mOrderDatabase.getWritableDatabase().delete(
                        OrderContract.OrdersEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
