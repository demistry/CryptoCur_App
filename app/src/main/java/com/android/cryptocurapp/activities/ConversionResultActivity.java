package com.android.cryptocurapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cryptocurapp.R;
import com.android.cryptocurapp.network.ExchangeLoaderClass;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static com.android.cryptocurapp.models.CryptoCurContract.LOADER_ID;
import static com.android.cryptocurapp.network.JSONHelper.parseRawJsonString;

public class ConversionResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private Button homeButton;
    private TextView convertedCryptoText, convertedCryptoAmount, convertedBaseCurrency, convertedBaseAmount;
    private double currencyAmountEntered;
    ArrayList<Map<String, Double>> btcAgainstBase;
    ArrayList<Map<String, Double>> ethAgainstBase;
    ArrayList<Map<String, Double>> sbdAgainstBase;
    private Bundle bundle;
    private static double amount;
    private static Map<String, Double> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeButton = (Button) findViewById(R.id.home_button);
        convertedCryptoText = (TextView) findViewById(R.id.converted_cryptocurrency_text);
        convertedBaseAmount = (TextView) findViewById(R.id.converted_base_amount);
        convertedBaseCurrency = (TextView) findViewById(R.id.converted_base_currency);
        convertedCryptoAmount = (TextView) findViewById(R.id.converted_cryptocurrency_amount);

        bundle = getIntent().getExtras();
        currencyAmountEntered = Double.valueOf(bundle.getString("baseAmount"));
        convertedCryptoText.setText(bundle.getString("cryptoText"));


        convertedBaseCurrency.setText(bundle.getString("baseText"));
        convertedBaseAmount.setText(bundle.getString("baseAmount"));

        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ConversionResultActivity.this, UserExchangeActivity.class));
                        finish();
                    }
                }
        );
        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public ExchangeLoaderClass onCreateLoader(int id, Bundle args) {
        return new ExchangeLoaderClass(ConversionResultActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, String data) {

        btcAgainstBase = parseRawJsonString(data, 0);
        ethAgainstBase = parseRawJsonString(data, 1);
        sbdAgainstBase = parseRawJsonString(data, 2);

        convertedCryptoAmount.setText(convertAmount(bundle.getString("cryptoText"), bundle.getString("baseText"), currencyAmountEntered));

    }

    public String convertAmount(String cryptoText, String baseText, double currencyAmountEntered){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            if (cryptoText.equals("BTC")){

                for (int i = 0; i<btcAgainstBase.size(); i ++){
                    map.putAll(btcAgainstBase.get(i));
                }
                amount = map.get(baseText);


                return String.format(Locale.ENGLISH,"%.6f",currencyAmountEntered/amount);
            }
            else if (cryptoText.equals("ETH")){
                for (int i = 0; i<ethAgainstBase.size(); i ++){
                    map.putAll(ethAgainstBase.get(i));

                }
                amount = map.get(baseText);


                return String.format(Locale.ENGLISH,"%.6f",currencyAmountEntered/amount);
            }
            else if (cryptoText.equals("SBD")){
                for (int i = 0; i<sbdAgainstBase.size(); i++){
                    map.putAll(sbdAgainstBase.get(i));
                }
                amount = map.get(baseText);
                return String.format(Locale.ENGLISH, "%.6f", currencyAmountEntered/amount);
            }
        }
        else{
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
            return "";
        }

        return "";
    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader.reset();
    }


}
