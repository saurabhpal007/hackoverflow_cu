package com.example.hackoverflow;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

;

public class geomap extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomap);

        searchView=findViewById(R.id.sv_location);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String location=searchView.getQuery().toString();
                List<Address> addressList=null;

                if(location !=null || !location.equals(""))
                {
                    Geocoder geocoder=new Geocoder(geomap.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address=addressList.get(0);
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    Log.i("latlong",""+latLng);
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if(location.equals("uiet")){
                                Toast.makeText(geomap.this, "Welcome!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(geomap.this, building.class);
                                startActivity(intent);

                                return false;
                            }
                            return false;

//                            Toast.makeText(geomap.this, "Welcome!", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(geomap.this, building.class);
//                            startActivity(intent);
//
//                            return false;
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;
        map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937, 78.9629) , 4.5f) );

    }

}
