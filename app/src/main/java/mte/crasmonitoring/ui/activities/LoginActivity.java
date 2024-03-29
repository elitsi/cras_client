package mte.crasmonitoring.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import mte.crasmonitoring.R;
import mte.crasmonitoring.model.UserInfo;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.utils.SharedPrefsUtils;
import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        loginWithFirebase();
    }

    private void loginWithFirebase()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            getUserDetailsAndContinue();
        }

        else {
            showFirebaseActivity();
        }
    }

    private void showFirebaseActivity()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.eye_full)
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    private void getUserDetailsAndContinue()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            final FirebaseUser user = auth.getCurrentUser();

            Log.v("firebaseToken", "firebaseToken - " + FirebaseInstanceId.getInstance().getToken());
            Log.v("firebaseToken", "image - " + user.getPhotoUrl());
            String photoUrl = (user.getPhotoUrl() == null) ? "" : user.getPhotoUrl().toString();
            UserInfo userInfo = new UserInfo(
                    user.getDisplayName(),
                    user.getEmail(),
                    user.getUid(),
                    photoUrl,
                    FirebaseInstanceId.getInstance().getToken()
            );
            APIManager.insertUser(this, userInfo, new APICallbacks<ResponseBody>() {
                @Override
                public void successfulResponse(ResponseBody responseBody) {
                    SharedPrefsUtils.saveUserId(LoginActivity.this,user.getUid());
                   // checkInvitations();

                    Intent i = new Intent(LoginActivity.this,ShowUserListsActivity.class);
                    startActivity(i);
                    finish();

                }

                @Override
                public void failedResponse(String errorMessage) {
                    Toast.makeText(LoginActivity.this, "Login user request failed.", Toast.LENGTH_LONG).show();
                    showFirebaseActivity();
                }

            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            getUserDetailsAndContinue();
            return;
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            Toast.makeText(this, "No internet connection - please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "An error has occurred while trying to log in..", Toast.LENGTH_SHORT).show();

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }
}
