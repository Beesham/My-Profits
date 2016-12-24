package com.myprofits.beesham.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by beesham on 23/12/16.
 */

public class OrderColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String ORDER_ID = "order_id";
    @DataType(DataType.Type.REAL) @NotNull
    public static final String TOTAL_PRICE = "total_price";
}
