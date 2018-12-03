package com.safepath.safepath;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import HTTPS.FirestoreRequest;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private Intent mServiceIntent;

    private Button buttonReport;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.mapView);
        ArcGISMap map = new ArcGISMap(Constants.arcGISMap);
        mMapView.setMap(map);

        checkLocationPermission();

        buttonReport = findViewById(R.id.button_report);

        buttonReport.setOnClickListener(v -> {
            if(Math.abs(Constants.lat + Constants.lon) > 0){
                send();
            }
        });
    }

    private void send(){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss", Locale.US);

        String timeString = fmt.format(date);

        FirestoreRequest.sendHazard(this,new Hazard(Constants.lat,Constants.lon,timeString));
    }

    public void startGPS(){

        mServiceIntent = new Intent(this, GPSservice.class);
        startService(mServiceIntent);
    }


    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return false;

        } else {

            //Request location updates:
            startGPS();

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

                        toastMessage("You are all set!");

                        //Request location updates:
                        startGPS();
                    }

                } else {

                    toastMessage("Please activate it next time!");

                }
            }

        }
    }

    // Show message to user through a short pop up
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }
}
