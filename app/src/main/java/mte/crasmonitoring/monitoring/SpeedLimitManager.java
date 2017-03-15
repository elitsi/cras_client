package mte.crasmonitoring.monitoring;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateUtils;

/**
 * Created by Mickael on 11/17/2016.
 */

public class SpeedLimitManager extends MonitoringBase {
    private static final int SPEED_LIMIT = 60;
    private static final int INTERVAL_TIME = (int) (DateUtils.SECOND_IN_MILLIS*5);
    private static final int INTERVAL_DISTANCE = 100;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private int lastKnownSpeedViolation = -1;
    public SpeedLimitManager(Context context, MonitoringListener speedLimitListener)
    {
        super(speedLimitListener);
        this.context = context;
    }

    @Override
    public void startMonitoring()
    {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                int speedKm = (int) (location.getSpeed()*3.6);

                if(speedKm > SPEED_LIMIT)
                {
                    lastKnownSpeedViolation = speedKm;
                    didBad();

                }
                else
                    didGood();

            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_TIME, INTERVAL_DISTANCE, locationListener);
    }

    @Override
    public void stopMonitoring()
    {
        if(locationManager != null && locationListener != null)
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
        }
    }

    public int getDrivingSpeed()
    {
        return lastKnownSpeedViolation;
    }


}
