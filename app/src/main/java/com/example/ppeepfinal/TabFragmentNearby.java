package com.example.ppeepfinal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragmentNearby extends Fragment {
    View v;
    private  static final int NUM_LIST_ITEM = 100;
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mNumberOfRestaurant;

    public TabFragmentNearby(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.activity_tab_fragment_nearby,container,false);

        mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        mNumberOfRestaurant.setLayoutManager(layoutManager);

        mNumberOfRestaurant.setHasFixedSize(true);

        tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(NUM_LIST_ITEM);
        mNumberOfRestaurant.setAdapter(tabFragmentNearbyAdapter);

        return v;

    }
}
