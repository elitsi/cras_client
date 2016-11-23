package mte.crasmonitoring.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import mte.crasmonitoring.user_lists.ShowUserListsActivity;
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


            Intent i = new Intent(getBaseContext(),ShowUserListsActivity.class);
            startActivity(i);
            finish();

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            startActivity(new Intent(this, ShowUserListsActivity.class));
            finish();
            return;
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
