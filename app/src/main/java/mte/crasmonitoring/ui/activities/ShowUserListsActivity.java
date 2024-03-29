package mte.crasmonitoring.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import mte.crasmonitoring.R;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.ui.adapters.TabsPagerAdapter;
import mte.crasmonitoring.utils.GeneralUtils;
import mte.crasmonitoring.utils.PermissionsManager;
import mte.crasmonitoring.utils.SharedPrefsUtils;

/**
 * Created by eli on 21/11/2016.
 */

public class ShowUserListsActivity extends AppCompatActivity  {
    private static final String TAG = GeneralUtils.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_lists_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        askForPermissions();
        setupViewPager();

        setupFab();
        setupLogout();

    }

    private void askForPermissions()
    {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.PROCESS_OUTGOING_CALLS

                ).withListener(new MultiplePermissionsListener()
        {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report)
            {
                askForComplexPermissions();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
            {
                token.continuePermissionRequest();
            }

        }).check();
    }

    private void askForComplexPermissions()
    {
        PermissionsManager.requestUsageStatsPermission(ShowUserListsActivity.this, new PermissionsManager.PermissionsListener() {


            @Override
            public void onPermissionsGranted() {

                PermissionsManager.requestOverlayPermission(ShowUserListsActivity.this, new PermissionsManager.PermissionsListener() {
                    @Override
                    public void onPermissionsGranted() {

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInvitations();
    }

    private void setupViewPager()
    {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    private void setupFab()
    {
        (findViewById(R.id.fab_share_deep_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDeepLink();
            }
        });
    }

    private void setupLogout()
    {
        (findViewById(R.id.logout_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(ShowUserListsActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(ShowUserListsActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
            }

        });
    }

    private void checkInvitations()
    {
        try {
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
                                        String inviterId = GeneralUtils.getQueryString(deepLink,"id");
                                        APIManager.addSupervisor(ShowUserListsActivity.this, inviterId, new APICallbacks<String>() {
                                            @Override
                                            public void successfulResponse(String s) {
                                                EventBus.getDefault().post(new Events.RefreshRemoteUsersEvent());
                                            }

                                            @Override
                                            public void failedResponse(String errorMessage) {

                                            }

                                        });
                                    } else {
                                        Log.d("DeepLinkTest", "getInvitation: no deep link found.");
                                    }

                                }
                            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void shareDeepLink()
    {
        String deepLink = buildDeepLink(SharedPrefsUtils.getUserId(this));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,"I want to monitor you!\n" + deepLink);

        Intent chooser = Intent.createChooser(intent, "Invite a friend");

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(ShowUserListsActivity.this.getPackageManager()) != null) {
            startActivity(chooser);
        }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PermissionsManager.REQUEST_USAGE_PERMISSION)
            PermissionsManager.requestUsageStatsPermission(ShowUserListsActivity.this, new PermissionsManager.PermissionsListener() {
                @Override
                public void onPermissionsGranted() {
                    PermissionsManager.requestOverlayPermission(ShowUserListsActivity.this, new PermissionsManager.PermissionsListener() {
                        @Override
                        public void onPermissionsGranted() {

                        }
                    });
                }
            });
        else if(requestCode == PermissionsManager.REQUEST_OVERLAY_PERMISSION)
        {
            PermissionsManager.requestOverlayPermission(ShowUserListsActivity.this, new PermissionsManager.PermissionsListener() {
                @Override
                public void onPermissionsGranted() {

                }
            });
        }

    }

}
