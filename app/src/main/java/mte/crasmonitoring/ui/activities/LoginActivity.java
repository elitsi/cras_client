package mte.crasmonitoring.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import mte.crasmonitoring.model.UserInfo;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.utils.SharedPrefsUtils;
import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            //tmp
//            FirebaseUser user = auth.getCurrentUser();
//            UserInfo userInfo = new UserInfo(
//                    "test1",
//                    "testMail1",
//                    "1",
//                    "testPic",
//                    FirebaseInstanceId.getInstance().getToken()
//            );
//            APIManager.insertUser(this, userInfo, new APICallbacks<ResponseBody>() {
//                @Override
//                public void successfulResponse(ResponseBody responseBody) {
//                }
//
//                @Override
//                public void FailedResponse(String errorMessage) {}
//            });

            getUserDetailsAndContinue();

        }

        else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
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
                public void FailedResponse(String errorMessage) {}
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            getUserDetailsAndContinue();
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            return;
        }

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }
}
