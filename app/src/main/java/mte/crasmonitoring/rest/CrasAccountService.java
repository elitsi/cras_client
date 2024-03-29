package mte.crasmonitoring.rest;

/**
 * Created by eli on 18/11/2016.
 */


import java.util.List;

import mte.crasmonitoring.model.UserInfo;
import mte.crasmonitoring.model.Supervised;
import mte.crasmonitoring.model.RemoteUser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CrasAccountService {

    @POST("/add_user")
    Call<ResponseBody> insertUser(@Body UserInfo userInfo);

    @GET("/add_user_supervisor")
    Call<ResponseBody> addSupervisor(@Header("sup_id") String supervisorId);

    @GET("/get_supervised_by")
    Call<List<RemoteUser>> getSupervisors(@Header("user_id") String user_uid);

    @GET("/get_supervise")
    Call<List<RemoteUser>> getSupervises(@Header("user_id") String user_uid);

    @GET("/start_monitoring")
    Call<ResponseBody> sendMonitorRequest(@Header("to_monitor_id") String supervisedId);

    @GET("/accept_monitoring")
    Call<ResponseBody> acceptMonitorRequest(@Header("sup_id") String supervisedId);

    @GET("/application_anomaly")
    Call<ResponseBody> sendAppViolationEvent(@Header("sup_id") String supervisedId);

    @GET("/speed_anomaly")
    Call<ResponseBody> sendSpeedViolationEvent(@Header("speed") int speed);

    @GET("/stop_monitoring_by_supervise")
    Call<ResponseBody> stopMonitorBySupervise(@Header("sup_id") String supervisorId);

    @GET("/stop_monitoring_by_supervisor")
    Call<ResponseBody> stopMonitorBySupervisor(@Header("sup_id") String superviseId);

    @GET("/get_user_by_id")
    Call<RemoteUser> getUser(@Header("user_to_get") String userToGet);

    @GET("/get_speed_limit")
    Call<Double> getSpeedLimit(@Header("latitude") double latitude,@Header("longitude") double longitude);

    @GET("/get_log_session")
    Call<ResponseBody> getUserSessions(@Header("sup_id") String sup_id);

}
