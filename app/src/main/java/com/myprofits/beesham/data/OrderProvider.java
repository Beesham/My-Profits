package com.myprofits.beesham.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by beesham on 23/12/16.
 */

@ContentProvider(authority = OrderProvider.AUTHORITY, database = OrderDatabase.class)
public class OrderProvider {
    public static final String AUTHORITY = "com.myprofits.beesham.data.OrderProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String ORDERS = "orders";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = OrderDatabase.ORDERS)
    public static class Orders {
        @ContentUri(
                path = Path.ORDERS,
                type = "vnd.android.cursor.dir/order"
        )
        public static final Uri CONTENT_URI = buildUri(Path.ORDERS);
    }
}
