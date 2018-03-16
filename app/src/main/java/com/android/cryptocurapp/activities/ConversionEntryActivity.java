package com.android.cryptocurapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.cryptocurapp.R;
import com.android.cryptocurapp.utils.TextSingleton;

public class ConversionEntryActivity extends AppCompatActivity {
    private String cryptoText, baseText;
    private TextView baseCurrencyTextView, cryptoCurrencyTextView;
    private Button button;
    private EditText currencyAmountEntered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_entry_layout);
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currencyAmountEntered = findViewById(R.id.base_currency_amount_entered);
        baseCurrencyTextView = findViewById(R.id.base_currency_text);
        cryptoCurrencyTextView = findViewById(R.id.crypto_currency_text);
        button = findViewById(R.id.convert_button);



        cryptoText = TextSingleton.getInstance().getCryptoText();
        baseText = TextSingleton.getInstance().getBaseText();

        baseCurrencyTextView.setText(baseText);
        cryptoCurrencyTextView.setText(cryptoText);



        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currencyAmountEntered.getText().toString().isEmpty()) currencyAmountEntered.setError("Enter an amount");
                        else {
                            Intent intent = new Intent(ConversionEntryActivity.this, ConversionResultActivity.class);
                            intent.putExtra("cryptoText", cryptoText);
                            intent.putExtra("baseText", baseText);
                            intent.putExtra("baseAmount", currencyAmountEntered.getText().toString());
                            startActivity(intent);
                        }

                    }
                }
        );
    }
}
