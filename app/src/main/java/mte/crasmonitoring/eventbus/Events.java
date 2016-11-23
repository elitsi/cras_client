package mte.crasmonitoring.eventbus;

import java.util.Calendar;

/**
 * Created by lenovo on 11/19/2016.
 */

public class Events {

    public static class DrivingLimitViolationEvent
    {
        private int drivingSpeed;
        public DrivingLimitViolationEvent(int drivingSpeed) {
            this.drivingSpeed = drivingSpeed;
        }

        public int getDrivingSpeed() {
            return drivingSpeed;
        }
    }

    public static class AppViolationEvent {}

    public static class RefreshRemoteUsersEvent {}

}
