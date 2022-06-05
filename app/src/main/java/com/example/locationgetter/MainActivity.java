package com.example.locationgetter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textViewLat, textViewLong, textViewAdd, textViewLoc, textViewCountry;
    Button btn;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewLat = findViewById(R.id.lat);
        textViewLong = findViewById(R.id.longt);
        textViewAdd = findViewById(R.id.address);
        textViewLoc = findViewById(R.id.local);
        textViewCountry = findViewById(R.id.country);
        btn = findViewById(R.id.locationButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    showLocation();
                else
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        });
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if(location !=null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            textViewLat.setText("Lattitude" + addressList.get(0).getLatitude());
                            textViewLong.setText("Longitude"+addressList.get(0).getLongitude());
                            textViewAdd.setText(addressList.get(0).getAddressLine(0));
                            textViewLoc.setText(addressList.get(0).getLocality());
                            textViewCountry.setText(addressList.get(0).getCountryName());

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Location is not found",Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}