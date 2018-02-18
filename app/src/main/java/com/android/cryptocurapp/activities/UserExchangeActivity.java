package com.android.cryptocurapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.cryptocurapp.R;
import com.android.cryptocurapp.fragments.ConversionDialogFragment;
import com.android.cryptocurapp.models.CryptoCurContract;
import com.android.cryptocurapp.network.ExchangeLoaderClass;
import com.android.cryptocurapp.network.JSONHelper;
import com.android.cryptocurapp.utils.ExchangeRateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserExchangeActivity extends AppCompatActivity implements ConversionDialogFragment.onCreateButtonClicked,
                                            ExchangeRateAdapter.cardClickedInterface, LoaderManager.LoaderCallbacks<String> {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ExchangeRateAdapter exchangeRateAdapter;
    private static ArrayList<String> cryptoText;
    private static ArrayList<String> baseText;
    private static ArrayList<String> exchangeRateTextArray;
    private ArrayList<Map<String, String>> btcExchangeArray;
    private ArrayList<Map<String, String>> ethExchangeArray;
    private ArrayList<Map<String, String>> sbdExchangeArray;
    private ArrayList<Map<String, String>> ltcExchangeArray;
    private ArrayList<Map<String, String>> xrpExchangeArray;
    private ArrayList<Map<String, String>> bchExchangeArray;
    private String crypto, base;
    private static Map<String, String> map = new HashMap<>();


    @Override
    public void conversionCardClicked(String cryptoText, String baseText, int position) {
        Intent intent = new Intent(UserExchangeActivity.this, ConversionEntryActivity.class);
        intent.putExtra("crypto",cryptoText);
        intent.putExtra("base", baseText);
        startActivity(intent);
    }

    @Override
    public ExchangeLoaderClass onCreateLoader(int id, Bundle args) {
        return new ExchangeLoaderClass(UserExchangeActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, String data) {
        btcExchangeArray = JSONHelper.parseDisplayJsonString(data,0);
        ethExchangeArray = JSONHelper.parseDisplayJsonString(data, 1);
        sbdExchangeArray = JSONHelper.parseDisplayJsonString(data, 2);
        ltcExchangeArray = JSONHelper.parseDisplayJsonString(data, 3);
        xrpExchangeArray = JSONHelper.parseDisplayJsonString(data, 4);
        bchExchangeArray = JSONHelper.parseDisplayJsonString(data, 5);
        updateList();

    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader.reset();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_layout);
        getLoaderManager().initLoader(CryptoCurContract.LOADER_ID, null, this).forceLoad();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_exchange_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateList();
        fab = (FloatingActionButton) findViewById(R.id.new_exchange_fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewExchangeItem();
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_icon){
            //delete all exchange rates
            emptyDatabase();
        }
        return super.onOptionsItemSelected(item);
    }
    private void addNewExchangeItem(){
        //add new list item to database
        ConversionDialogFragment conversionDialogFragment = new ConversionDialogFragment();
        conversionDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void createNewExchange(String crypto, String base) {//implemented fragment interface
        //send clicked item to database for save
        saveTextToDatabase(crypto, base);
    }
    private void saveTextToDatabase(String crypto, String base){
        this.crypto = crypto;
        this.base = base;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CryptoCurContract.CRYPTOCURRENCY_COLUMN, crypto);
        contentValues.put(CryptoCurContract.BASECURRENCY_COLUMN, base);
        contentValues.put(CryptoCurContract.EXCHANGERATE_COLUMN, showExchangeRate(crypto,base));
        getContentResolver().insert(CryptoCurContract.CONTENT_BASE_URI, contentValues);
        updateList();
    }
    private ArrayList<String> retrieveTextFromDatabase(int currencyType){
        ArrayList<String> currency = new ArrayList<>();
        String [] cryptoColumn = {CryptoCurContract.CRYPTOCURRENCY_COLUMN};
        String [] baseColumn = {CryptoCurContract.BASECURRENCY_COLUMN};
        String [] exchangeRateColumn = {CryptoCurContract.EXCHANGERATE_COLUMN};
        switch (currencyType){
            case 0://retrieve CryptoCurrency
                Cursor cursorCrypt = getContentResolver().query(CryptoCurContract.CONTENT_BASE_URI,cryptoColumn,null,null,null);
                if(cursorCrypt!=null){
                    while(cursorCrypt.moveToNext()){
                        currency.add(cursorCrypt.getString(0));
                    }
                    cursorCrypt.close();
                }

                return currency;
            case 1://retrieve BaseCurrency
                Cursor cursorBase = getContentResolver().query(CryptoCurContract.CONTENT_BASE_URI,baseColumn,null,null,null);
                if(cursorBase!=null){
                    while(cursorBase.moveToNext()){
                        currency.add(cursorBase.getString(0));
                    }
                    cursorBase.close();
                }
                return currency;
            case 2://retrieve ExchangeRateAmount
                Cursor cursorExchange = getContentResolver().query(CryptoCurContract.CONTENT_BASE_URI,exchangeRateColumn,null,null,null);
                if(cursorExchange!=null){
                    while(cursorExchange.moveToNext()){
                        currency.add(cursorExchange.getString(0));
                    }
                    cursorExchange.close();
                }
                return currency;
        }
        return null;
    }

    private void emptyDatabase(){
        getContentResolver().delete(CryptoCurContract.CONTENT_BASE_URI, null, null);
        updateList();
        Toast.makeText(this, "Database Emptied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deletedConversioncard(int position, String baseTextString) {
        getContentResolver().delete(Uri.withAppendedPath(CryptoCurContract.CONTENT_BASE_URI,String.valueOf(position)),CryptoCurContract.EXCHANGERATE_COLUMN +" =?",
                new String[]{baseTextString});
        updateList();
        Toast.makeText(this, "Exchange Rate Card Deleted", Toast.LENGTH_SHORT).show();
    }
    private void updateList(){
        cryptoText = retrieveTextFromDatabase(0);
        baseText = retrieveTextFromDatabase(1);
        exchangeRateTextArray = retrieveTextFromDatabase(2);

        exchangeRateAdapter = new ExchangeRateAdapter(cryptoText, baseText,this, exchangeRateTextArray);
        recyclerView.setAdapter(exchangeRateAdapter);
        exchangeRateAdapter.notifyDataSetChanged();
    }

    public String showExchangeRate(String cryptoText, String baseText){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            if (cryptoText != null){
                if (cryptoText.equals("BTC")){
                    if (btcExchangeArray!=null){
                        for (int i = 0; i<btcExchangeArray.size(); i ++){
                            map.putAll(btcExchangeArray.get(i));

                        }
                        return map.get(baseText);
                    }
                }
                else if (cryptoText.equals("ETH")){
                    if (ethExchangeArray!=null){
                        for (int i = 0; i<ethExchangeArray.size(); i ++){
                            map.putAll(ethExchangeArray.get(i));
                        }
                        return map.get(baseText);
                    }
                }
                else if (cryptoText.equals("SBD")){
                    if (sbdExchangeArray!=null){
                        for (int i = 0; i<sbdExchangeArray.size(); i++){
                            map.putAll(sbdExchangeArray.get(i));
                        }
                        return map.get(baseText);
                    }
                }
                //
                else if (cryptoText.equals("LTC")){
                    if (ltcExchangeArray!=null){
                        for (int i = 0; i<ltcExchangeArray.size(); i++){
                            map.putAll(ltcExchangeArray.get(i));
                        }
                        return map.get(baseText);
                    }

                }
                else if (cryptoText.equals("XRP")){
                    if (xrpExchangeArray!=null){
                        for (int i = 0; i<xrpExchangeArray.size(); i++){
                            map.putAll(xrpExchangeArray.get(i));
                        }
                        return map.get(baseText);
                    }

                }
                else if (cryptoText.equals("BCH")){
                    if (bchExchangeArray!=null){
                        for (int i = 0; i<bchExchangeArray.size(); i++){
                            map.putAll(bchExchangeArray.get(i));
                        }
                        return map.get(baseText);
                    }

                }
            }
            else {

                return "";
            }
        }
        else {
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
            return "";
        }

        return "";
    }


}
