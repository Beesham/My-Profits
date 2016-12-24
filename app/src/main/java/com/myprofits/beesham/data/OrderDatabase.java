package com.myprofits.beesham.data;

import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.annotation.Database;

/**
 * Created by beesham on 23/12/16.
 */

public class OrderDatabase {
    private OrderDatabase(){}

    public static final int VERSION = 1;

    @Table(OrderColumns.class) public static final String ORDERS = "orders";
}