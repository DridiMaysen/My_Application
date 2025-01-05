package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Activity.ForgetActivity;
import com.example.myapplication.Activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener

{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private ActivityMapsBinding binding;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code =99;
    private double latitude , longitude;
    private int ProximityRadius =5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onclick( View v){
        String hospital="hospital", clinic="clinic", pharmacy="pharmacy";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();



        if (v.getId()==R.id.search_address){

                EditText addressField=(EditText) findViewById(R.id.location_search);
                String address =addressField.getText().toString();
                List<Address> addressList=null;
                MarkerOptions userMarkerOptions= new MarkerOptions();
                if (! TextUtils.isEmpty(address)){
                    Geocoder geocoder= new Geocoder(this);
                    try {
                        addressList= geocoder.getFromLocationName(address,6);

                        if (addressList!=null){
                            for (int i =0; i<addressList.size();i++){
                                Address userAddress= addressList.get(i);
                                LatLng latLng= new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }


                        }
                        else {
                            Toast.makeText(this, "location not found...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                else {
                    Toast.makeText(this,"please write any location", Toast.LENGTH_SHORT);
                }
        }
        if (v.getId()== R.id.hospitals_nearby) {
            mMap.clear();
            String url = getUrl(latitude, longitude, hospital);
            transferData[0] = mMap;
            transferData[1] = url;
            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Searching for Nearby Hospitals ...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Schowing Nearby Hospitals ...", Toast.LENGTH_SHORT).show();
        }
        if (v.getId()==R.id.clinic_nearby) {
            mMap.clear();
            String url = getUrl(latitude, longitude, clinic);
            transferData[0] = mMap;
            transferData[1] = url;
            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Searching for Nearby Clinic ...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Schowing Nearby Clinic ...", Toast.LENGTH_SHORT).show();
        }
        if (v.getId()== R.id.pharmacy_nearby){
                mMap.clear();
                String url= getUrl(latitude ,longitude, pharmacy);
                transferData[0]=mMap;
                transferData[1]= url ;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Pharmacy ...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Schowing Nearby Pharmacy ...", Toast.LENGTH_SHORT).show();
                }
        if (v.getId()==R.id.homee){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }




    }
    private String getUrl(double latitude,double longitude,String nearbyPlace){
        StringBuilder googleURl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURl.append("location=36.428161,10.569706");
        googleURl.append("&radius=" + ProximityRadius);
        googleURl.append("&type=" + nearbyPlace);
        googleURl.append("&sensor=true");
        googleURl.append("&key="+ "AIzaSyB52G0nxo7KDtUnqYryL28EcAg39AXGZOI");
        Log.d("MapsActivity","url = " + googleURl.toString());
        return googleURl.toString();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setMyLocationEnabled(true);
    }
    public boolean checkUserLocationPermission(){
        if ( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient== null){
                            buildGoogleApiClient();
                        }mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
                }
                return ;

        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient=new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude= location.getLatitude();
        longitude= location.getLongitude();
        lastLocation= location;
        if (currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("user current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currentUserLocationMarker =mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest= new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}