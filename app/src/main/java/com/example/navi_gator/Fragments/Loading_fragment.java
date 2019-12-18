package com.example.navi_gator.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.R;

public class Loading_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView animatedText;

    public Loading_fragment() {
        // Required empty public constructor
    }

    public static Loading_fragment newInstance() {
        return new Loading_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_fragment, container, false);
        this.animatedText = view.findViewById(R.id.animatedText);
        animateText();
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
                animatedText.setText(text);
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
