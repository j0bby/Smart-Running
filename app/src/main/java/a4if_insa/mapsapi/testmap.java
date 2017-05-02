package a4if_insa.mapsapi;

import android.content.Context;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import a4if_insa.mapsa.test.R;

public class testmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider; // location provider
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static int meCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // instanciate locaionManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get all available providers
        List<String> providerList = locationManager.getProviders(true);
        //LocationManager.Provider
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            // we appreciate here usage of GPS
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // when there is no available location provider
            Toast.makeText(testmap.this, "please check if you have available location provider", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // print device's postion coordinate
            String firstInfo = "first response of your request";
            Toast.makeText(this, firstInfo, Toast.LENGTH_LONG).show();
        } else {
            String info = "Patiently waiting for 10sec and we will get you on the map.s";
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        }

        // renew the postion coordinate
        locationManager.requestLocationUpdates(provider, 10 * 1000, 1,locationListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            // remove the listener when program's exiting
            locationManager.removeUpdates(locationListener);
        }
    }

    public void refreshMap (GoogleMap myGoogleMap, double latitude, double longitude) {
        int poiColor= Color.argb(100,253,48,152);
        int poiStrokeColor = Color.argb(200,253,48,152);

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);
        // Get back the mutable Circle
        Circle circle = myGoogleMap.addCircle(circleOptions);

        myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Me" + meCounter).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
        meCounter += 1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int poiColor= Color.argb(100,253,48,152);
        int poiStrokeColor = Color.argb(200,253,48,152);

        int startColor = Color.argb(100,68,251,0);
        int startStrokeColor=Color.argb(200,68,251,0);

        // Add a marker in Sydney and move the camera
        LatLng lyon = new LatLng(45.782969, 4.873652);
        mMap.addMarker(new MarkerOptions().position(lyon).title("La Doua")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon,15));


        CircleOptions circleOptions3 = new CircleOptions()
                .center(lyon)
                .radius(50) // In meters
                .fillColor(startColor)
                .strokeColor(startStrokeColor);
        Circle circle3= mMap.addCircle(circleOptions3);

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(45.784031, 4.880026))
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);
        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mMap.addMarker(new MarkerOptions().position(new LatLng(45.784031, 4.880026)).title("Matthew").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();

        CircleOptions circleOptions2 = new CircleOptions()
                .center(new LatLng(45.784730, 4.872096))
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);

        mMap.addMarker(new MarkerOptions().position(new LatLng(45.784730, 4.872096)).title("soDomy").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();

        Circle circle2 = mMap.addCircle(circleOptions2);
        circle2.setFillColor(startColor);
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location location) {
            // code run when device's postion changed
//            String changeInfo = "refresh every 10 seconds\n" + sdf.format(new Date())
//                    + ",\n longitude : " + location.getLongitude() + ",\n latitude : "
//                    + location.getLatitude();
//            showLocation(location, changeInfo);
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String info = "refresh every moving 10 seconds \n" + sdf.format(new Date())
                    + ", \n longitude : " + location.getLongitude() + ", \n latitude : "
                    + location.getLatitude();
            Toast.makeText(testmap.this, info, Toast.LENGTH_LONG).show();
            refreshMap(mMap, lat, lon);
        }
    };


}
