package mte.crasmonitoring.Auth;

/**
 * Created by lenovo on 11/20/2016.
 */

public interface APICallbacks<T> {
    void successfulResponse(T t);
    void FailedResponse(String errorMessage);
}