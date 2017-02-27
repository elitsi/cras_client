package mte.crasmonitoring.model;

public interface SendViolationToApi {
        void sendAppViolationEvent();
        void sendDrivingViolationEvent();
    }