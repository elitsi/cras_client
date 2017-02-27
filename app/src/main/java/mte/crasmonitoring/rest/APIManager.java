package mte.crasmonitoring.rest;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import mte.crasmonitoring.model.UserInfo;
import mte.crasmonitoring.model.Supervised;
import mte.crasmonitoring.model.RemoteUser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by lenovo on 11/20/2016.
 */

public class APIManager {

    public static void getSupervises(final Context context, String user_uid, final APICallbacks<List<RemoteUser>> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<List<RemoteUser>> call = accountService.getSupervises(user_uid);

        call.enqueue(new Callback<List<RemoteUser>>() {
            @Override
            public void onResponse(Call<List<RemoteUser>> call, retrofit2.Response<List<RemoteUser>> response) {
                if (response.isSuccessful()) {
                    List<RemoteUser> sup = response.body();
                    apiCallbacks.successfulResponse(response.body());
                } else {
                    Log.d("Response Failed: ", response.message());
                    apiCallbacks.FailedResponse(response.message());
                }
            }
            @Override
            public void onFailure(Call<List<RemoteUser>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                apiCallbacks.FailedResponse(t.getMessage());
            }
        });
    }

    public static void getSupervisors(final Context context, String user_uid, final APICallbacks<List<RemoteUser>> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<List<RemoteUser>> call = accountService.getSupervisors(user_uid);

        call.enqueue(new Callback<List<RemoteUser>>() {
            @Override
            public void onResponse(Call<List<RemoteUser>> call, retrofit2.Response<List<RemoteUser>> response) {
                if (response.isSuccessful()) {
                    apiCallbacks.successfulResponse(response.body());
                } else {
                    Log.d("Response Failed: ", response.message());
                    apiCallbacks.FailedResponse(response.message());
                }
            }
            @Override
            public void onFailure(Call<List<RemoteUser>> call, Throwable t) {
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
                if(t == null) return;
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

                    try {
                        apiCallbacks.successfulResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    apiCallbacks.successfulResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                apiCallbacks.successfulResponse(t.getMessage());
            }
        });
    }

    public static void sendMonitorRequest(Context context, String supervisedId, final APICallbacks<String> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<ResponseBody> call = accountService.sendMonitorRequest(supervisedId);
        call.enqueue(new Callback<ResponseBody >() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        apiCallbacks.successfulResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    apiCallbacks.successfulResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                apiCallbacks.successfulResponse(t.getMessage());
            }
        });
    }

    public static void acceptMonitorRequest(Context context, String supervisorId, final APICallbacks<String> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<ResponseBody> call = accountService.acceptMonitorRequest(supervisorId);
        call.enqueue(new Callback<ResponseBody >() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        apiCallbacks.successfulResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    apiCallbacks.successfulResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                apiCallbacks.successfulResponse(t.getMessage());
            }
        });
    }

    public static void sendAppViolationEvent(Context context, String supervisorId, final APICallbacks<String> apiCallbacks)
    {
        CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, context);
        Call<ResponseBody> call = accountService.sendAppViolationEvent(supervisorId);
        call.enqueue(new Callback<ResponseBody >() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        apiCallbacks.successfulResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    apiCallbacks.successfulResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                apiCallbacks.successfulResponse(t.getMessage());
            }
        });
    }

}
