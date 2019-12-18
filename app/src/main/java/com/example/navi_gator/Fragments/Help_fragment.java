package com.example.navi_gator.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.R;

public class Help_fragment extends Fragment {

    private Button back_btn;
    private TextView loading;

    private OnFragmentInteractionListener mListener;

    public Help_fragment() {
        // Required empty public constructor
    }

    public static Help_fragment newInstance() {
        return new Help_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);
        back_btn = view.findViewById(R.id.help_back_btn);
        loading = view.findViewById(R.id.loading_explain);
        animateText();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBack();
            }
        });
        return view;
    }

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

    private void setText(final CharSequence text) {
        if (getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setText(text);
            }
        });
    }

    private void animateText() {
        final boolean timeIsTicking = true;

        final Thread animatedText = new Thread() {
            public void run() {
                try {
                    int time = 0;
                    while(timeIsTicking) {
                        sleep(100);
                        String textFull = "Navi-gator";
                        int rest = time % (textFull.length() * 100) + 100;
                        setText(textFull.substring(0, rest != 0 ? (rest / 100) : 0));
                        time += 100;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        animatedText.start();
    }
}
