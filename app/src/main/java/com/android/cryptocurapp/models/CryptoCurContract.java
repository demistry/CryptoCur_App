package com.android.cryptocurapp.models;


import android.net.Uri;

/**
 * Created by ILENWABOR DAVID on 20/10/2017.
 */

public final class CryptoCurContract {
    public static final String CRYPTOCUR_TABLE = "CryptoCurApp";
    public static final String CRYPTOCUR_ID = "_id";
    public static final String CRYPTOCURRENCY_COLUMN = "CryptoCurrency";
    public static final String BASECURRENCY_COLUMN = "BaseCurrency";
    public static final String EXCHANGERATE_COLUMN = "ExchangeRate";
    public static final int LOADER_ID = 1;
    public static final String CRYTOCUR_AUTHORITY = "com.android.cryptocurapp";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://com.android.cryptocurapp/CryptoCurApp");
    private CryptoCurContract(){

    }
}
