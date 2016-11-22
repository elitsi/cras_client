package mte.crasmonitoring.Auth;

/**
 * Created by eli on 18/11/2016.
 */


import java.util.ArrayList;
import java.util.List;

import mte.crasmonitoring.user_lists.ResponseList;
import mte.crasmonitoring.user_lists.Superviser;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CrasAccountService {

    @POST("/add_user")
    Call<ResponseBody> insertUser(@Body UserInfo userInfo);

    @GET("/add_user_supervisor")
    Call<ResponseBody> addSupervisor(@Header("sup_id") String supervisorId);

    @GET("/get_supervisors")
    Call<List<Superviser>> getSupervisors(@Header("user_id") String user_uid);

}
