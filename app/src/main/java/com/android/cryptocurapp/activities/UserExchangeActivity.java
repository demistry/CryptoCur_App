package com.android.cryptocurapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.android.cryptocurapp.storage.CryptoCurDatabase;
import com.android.cryptocurapp.utils.ExchangeRateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
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
        updateList();
        //Log.v("LOG", btcExchangeArray.toString());
        //Log.v("LOG", ethExchangeArray.toString());

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
        CryptoCurDatabase cryptDatabase = new CryptoCurDatabase(this);
        SQLiteDatabase db = cryptDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CryptoCurContract.CRYPTOCURRENCY_COLUMN, crypto);
        contentValues.put(CryptoCurContract.BASECURRENCY_COLUMN, base);
        contentValues.put(CryptoCurContract.EXCHANGERATE_COLUMN, showExchangeRate(crypto,base));
        db.insert(CryptoCurContract.CRYPTOCUR_TABLE, null, contentValues);
        db.close();
        updateList();
    }
    private ArrayList<String> retrieveTextFromDatabase(int currencyType){
        ArrayList<String> currency = new ArrayList<>();
        String [] cryptoColumn = {CryptoCurContract.CRYPTOCURRENCY_COLUMN};
        String [] baseColumn = {CryptoCurContract.BASECURRENCY_COLUMN};
        String [] exchangeRateColumn = {CryptoCurContract.EXCHANGERATE_COLUMN};
        CryptoCurDatabase cryptodatabase = new CryptoCurDatabase(this);
        SQLiteDatabase db = cryptodatabase.getReadableDatabase();;
        switch (currencyType){
            case 0://retrieve CryptoCurrency
                Cursor cursorCrypt = db.query(CryptoCurContract.CRYPTOCUR_TABLE,cryptoColumn,
                        null,
                        null,
                        null,
                        null,
                        null);
                while(cursorCrypt.moveToNext()){
                    currency.add(cursorCrypt.getString(0));
                }
                cursorCrypt.close();
                db.close();
                return currency;
            case 1://retrieve BaseCurrency
                Cursor cursorBase = db.query(CryptoCurContract.CRYPTOCUR_TABLE,baseColumn,
                        null,
                        null,
                        null,
                        null,
                        null);
                while(cursorBase.moveToNext()){
                    currency.add(cursorBase.getString(0));
                }
                cursorBase.close();
                db.close();
                return currency;
            case 2://retrieve ExchangeRateAmount
                Cursor cursorExchange = db.query(CryptoCurContract.CRYPTOCUR_TABLE,exchangeRateColumn,
                        null,
                        null,
                        null,
                        null,
                        null);
                while(cursorExchange.moveToNext()){
                    currency.add(cursorExchange.getString(0));
                }
                cursorExchange.close();
                db.close();
                return currency;
        }
        return null;
    }

    private void emptyDatabase(){
        CryptoCurDatabase cryptoCurDatabase = new CryptoCurDatabase(this);
        SQLiteDatabase db = cryptoCurDatabase.getWritableDatabase();
        db.delete(CryptoCurContract.CRYPTOCUR_TABLE, null,null);
        db.close();
        updateList();
        Toast.makeText(this, "Database Emptied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deletedConversioncard(int position, String baseTextString) {
        CryptoCurDatabase cryptoCurDatabase = new CryptoCurDatabase(this);
        SQLiteDatabase db = cryptoCurDatabase.getWritableDatabase();
        db.delete(CryptoCurContract.CRYPTOCUR_TABLE, CryptoCurContract.EXCHANGERATE_COLUMN +" =?", new String[]{baseTextString});
        db.close();
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

                    for (int i = 0; i<btcExchangeArray.size(); i ++){
                        map.putAll(btcExchangeArray.get(i));

                    }
                    return map.get(baseText);
                }
                else if (cryptoText.equals("ETH")){
                    for (int i = 0; i<ethExchangeArray.size(); i ++){
                        map.putAll(ethExchangeArray.get(i));
                    }
                    return map.get(baseText);
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
