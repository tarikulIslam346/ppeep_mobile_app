package com.example.ppeepfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hitomi.cmlibrary.CircleMenu;


public class FragmentGroup extends Fragment {
private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;

    View v;
    private CircleMenu circleMenu,circleMenu2;
    public FragmentGroup() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.group_fragment,container,false);









        //circle menu start
        final CircleMenu circleMenu = (CircleMenu) v.findViewById(R.id.circleMenuId);




        circleMenu.setMainMenu(Color.parseColor("#FFFFFF"), R.drawable.iconppeep2, R.drawable.iconppeep2);
        circleMenu.addSubMenu(Color.parseColor("#0077B5"), R.drawable.icon_pickaboo)
                .addSubMenu(Color.parseColor("#00acee"), R.drawable.icon_daraz)
                .addSubMenu(Color.parseColor("#C4302B"), R.drawable.icon_baagdoom)
                .addSubMenu(Color.parseColor("#fffc00"), R.drawable.icon_daraz)
                .addSubMenu(Color.parseColor("#54C0d4"), R.drawable.icon_robishop)
                .addSubMenu(Color.parseColor("#00acee"), R.drawable.icon_pickaboo)
                .addSubMenu(Color.parseColor("#C4302B"), R.drawable.icon_baagdoom)
                .addSubMenu(Color.parseColor("#fffc00"), R.drawable.icon_daraz)
                .addSubMenu(Color.parseColor("#54C0d4"), R.drawable.icon_robishop)
                .addSubMenu(Color.parseColor("#C4302B"), R.drawable.icon_baagdoom)


        ;










        return v;
    }


}
