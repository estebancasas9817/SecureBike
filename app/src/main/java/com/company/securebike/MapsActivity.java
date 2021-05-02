package com.company.securebike;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int Permisos_Call = 0;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private static final double RADIUS_OF_EARTH_KM = 6366 ;
    private FusedLocationProviderClient mFusedLocationClient;
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button buttonConf = (Button)findViewById(R.id.b1);
        Button buttonCont = (Button)findViewById(R.id.b3);

        buttonConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent(v.getContext(),Configuracion.class);
                startActivity(intentWeb);
            }
        });

        //INSTANCIAR SENSORES
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor =sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        //Sensores (LISTENER)
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                if (mMap != null)
                {
                    if (event.values[0] < 5000)
                    {
                        Log.i("MAPS", "DARK MAP " +event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this,R.raw.dark_style_map));
                    }
                    else
                    {
                        Log.i("MAPS", "LIGHT MAP " +event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this,R.raw.light_style_map));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy){}
        };

        final EditText searchMap = (EditText)findViewById(R.id.texto);
        searchMap.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Geocoder mGeocoder = new Geocoder(getBaseContext());
                String addressString =  searchMap.getText().toString();
                if (!addressString.isEmpty())
                {
                    try{
                        List<Address> addresses = mGeocoder.getFromLocationName(addressString,2);
                        if (addresses != null && !addresses.isEmpty())
                        {
                            Address addressResult =addresses.get(0);
                            LatLng position = new LatLng(addressResult.getLatitude(),addressResult.getLongitude());
                            if (mMap != null)
                            {
                                mMap.addMarker(new MarkerOptions().position(position)
                                        .title("TEST")
                                        .snippet("Población: 8.081.000")
                                        .alpha(0.5f)
                                        //Añadir un icono
                                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.camara)));
                                        //Añado el marcador de un color
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                            }
                        }
                        else
                        {
                            Toast.makeText(MapsActivity.this, "Dirección no encontrada",Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MapsActivity.this, "La dirección esta vacía",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        startLocationUpdates();


        int permissionLocation1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocation2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionLocation1 != PackageManager.PERMISSION_GRANTED
                && permissionLocation2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2000);
        }
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
        try{
            mMap = googleMap;
            // Add a marker in Bogota and move the camera
            LatLng bogota = new LatLng(4.65,-74.05);
            //Estilo del mapa
            //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.dark_style_map));
            //ZOOM

            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            //Marcador
            //Añadir directo:
            mMap.addMarker(new MarkerOptions().position(bogota).title("Marker in bogota"));
            //Instanciar variable
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
            Marker mfBogota = mMap.addMarker(new MarkerOptions().position(bogota)
                    .title("BOGOTÁ")
                    .snippet("Población: 8.081.000")
                    .alpha(0.5f)
                    //Añadir un icono
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.camara)));
                    //Añado el marcador de un color
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
            {
                @Override
                public void onMapLongClick(LatLng latLng)
                {
                    mMap.clear();
                    Marker seleccion = mMap.addMarker(new MarkerOptions().position(latLng).title("pos"));
                    CircleOptions circleOptions = new CircleOptions()
                            .center(latLng)
                            .radius(1000)//metros.
                            .strokeWidth(10)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(128, 127, 0,0))
                            .clickable(true);
                    mMap.addCircle(circleOptions);
                }});


            //Oculta el marcador
            //mfBogota.setVisible(false);
            //Eliminar el marcador
            //mfBogota.remove();
            //Borrar todo los marcadores
            //mMap.clear();

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationRequest = createLocationRequest();

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    double latitud = location.getLatitude();
                    double longitud = location.getLongitude();
                    LatLng Casa = new LatLng(latitud,longitud);
                    mMap.addMarker(new MarkerOptions().position(Casa).title("CASA"));
                    Log.i("LOCATION", "Location update in the callback: " + location);
                    if (location != null) {
                        mMap.addMarker(new MarkerOptions().position(Casa)
                                .title("Aqui estoy")
                                .snippet("hola")
                                .alpha(0.5f)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Casa));
                    }
                }
            };

        }
        catch(Exception ex)
        {
            Log.i("",ex.getStackTrace().toString());
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
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

    private void startLocationUpdates() {
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
}