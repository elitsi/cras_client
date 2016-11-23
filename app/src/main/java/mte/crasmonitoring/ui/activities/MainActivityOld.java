package mte.crasmonitoring.ui.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.firebase.iid.FirebaseInstanceId;

import mte.crasmonitoring.R;
import mte.crasmonitoring.utils.PermissionsManager;

public class MainActivityOld extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.v("firebase", "firebaseToken - " + firebaseToken);

        (findViewById(R.id.btn_start_monitoring)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Permissive.Request(Manifest.permission.BLUETOOTH, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.PROCESS_OUTGOING_CALLS)
                        .whenPermissionsGranted(new PermissionsGrantedListener() {
                            @Override
                            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                PermissionsManager.requestUsageStatsPermission(MainActivityOld.this, new PermissionsManager.PermissionsListener() {
                                    @Override
                                    public void onPermissionsGranted() {
                                        PermissionsManager.requestOverlayPermission(MainActivityOld.this, new PermissionsManager.PermissionsListener() {
                                            @Override
                                            public void onPermissionsGranted() {
                                                startMonitoringActivity();
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .whenPermissionsRefused(new PermissionsRefusedListener() {
                            @Override
                            public void onPermissionsRefused(String[] permissions) {
                                // given permissions are refused
                                Toast.makeText(MainActivityOld.this,"Please enable permission", Toast.LENGTH_LONG).show();                            }
                        })
                        .execute(MainActivityOld.this);
            }
        });

        (findViewById(R.id.btn_copy_token)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("firebasetoken", FirebaseInstanceId.getInstance().getToken());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivityOld.this,"Token copied", Toast.LENGTH_LONG).show();

            }
        });



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PermissionsManager.REQUEST_USAGE_PERMISSION)
            PermissionsManager.requestUsageStatsPermission(MainActivityOld.this, new PermissionsManager.PermissionsListener() {
                @Override
                public void onPermissionsGranted() {
                    PermissionsManager.requestOverlayPermission(MainActivityOld.this, new PermissionsManager.PermissionsListener() {
                        @Override
                        public void onPermissionsGranted() {
                            startMonitoringActivity();
                        }
                    });
                }
            });
        else if(requestCode == PermissionsManager.REQUEST_OVERLAY_PERMISSION)
        {
            PermissionsManager.requestOverlayPermission(MainActivityOld.this, new PermissionsManager.PermissionsListener() {
                @Override
                public void onPermissionsGranted() {
                    startMonitoringActivity();
                }
            });
        }

    }

    private void startMonitoringActivity()
    {
        startActivity(new Intent(MainActivityOld.this, MonitoringActivity.class));
    }

}
