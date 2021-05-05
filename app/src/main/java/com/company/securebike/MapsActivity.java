package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int Permisos_Call = 0;
    private static final double RADIUS_OF_EARTH_KM = 6366;

    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient mFusedLocationClient;

    private Marker locacionActual;
    private Marker buscarLocation;
    private Polyline currentPolyline;
    private GoogleMap mMap;

    String permLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_ID = 15;

    private static final int SETTINGS_GPS = 20;

    Geocoder geocoder;
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Log.i(" LOCATION ", "Longitud: " + location.getLongitude());
                    Log.i(" LOCATION ", "Latitud: " + location.getLatitude());

                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    locacionActual = mMap.addMarker(new MarkerOptions().position(userLocation).title("Tu Ubicación"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

                }

            }

        };



        int permissionLocation1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocation2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionLocation1 != PackageManager.PERMISSION_GRANTED
                && permissionLocation2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2000);
        }

        initView();

        Button buttonConf = (Button) findViewById(R.id.b1);
        Button buttonCont = (Button) findViewById(R.id.b3);

        geocoder = new Geocoder(this);
        address = findViewById(R.id.texto);
        address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String direccion = address.getText().toString();
                    if (!direccion.isEmpty()) {
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(direccion, 2);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                if (mMap != null) {
                                    if(buscarLocation != null) {
                                        buscarLocation.remove();
                                    }
                                    LatLng location = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                                    buscarLocation = mMap.addMarker(new MarkerOptions().
                                            position(location).
                                            title(addressResult.getAddressLine(0)).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

                                    Double latitude1 = buscarLocation.getPosition().latitude;
                                    Double latitude2 = locacionActual.getPosition().latitude;
                                    Double longitude1 = buscarLocation.getPosition().longitude;
                                    Double longitude2 = locacionActual.getPosition().longitude;
                                    Toast.makeText(MapsActivity.this, "La distancia es: " + distance(latitude1, longitude1, latitude2, longitude2) + " Km", Toast.LENGTH_SHORT).show();


                                }
                            } else {
                                Toast.makeText(v.getContext(), "Dirección No Encontrada", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

        buttonConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent(v.getContext(), Configuracion.class);
                startActivity(intentWeb);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(buscarLocation != null)
                {
                    buscarLocation.remove();
                }
                buscarLocation=mMap.addMarker(new MarkerOptions().position(latLng).title(geoCoderSearchLatLang(latLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                Double latitude1 = buscarLocation.getPosition().latitude;
                Double latitude2 = locacionActual.getPosition().latitude;
                Double longitude1 = buscarLocation.getPosition().longitude;
                Double longitude2 = locacionActual.getPosition().longitude;
                Toast.makeText(MapsActivity.this, "La distancia es: " + distance(latitude1, longitude1, latitude2, longitude2) + " Km", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void compartir(View v)
    {
        //Toast.makeText(v.getContext(), "Pasando a registrarse", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
        String aux = "https://www.youtube.com/watch?v=iuTtlb2COtc&ab_channel=CamiloVEVO";
        intent.putExtra(Intent.EXTRA_TEXT,aux);
        startActivity(intent);
    }

    private void getRoute(Double latitude1,Double longitude1,Double latitude2,Double longitude2) {

        String origen = latitude1.toString()+","+longitude1.toString();
        String destino = latitude2.toString()+","+longitude2.toString();
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext();
        context.setApiKey("AIzaSyCBfwtEMWCpBt9XwfJwu_2UHYTBZVW7xnM");
        DirectionsApiRequest req = DirectionsApi.getDirections(context, origen,destino);
        try{
            DirectionsResult res = req.await();
            Log.i("DIRECCION",res.toString());

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.i("DIRECCION",e.getMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(5);
            mMap.addPolyline(opts);
        }
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latlng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( latlng, 18);
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    private void initView(){
        if(ContextCompat.checkSelfPermission(this, permLocation)== PackageManager.PERMISSION_GRANTED) {
            checkSettingsLocation();
        }
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = 6371 * c;
        return Math.round(result*100.0)/100.0;
    }

    private String geoCoderSearchLatLang(LatLng latLng) {
        String finalName="";
        try {
            Address name = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
            finalName = name.getAddressLine(0);
        }catch (IOException e){
            e.printStackTrace();
        }
        return finalName;
    }

    private void checkSettingsLocation()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                int statusCode = ((ApiException)e).getStatusCode();
                switch (statusCode){
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MapsActivity.this,SETTINGS_GPS );
                        }
                        catch (IntentSender.SendIntentException sendEx)
                        {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    public void contactos(View v)
    {
        Toast.makeText(v.getContext(), "Tus contactos", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Contactos.class);
        intent.putExtra("login", "Hola_4");
        startActivity(intent);
    }

    public void agregarBicicleta(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a agregar bicicleta", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), AgregarBicicleta.class);
        intent.putExtra("home", "Hola_5");
        startActivity(intent);
    }

    public void configurarBicicleta(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a configurar bicicleta", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), ConfiguracionBicicleta.class);
        intent.putExtra("home", "Hola_6");
        startActivity(intent);
    }

    public void llamar (View v)
    {
        Intent intent = new Intent (Intent.ACTION_CALL, Uri.parse("tel:3124142683"));
        Permisos.requestPermission(this, Manifest.permission.READ_CONTACTS, "", Permisos_Call);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            startActivity(intent);
    }

    private void startLocationUpdates()
    {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); //tasa de refresco en milisegundos
        locationRequest.setFastestInterval(5000); //máxima tasa de refresco
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}