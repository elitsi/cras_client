package mte.crasmonitoring.Auth;

/**
 * Created by eli on 18/11/2016.
 */


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CrasAccountService {

    @POST("/add_user")
    @FormUrlEncoded
    Call<String> insertUser(@Field("user_id") String uid,
                            @Field("mail") String email,
                            @Field("name") String displayName,
                            @Field("picture") String pictureUri);

    @POST("/add_user")
    Call<String> insertUser2(@Body UserInfo userInfo);

    @GET("/add_user_supervisor")
    Call<String> addSupervisor(@Header("sup_id") String supervisorId);

}
