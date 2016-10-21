package com.dada.realestatemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.realestatemanager.R;
import com.dada.realestatemanager.adapter.MapAdapter;
import com.dada.realestatemanager.util.PropertyItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


       // listView.(ListView)findViewById(R.id.list);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final MapAdapter arrayAdapter = new MapAdapter(MapsActivity.this,BuyerMainActivity.propertyList);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                PropertyItem selected=BuyerMainActivity.propertyList.get(0);
                for(int i=0;i<BuyerMainActivity.propertyList.size();i++){
                    if(marker.getTitle().equals(BuyerMainActivity.propertyList.get(i).name)){
                        selected=BuyerMainActivity.propertyList.get(i);
                    }
                }


                DialogPlus dialog = DialogPlus.newDialog(MapsActivity.this)
                        .setAdapter(arrayAdapter)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position)
                            {
                               if(position!=-1){
                                   Intent intent=new Intent(MapsActivity.this, DetailActivity.class);
                                   intent.putExtra("pos",position);
                                   startActivity(intent);
                               }
                                //startActivity(new Intent(MapsActivity.this, DetailActivity.class));
                            }
                        })
                        .setHeader(R.layout.list_header)
                        .setExpanded(true,450)// This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                TextView selectedName=(TextView) findViewById(R.id.current_name_value);
                selectedName.setText(selected.name);
                TextView selectedType=(TextView) findViewById(R.id.current_type_value);
                selectedType.setText(selected.type);
                TextView selectedCost=(TextView) findViewById(R.id.current_cost_value);
                selectedCost.setText(selected.cost);
                TextView selectedSize=(TextView) findViewById(R.id.current_size_value);
                selectedSize.setText(selected.size);
                return true;
            }
        });

        // Add a marker in Sydney and move the camera
        int markerSize=BuyerMainActivity.propertyList.size();
       // LatLng focus=new LatLng(-35,80);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if(markerSize!=0){

            for(int i=0;i<markerSize;i++){
                PropertyItem temp=BuyerMainActivity.propertyList.get(i);
                LatLng tempLoc = new LatLng(Double.parseDouble(temp.latitude ), Double.parseDouble( temp.longitude));
                Marker marker = mMap.addMarker(new MarkerOptions().position(tempLoc).title(temp.name));
                //focus=tempLoc;
                builder.include(marker.getPosition());
            }

        }
        LatLngBounds bounds = builder.build();
       // Toast.makeText(this, bounds.getCenter().toString(),Toast.LENGTH_LONG).show();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng());
        int padding = 150;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);
        mMap.moveCamera(cu);
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
    }
}
