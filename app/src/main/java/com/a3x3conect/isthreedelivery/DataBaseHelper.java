package com.a3x3conect.isthreedelivery;

import android.provider.BaseColumns;

public final class DataBaseHelper {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataBaseHelper() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}