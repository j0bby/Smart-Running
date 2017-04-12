package a4if_insa.getcoordinate2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import a4if_insa.getcoordinate2.R;

public class MainActivity extends Activity {

    private TextView positionText;// textview for latitude and for the longtitude
    private TextView tipInfo;// message info

    private LocationManager locationManager;//

    private String provider;// location provider

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        positionText = (TextView) findViewById(R.id.position_text);
        tipInfo = (TextView) findViewById(R.id.tipInfo);
        // instanciate locaionManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get all available providers
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            // we appreciate here usage of GPS
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // when there is no available location provider
            Toast.makeText(MainActivity.this, "there is no available location provider", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // print device's postion coordinate
            String firstInfo = "first response of your request";
            showLocation(location, firstInfo);
        } else {
            String info = "Sorry, cannot obtain the actual position";
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
            positionText.setText(info);
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

    ;

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
            String changeInfo = "refresh every 10 seconds\n" + sdf.format(new Date())
                    + ",\n longitude : " + location.getLongitude() + ",\n latitude : "
                    + location.getLatitude();
            showLocation(location, changeInfo);
        }
    };

    /**
     *  print device's postion coordinate
     *
     * @param location
     */
    private void showLocation(Location location, String changeInfo) {
        // TODO Auto-generated method stub
        String currentLocation = "Your actual longitude : " + location.getLongitude() + ",\n"
                + "Your actual latitude : " + location.getLatitude();
        positionText.setText(currentLocation);
        tipInfo.setText(changeInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
