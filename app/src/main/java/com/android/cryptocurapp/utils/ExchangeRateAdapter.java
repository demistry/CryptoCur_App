package com.android.cryptocurapp.utils;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cryptocurapp.R;
import com.android.cryptocurapp.storage.RoomEntity;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ILENWABOR DAVID on 18/10/2017.
 */

public class ExchangeRateAdapter extends RecyclerView.Adapter<ExchangeRateAdapter.ExchangeViewHolder> {
    private ArrayList<String> crypto, base;
    private cardClickedInterface interfaceReference;
    private ArrayList<String> exchangeAmount;

    //private List<RoomEntity> entities;

    public ExchangeRateAdapter(ArrayList<String> crypto, ArrayList<String> base,
                               cardClickedInterface interfaceReference, ArrayList<String> exchangeAmount){
        this.crypto = crypto;
        this.base = base;
        this.interfaceReference = interfaceReference;
        this.exchangeAmount = exchangeAmount;
    }
//    public ExchangeRateAdapter(List<RoomEntity> entities){
//        this.entities = entities;
//    }
//    public void updateExchangeRateItems(ArrayList<String> exchangeRateAmount){
//        final RateDiffCallback diffCallback = new RateDiffCallback(this.exchangeAmount, exchangeRateAmount);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//        this.exchangeAmount.clear();
//        this.exchangeAmount.addAll(exchangeRateAmount);
//        diffResult.dispatchUpdatesTo(this);
//    }
    public interface cardClickedInterface{
        void conversionCardClicked(String cryptoText, String baseText, int position);
        void deletedConversioncard(int position, String baseText);
    }
    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final ExchangeViewHolder viewHolder = new ExchangeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.conversion_cardview_layout,parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExchangeViewHolder holder, final int position) {
        holder.cryptoText.setText(crypto.get(position));
        holder.baseText.setText(base.get(position));
        holder.exchangeAmountTextView.setText(exchangeAmount.get(position));
        holder.deleteImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfaceReference.deletedConversioncard(holder.getAdapterPosition(), exchangeAmount.get(position));
                    }
                }
        );
        holder.cardview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfaceReference.conversionCardClicked(crypto.get(position), base.get(position), position);
                    }
                }
        );


    }

    @Override
    public int getItemCount() {

        return base.size();
    }
    public class ExchangeViewHolder extends RecyclerView.ViewHolder{
        public TextView cryptoText, baseText, exchangeAmountTextView;
        public ImageView deleteImageView;
        public FrameLayout cardview;
        public ExchangeViewHolder(View view){
            super(view);
            cryptoText = (TextView) view.findViewById(R.id.exchange_crypto_currency);
            baseText = (TextView) view.findViewById(R.id.exchange_base_currency);
            deleteImageView = (ImageView) view.findViewById(R.id.delete_icon);
            cardview =  view.findViewById(R.id.exchange_rate_cardview);
            exchangeAmountTextView = (TextView) view.findViewById(R.id.current_exchange_rate_text);
        }
    }
}
