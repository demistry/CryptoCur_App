package com.android.cryptocurapp.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ILENWABOR DAVID on 12/03/2018.
 */

@Entity()
public class RoomEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cryptocurrency;
    private String basecurrency;
    private String exchangeRate;


    public RoomEntity(String cryptocurrency, String basecurrency, String exchangeRate) {
        this.cryptocurrency = cryptocurrency;
        this.basecurrency = basecurrency;
        this.exchangeRate = exchangeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public String getBasecurrency() {
        return basecurrency;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }
}
