package com.android.cryptocurapp.network;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by ILENWABOR DAVID on 20/10/2017.
 */

public class ExchangeLoaderClass extends AsyncTaskLoader<String> {
    private static final String url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC,ETH,SBD&tsyms=USD,GBP,EUR,NGN,CHF,JPY,INR,CAD,AUD,IQD,CNY,ZAR,NZD,RUB,SGD,SEK,KWD,MXN,TRY,BRL";
    public ExchangeLoaderClass(Context context){
        super(context);
    }
    @Override
    public String loadInBackground() {
        return getJsonFromWeb(url);
    }
    private String getJsonFromWeb(String string){
        String jsonString ="";
        InputStream inputStream = null;
        try {
            URL url = new URL(string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            jsonString = getJsonString(inputStream);
            inputStream.close();
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        return jsonString;
    }
    private String getJsonString(InputStream inputStream) throws IOException{
        String json = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,  Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        json = bufferedReader.readLine();
        while (json!=null){
            stringBuilder.append(json);
            json = bufferedReader.readLine();
            }
        return stringBuilder.toString();
    }
}
