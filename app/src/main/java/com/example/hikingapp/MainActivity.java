package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView latitude, longitude, accuracy, altitude, address;
    LocationManager locationManager;
    LocationListener locationListener;
    DecimalFormat df;
    String addressoflocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = findViewById(R.id.latitide);
        longitude = findViewById(R.id.longitude);
        accuracy = findViewById(R.id.accuracy);
        altitude = findViewById(R.id.altitude);
        address = findViewById(R.id.address);
        df = new DecimalFormat("#.####");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.getLatitude() + " " + location.getLongitude()
                        + " " + location.getAccuracy() + " " + location.getAltitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                addressoflocation = "";
                try
                {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(listAddresses!=null && listAddresses.size()>0){

                        if(listAddresses.get(0).getSubThoroughfare()!=null)
                            addressoflocation = listAddresses.get(0).getSubThoroughfare();
                        if(listAddresses.get(0).getThoroughfare()!=null)
                            addressoflocation = addressoflocation + " " + listAddresses.get(0).getThoroughfare();
                        if(listAddresses.get(0).getLocality()!=null)
                            addressoflocation = addressoflocation + " " + listAddresses.get(0).getLocality();
                        if(listAddresses.get(0).getPostalCode()!= null)
                            addressoflocation = addressoflocation + " " + listAddresses.get(0).getPostalCode();
                        if(listAddresses.get(0).getCountryName()!= null)
                            addressoflocation = addressoflocation + " " + listAddresses.get(0).getCountryName();

                        }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                latitude.setText("Latitude : "+ String.valueOf(df.format(location.getLatitude())));
                longitude.setText("Longitude : "+ String.valueOf(df.format(location.getLongitude())));
                accuracy.setText("Accuracy : "+ String.valueOf(df.format(location.getAccuracy())));
                altitude.setText("Altitude : "+ String.valueOf(df.format(location.getAltitude())));
                address.setText("Address : "+addressoflocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}

