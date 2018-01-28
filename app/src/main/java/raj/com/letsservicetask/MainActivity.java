package raj.com.letsservicetask;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    MapView mapView;
    LocationManager locationManager;
    String provider;
    DbHelper dbHelper;
    ArrayList<LogObject> gArrayList;
    MyAdapter myAdapter;
    LinearLayoutManager llm;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rc);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

//        mapView=(MapView)findViewById(R.id.mapView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dbHelper = new DbHelper(this);
        gArrayList = new ArrayList<>();
        initMap();


        gArrayList.addAll(dbHelper.getAllDetails());
        Collections.reverse(gArrayList);
        myAdapter = new MyAdapter(MainActivity.this, gArrayList);
        recyclerView.setAdapter(myAdapter);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        myAdapter.notifyDataSetChanged();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gArrayList.addAll(dbHelper.getAllDetails());
                myAdapter.notifyDataSetChanged();
            }
        });

        provider = locationManager.getBestProvider(new Criteria(), false);

        if (checkLocationPermission()) {
            Intent intent = new Intent(MainActivity.this, MyLocationService.class);
            startService(intent);

        }
    }

    public void addDetails(LogObject object) {
        dbHelper.addDetail(object);
        gArrayList.addAll(dbHelper.getAllDetails());
        myAdapter = new MyAdapter(MainActivity.this, gArrayList);
        recyclerView.setAdapter(myAdapter);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Verify for location service")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(MainActivity.this, MyLocationService.class);
                        startService(intent);

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) MainActivity.this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Polyline line;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(gArrayList.get(0).getLat()), Double.parseDouble(gArrayList.get(0).getLng()));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Position"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < gArrayList.size(); z++) {
            LatLng point = new LatLng(Double.parseDouble(gArrayList.get(z).getLat()), Double.parseDouble(gArrayList.get(z).getLng()));
            options.add(point);
        }

        line = mMap.addPolyline(options);
        line.setJointType(R.string.default_set);
    }
}
