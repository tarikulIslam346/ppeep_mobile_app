package com.example.ppeepfinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;

import java.util.List;


public class FragmentMyProfile extends Fragment {

    View v;

    TextView myProfileName,myPhoneNo;

    private UserDatabase mdb;

    public FragmentMyProfile() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      v=inflater.inflate(R.layout.myprofile_fragment,container,false);

        mdb = UserDatabase.getInstance(getContext());//intantiate room database

        myProfileName = v.findViewById(R.id.profile_name);

        myPhoneNo = v.findViewById(R.id.phone_no);

        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!= 0){//if data exist

           myPhoneNo.setText(user.get(0).getPhone());// set phone to text view

           myProfileName.setText(user.get(0).getName());// set name to text view
        }

      return v;

    }
}
