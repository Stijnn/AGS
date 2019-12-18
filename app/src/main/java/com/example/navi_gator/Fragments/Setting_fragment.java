package com.example.navi_gator.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navi_gator.Activity.MapsActivity;
import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.R;

import java.util.Locale;


public class Setting_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Spinner spinner;
    Button back_btn;
    Locale myLocale;

    public Setting_fragment() {
        // Required empty public constructor
    }

    public static Setting_fragment newInstance() {
        return new Setting_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        this.spinner = view.findViewById(R.id.language_spinner);
        this.back_btn = view.findViewById(R.id.setting_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBack();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.language_array,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 2) {
                    Toast.makeText(parent.getContext(),
                            "Sie haben Deutsch gewählt", Toast.LENGTH_LONG)
                            .show();
                    setLocale("de");
                } else if (pos == 3) {
                    Toast.makeText(parent.getContext(),
                            "Je hebt Nederlands geselecteerd", Toast.LENGTH_LONG)
                            .show();
                    setLocale("nl");
                } else if (pos == 1) {
                    Toast.makeText(parent.getContext(),
                            "You have selected English", Toast.LENGTH_LONG)
                            .show();
                    setLocale("en");
                }
                spinner.setOnItemSelectedListener(this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MapsActivity.class);
        startActivity(refresh);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
