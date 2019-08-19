package com.example.ppeepfinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TabFragmentFoodHomeRoot extends Fragment {

    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.food_home_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */
        transaction.replace(R.id.root_frame, new TabFragmentFoodHome());

        transaction.commit();

        return view;
    }

}