package com.android.cryptocurapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import com.android.cryptocurapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversionDialogFragment extends DialogFragment {

    private onCreateButtonClicked buttonClickedReference;
    private Button button;
    private Spinner cryptoSpinner, baseSpinner;
    public interface onCreateButtonClicked{
         void createNewExchange(String cryptoString, String baseString);
    }

    public ConversionDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.new_rate_dialog_selector, null);
        dialog.setView(view);
        button = (Button) view.findViewById(R.id.create_conversion_rate);
        cryptoSpinner = (Spinner) view.findViewById(R.id.cryptocurrency_spinner);
        baseSpinner = (Spinner) view.findViewById(R.id.base_currency_spinner);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createButtonClicked(cryptoSpinner, baseSpinner);
                        getDialog().dismiss();
                    }
                }
        );
        return dialog.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_rate_dialog_selector, container, false);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Select currency");
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.buttonClickedReference = (onCreateButtonClicked) activity;
    }
    private void createButtonClicked(Spinner crypto, Spinner base){
        buttonClickedReference.createNewExchange(crypto.getSelectedItem().toString(),base.getSelectedItem().toString());
    }

}
