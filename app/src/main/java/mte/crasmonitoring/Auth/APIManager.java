package mte.crasmonitoring.Auth;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import mte.crasmonitoring.user_lists.Supervised;
import mte.crasmonitoring.user_lists.Supervisor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by lenovo on 11/20/2016.
 */

public class APIManager {

    public static void getSupervises(final Context context, String user_uid, final APICallbacks<List<Supervised>> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<List<Supervised>> call = accountService.getSupervises(user_uid);

        call.enqueue(new Callback<List<Supervised>>() {
            @Override
            public void onResponse(Call<List<Supervised>> call, retrofit2.Response<List<Supervised>> response) {
                if (response.isSuccessful()) {
                    List<Supervised> sup = response.body();
                    apiCallbacks.successfulResponse(response.body());
                } else {
                    Log.d("Response Failed: ", response.message());
                    apiCallbacks.FailedResponse(response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Supervised>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                apiCallbacks.FailedResponse(t.getMessage());
            }
        });
    }

    public static void getSupervisors(final Context context, String user_uid, final APICallbacks<List<Supervisor>> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<List<Supervisor>> call = accountService.getSupervisors(user_uid);

        call.enqueue(new Callback<List<Supervisor>>() {
            @Override
            public void onResponse(Call<List<Supervisor>> call, retrofit2.Response<List<Supervisor>> response) {
                if (response.isSuccessful()) {
                    List<Supervisor> sup = response.body();
                    apiCallbacks.successfulResponse(response.body());
                } else {
                    Log.d("Response Failed: ", response.message());
                    apiCallbacks.FailedResponse(response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Supervisor>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                apiCallbacks.FailedResponse(t.getMessage());
            }
        });
    }


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
