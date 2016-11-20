package mte.crasmonitoring.Auth;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import mte.crasmonitoring.utils.SharedPrefsUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by lenovo on 11/20/2016.
 */

public class APIManager {

    public static void insertUser(final Context context, UserInfo userInfo, final APICallbacks<ResponseBody> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<ResponseBody> call = accountService.insertUser(userInfo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    apiCallbacks.successfulResponse(response.body());

                } else {
                    Log.d("Response Failed: ", response.message());
                    apiCallbacks.FailedResponse(response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                apiCallbacks.FailedResponse(t.getMessage());
            }
        });
    }

    public static void addSupervisor(Context context, String inviterId, final APICallbacks<String> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<ResponseBody> call = accountService.addSupervisor(inviterId);
        call.enqueue(new Callback<ResponseBody >() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // user object available
                    //Log.d("Response Successful: ", response.message());
                    try {
                        apiCallbacks.successfulResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.d("Response Failed: ", response.message());
                    apiCallbacks.successfulResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                apiCallbacks.successfulResponse(t.getMessage());
            }
        });
    }

}
