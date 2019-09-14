package com.example.ppeepfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

public class ShopCategoryFragment extends Fragment {

    MaterialCardView menCategory;
    View v;

    public ShopCategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_shop_category_fragment, container, false);

        menCategory=(MaterialCardView) v.findViewById(R.id.menShopCategory);

        menCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mencategory = new Intent(getContext(), ShopFragmentSubCategory.class);
                startActivity(mencategory);
            }

    });
        return v;
    }
}
