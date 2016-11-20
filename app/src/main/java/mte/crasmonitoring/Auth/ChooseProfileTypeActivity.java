package mte.crasmonitoring.Auth;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import mte.crasmonitoring.MainActivity;
import mte.crasmonitoring.R;
import mte.crasmonitoring.utils.Constants;
import mte.crasmonitoring.utils.PermissionsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;


public class ChooseProfileTypeActivity extends AppCompatActivity {
    private static final String TAG = ChooseProfileTypeActivity.class.getSimpleName();
    private FirebaseUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);

        checkInvitations();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            user = auth.getCurrentUser();
            ((TextView)(findViewById(R.id.mailTxt))).setText(user.getEmail());
            ((TextView)(findViewById(R.id.nameTxt))).setText(user.getDisplayName());

            CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, this);
            //Call<String> call = accountService.insertUser(user.getUid(),user.getEmail(),user.getDisplayName(),"");
            Call<String> call = accountService.insertUser2(new UserInfo(user.getDisplayName(), user.getEmail(),user.getUid(),"testImage"));


            call.enqueue(new Callback<String >() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {
                         if (response.isSuccessful()) {
                             // user object available
                             Log.d("Response Successful: ", response.message());

                             SharedPreferences sharedPref = ChooseProfileTypeActivity.this.getPreferences(Context.MODE_PRIVATE);
                             SharedPreferences.Editor editor = sharedPref.edit();
                             editor.putString(Constants.KEY_USER_ID, user.getUid());
                             editor.commit();

                             checkInvitations();

                         } else {
                             Log.d("Response Failed: ", response.message());
                         }
                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {
                         // something went completely south (like no internet connection)
                         Log.d("Error", t.getMessage());
                     }
            });


            Toast.makeText(getApplicationContext(),call.toString(),Toast.LENGTH_LONG);
        }

        (findViewById(R.id.btn_share_deep_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null)
                {
                    String deepLink = buildDeepLink(user.getUid());

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    intent.putExtra(Intent.EXTRA_TEXT,"I want to monitor you!\n" + deepLink);

                    Intent chooser = Intent.createChooser(intent, "Invite a friend");

                    // Verify the intent will resolve to at least one activity
                    if (intent.resolveActivity(ChooseProfileTypeActivity.this.getPackageManager()) != null) {
                        startActivity(chooser);
                    }
                }
            }
        });


    }

    public String  buildDeepLink(@NonNull String userId) {
        // Get this app's package name.
        String packageName = getApplicationContext().getPackageName();

        return new StringBuilder().append("https://")
        .append(getString(R.string.invite_url_host))
        .append("/?link=")
        .append(getString(R.string.invite_url_deep_link))
        .append(userId)
        .append("&apn=")
        .append(packageName).toString();
    }

    private void checkInvitations()
    {
        // Build GoogleApiClient with AppInvite API for receiving deep links
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "onConnectionFailed. " + connectionResult.getErrorMessage());
                    }
                })
                .addApi(AppInvite.API)
                .build();

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, false)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String inviterId = getQueryString(deepLink,"id");

                                    CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class, ChooseProfileTypeActivity.this);
                                    //Call<String> call = accountService.insertUser(user.getUid(),user.getEmail(),user.getDisplayName(),"");
                                    Call<String> call = accountService.addSupervisor(inviterId);
                                    call.enqueue(new Callback<String >() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()) {
                                                // user object available
                                                Log.d("Response Successful: ", response.message());
                                            } else {
                                                Log.d("Response Failed: ", response.message());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            // something went completely south (like no internet connection)
                                            Log.d("Error", t.getMessage());
                                        }
                                    });


                                    Log.v("DeepLinkTest","Deep link - " + deepLink);
                                    Log.v("DeepLinkTest","inviterId - " + inviterId);
                                } else {
                                    Log.d("DeepLinkTest", "getInvitation: no deep link found.");
                                }
                            }
                        });
    }

    public static String getQueryString(String url, String tag) {
        try {
            Uri uri=Uri.parse(url);
            return uri.getQueryParameter(tag);
        }catch(Exception e){
            Log.e(TAG,"getQueryString() " + e.getMessage());
        }
        return "";
    }

}
