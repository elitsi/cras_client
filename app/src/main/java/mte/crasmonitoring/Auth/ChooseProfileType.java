package mte.crasmonitoring.Auth;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import mte.crasmonitoring.MainActivity;
import mte.crasmonitoring.R;
import mte.crasmonitoring.utils.PermissionsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;


public class ChooseProfileType extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            ((TextView)(findViewById(R.id.mailTxt))).setText(user.getEmail());
            ((TextView)(findViewById(R.id.nameTxt))).setText(user.getDisplayName());


            CrasAccountService accountService = ServiceGenerator.createService(CrasAccountService.class);
            //Call<String> call = accountService.insertUser(user.getUid(),user.getEmail(),user.getDisplayName(),"");
            Call<String> call = accountService.insertUser2(new UserInfo(user.getDisplayName(), user.getEmail(),user.getUid(),"testImage"));


            call.enqueue(new Callback<String >() {
                             @Override
                             public void onResponse(Call<String> call, Response<String> response) {
                                 if (response.isSuccessful()) {
                                     // user object available
                                     Log.d("Response Successful: ", response.message());

                                 } else {
                                     Log.d("Response Failed: ", response.message());

                                     // error response, no access to resource?
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


    }

}
