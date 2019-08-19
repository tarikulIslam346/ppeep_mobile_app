package com.example.ppeepfinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class TabFragmentFoodCuisineSearch  extends Fragment {

    private static final String TAG = "Cuisine Search";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater
                .inflate(R.layout.tab_fragment_food_cuisine_search, container, false);



        return view;
    }

}

