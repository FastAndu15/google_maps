package com.example.google_maps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.google_maps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationListener m_oLocationListener;
    LocationManager m_oLocationManager;
    private PolylineOptions m_oLineOptions;
    Handler handler = new Handler();
    private Random m_oRandom;
    Runnable m_rUpdateLocatie = new Runnable() {
        @Override
        public void run() {
            double newLat,newLong;
            if(m_oRandom.nextInt(2) == 0) {
                if(m_oRandom.nextInt(2) == 0)
                    newLat = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                            1)).latitude + 0.0005;
                else
                    newLat = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                            1)).latitude - 0.0005;
                newLong = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                        1)).longitude;
            }
            else {
                newLat = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                        1)).latitude;
                if(m_oRandom.nextInt(2) == 0)
                    newLong = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                            1)).longitude + 0.0005;
                else

                    newLong = (m_oLineOptions.getPoints().get(m_oLineOptions.getPoints().size() -
                            1)).longitude - 0.0005;
            }
            updateHarta(new LatLng(newLat,newLong));
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        m_oLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        m_oLineOptions = new PolylineOptions().width(15).color(Color.RED);
        m_oRandom = new Random();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        m_oLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                updateHarta(loc);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        m_oLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,1,m_oLocationListener);
        LatLng startPoint = new LatLng(44.8585, 24.8692);
        updateHarta(startPoint);
        handler.postDelayed(m_rUpdateLocatie,10 * 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void updateHarta(LatLng locatieNoua) {
        if (mMap == null) return;
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(locatieNoua).title("Locatia mea"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locatieNoua, 16.0f));
        m_oLineOptions.add(locatieNoua);
// desenare linie
        mMap.addPolyline(m_oLineOptions);
    }
}
