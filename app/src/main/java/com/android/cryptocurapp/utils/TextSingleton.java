package com.android.cryptocurapp.utils;

/**
 * Created by ILENWABOR DAVID on 16/03/2018.
 */

public class TextSingleton {
    private static final TextSingleton ourInstance = new TextSingleton();
    private String cryptoText, baseText;

    public static TextSingleton getInstance() {
        return ourInstance;
    }

    private TextSingleton() {
    }

    public String getCryptoText() {
        return cryptoText;
    }

    public void setCryptoText(String cryptoText) {
        this.cryptoText = cryptoText;
    }

    public String getBaseText() {
        return baseText;
    }

    public void setBaseText(String baseText) {
        this.baseText = baseText;
    }
}
