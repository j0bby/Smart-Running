package a4414.test;


import android.graphics.Color;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class testmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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


}
