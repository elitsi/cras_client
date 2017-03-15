package mte.crasmonitoring.rest;

/**
 * Created by lenovo on 11/20/2016.
 */

public interface APICallbacks<T> {
    void successfulResponse(T t);
    void failedResponse(String errorMessage);
}
