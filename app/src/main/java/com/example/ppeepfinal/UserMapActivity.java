package com.example.ppeepfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dingi.dingisdk.Dingi;
import com.dingi.dingisdk.camera.CameraUpdate;
import com.dingi.dingisdk.camera.CameraUpdateFactory;
import com.dingi.dingisdk.constants.Style;
import com.dingi.dingisdk.geometry.LatLng;
import com.dingi.dingisdk.maps.DingiMap;
import com.dingi.dingisdk.maps.MapView;
import com.dingi.dingisdk.maps.OnMapReadyCallback;
import com.example.ppeepfinal.utilities.Api;
import com.example.ppeepfinal.utilities.MyLocation;
import com.example.ppeepfinal.utilities.VolleyRequest;
import com.google.android.material.button.MaterialButton;


import org.json.JSONObject;

import static com.dingi.dingisdk.constants.Style.DINGI_ENGLISH;

public class UserMapActivity extends AppCompatActivity implements OnMapReadyCallback {



    private static final String TAG = "BasicMapActivity";
    private MapView mMap;
    private DingiMap map;
    Button addressConfirm;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dingi.getInstance(this, "EjFUMTUMKFcnJ2VzRnL39Cd2ixtHScJ2p0C1vhP2");
        setContentView(R.layout.activity_user_map);

        addressConfirm = (Button) findViewById(R.id.bt_delivery_address_confirm);


       // Toolbar toolbar = findViewById(R.id.toolbar);

       /* toolbar.setTitle("Dingi Map Reverse Geocoding");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        mMap = findViewById(R.id.dingi_map);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);



    }


    @Override
    public void onMapReady(@NonNull DingiMap dingiMap) {
        dingiMap.setStyleUrl(Style.DINGI_ENGLISH);
        map = dingiMap;
        MyLocation myLocation = new MyLocation(UserMapActivity.this);
        myLocation.setListener(new MyLocation.MyLocationListener() {
            @Override
            public void onLocationFound(Location location) {

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18);
                map.animateCamera(cameraUpdate);
                map.addOnCameraMoveListener(new DingiMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        LatLng center = map.getCameraPosition().target;
                        callAPI(center);

                    }
                });


            }

            @Override
            public void onFailed() {

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onStart() {
        super.onStart();
        mMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    private void callAPI(LatLng latLng) {
        VolleyRequest volleyRequest = new VolleyRequest(UserMapActivity.this);
        volleyRequest.VolleyGet(Api.reverseGeo + "demo?lat=" + latLng.getLatitude() + "&lng=" + latLng.getLongitude() + "&address_level=UPTO_THANA");
        volleyRequest.setListener(new VolleyRequest.MyServerListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    ((EditText) findViewById(R.id.address)).setText(response.getJSONObject("result").getString("address"));
                    address = ((EditText) findViewById(R.id.address)).getText().toString();
                    addressConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent foodCart = new Intent(UserMapActivity.this, FoodCartPage.class);
                            if(address != null)foodCart.putExtra("address", address);
                            startActivity(foodCart);
                        }
                    });

                } catch (Exception e) {

                }


            }

            @Override
            public void onError(String error) {
                Toast.makeText(UserMapActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void responseCode(int resposeCode) {

            }
        });
    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
