package mte.crasmonitoring.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ChooseProfileTypeActivity.class));
            finish();
        }

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            startActivity(new Intent(this, ChooseProfileTypeActivity.class));
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
