package com.myprofits.beesham.service;

import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by beesham on 23/12/16.
 */
public class OrdersIntentServiceTest{

    @Mock
    Context context;

    @Test
    public void testService(){
        Intent i = new Intent(context, OrdersIntentService.class);
        context.startService(i);
    }
}