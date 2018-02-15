package com.android.cryptocurapp.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ILENWABOR DAVID on 22/10/2017.
 */

public class JSONHelper {
    private static String[] currencies = { "USD","GBP","EUR","NGN","CHF","JPY","INR","CAD","AUD","IQD","CNY","ZAR","NZD","RUB","SGD","SEK","KWD","MXN","TRY","BRL"};
    public static ArrayList<Map<String, Double>> parseRawJsonString(String data, int cryptoType){

        ArrayList<Map<String, Double>> baseCurrencyBTC = new ArrayList<>();
        ArrayList<Map<String, Double>> baseCurrencyETH = new ArrayList<>();
        ArrayList<Map<String, Double>> baseCurrencySBD = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject object = jsonObject.getJSONObject("RAW");
            JSONObject objectBTC = object.getJSONObject("BTC");
            JSONObject objectETH = object.getJSONObject("ETH");
            JSONObject objectSBD = object.getJSONObject("SBD");


            for (int i = 0; i<currencies.length; i ++){
                Map<String, Double> hashMapBTC = new HashMap<>();
                Map<String, Double> hashMapETH = new HashMap<>();
                Map<String, Double> hashMapSBD = new HashMap<>();


                JSONObject obj = objectBTC.getJSONObject(currencies[i]);
                JSONObject obj2 = objectETH.getJSONObject(currencies[i]);
                JSONObject obj3 = objectSBD.getJSONObject(currencies[i]);


                hashMapBTC.put(currencies[i],obj.getDouble("PRICE"));
                hashMapETH.put(currencies[i], obj2.getDouble("PRICE"));
                hashMapSBD.put(currencies[i], obj3.getDouble("PRICE"));


                baseCurrencyBTC.add(hashMapBTC);
                baseCurrencyETH.add(hashMapETH);
                baseCurrencySBD.add(hashMapSBD);

                Log.v("LOG","BTC price added for "+ hashMapBTC.toString());
                Log.v("LOG","ETH price added for "+ hashMapETH.toString());
            }
            if (cryptoType == 0)
                return baseCurrencyBTC;
            if (cryptoType == 1)
                return baseCurrencyETH;
            if (cryptoType == 2)
                return baseCurrencySBD;
        }
        catch (JSONException json){
            json.printStackTrace();
            return null;
        }
        return null;
    }


    public static ArrayList<Map<String, String>> parseDisplayJsonString(String data, int cryptoType){

        ArrayList<Map<String, String>> baseCurrencyBTC = new ArrayList<>();
        ArrayList<Map<String, String>> baseCurrencyETH = new ArrayList<>();
        ArrayList<Map<String, String>> baseCurrencySBD = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject object = jsonObject.getJSONObject("DISPLAY");
            JSONObject objectBTC = object.getJSONObject("BTC");
            JSONObject objectETH = object.getJSONObject("ETH");
            JSONObject objectSBD = object.getJSONObject("SBD");


            for (int i = 0; i<currencies.length; i ++){
                Map<String, String> hashMapBTC = new HashMap<>();
                Map<String, String> hashMapETH = new HashMap<>();
                Map<String, String> hashMapSBD = new HashMap<>();


                JSONObject obj = objectBTC.getJSONObject(currencies[i]);
                JSONObject obj2 = objectETH.getJSONObject(currencies[i]);
                JSONObject obj3 = objectSBD.getJSONObject(currencies[i]);


                hashMapBTC.put(currencies[i],obj.getString("PRICE"));
                hashMapETH.put(currencies[i], obj2.getString("PRICE"));
                hashMapSBD.put(currencies[i], obj3.getString("PRICE"));


                baseCurrencyBTC.add(hashMapBTC);
                baseCurrencyETH.add(hashMapETH);
                baseCurrencySBD.add(hashMapSBD);

                Log.v("LOG","BTC price added for "+ hashMapBTC.toString());
                Log.v("LOG","ETH price added for "+ hashMapETH.toString());
            }
            if (cryptoType == 0)
                return baseCurrencyBTC;
            if (cryptoType == 1)
                return baseCurrencyETH;
            if (cryptoType == 2)
                return baseCurrencySBD;
        }
        catch (JSONException json){
            json.printStackTrace();
            return null;
        }
        return null;
    }

}
