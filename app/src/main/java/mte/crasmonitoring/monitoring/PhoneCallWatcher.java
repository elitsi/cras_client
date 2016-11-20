package mte.crasmonitoring.monitoring;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import mte.crasmonitoring.MainActivity;

/**
 * Watches for call state changes and notifies attached listener.
 */
public class PhoneCallWatcher implements MonitoringAbilityService {

    private Context mContext;
    private boolean mIsWatching;
    private PhoneCallReceiver mReceiver;
    private OnCallStateListener mListener;
    private BluetoothHeadset mBluetoothHeadset;

    public interface OnCallStateListener {
        void onCallAnswered();
        void onCallEnded();
    }

    private interface BluetoothConnectionListener{
        void onDeviceConnected();
        void onDeviceNotConnected();
    }

    public PhoneCallWatcher(Context context, OnCallStateListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setOnCallStateListener(OnCallStateListener listener) {
        mListener = listener;
    }

    @Override
    public void startMonitoring() {
        if (mIsWatching) {
            return;
        }

        if (mReceiver == null) {
            mReceiver = new PhoneCallReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        mContext.registerReceiver(mReceiver, filter);
        mIsWatching = true;
    }

    @Override
    public void stopMonitoring() {
        if (mIsWatching && mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        mIsWatching = false;
    }

    private class PhoneCallReceiver extends BroadcastReceiver {

        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private boolean isIncoming;

        @Override
        public void onReceive(Context context, Intent intent) {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state);
        }

        public void onCallStateChanged(Context context, int state) {
            if(lastState == state){
                //No change, debounce extras
                return;
            }
            Log.d("Receiver", state + "");
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    checkBluetoothConnection(new BluetoothConnectionListener() {
                        @Override
                        public void onDeviceConnected() {

                        }

                        @Override
                        public void onDeviceNotConnected() {
                            mListener.onCallAnswered();
                        }
                    });

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    checkBluetoothConnection(new BluetoothConnectionListener() {
                        @Override
                        public void onDeviceConnected() {}

                        @Override
                        public void onDeviceNotConnected() {
                            if(mListener != null)
                                mListener.onCallEnded();
                        }
                    });

                    break;
            }
            lastState = state;
        }
    }

    private void checkBluetoothConnection(final BluetoothConnectionListener bluetoothConnectionListener)
    {
        // Get the default adapter
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Define Service Listener of BluetoothProfile
        BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.HEADSET) {
                    mBluetoothHeadset = (BluetoothHeadset) proxy;

                    //call functions on mBluetoothHeadset to check if Bluetooth SCO audio is connected.
                    List<BluetoothDevice> devices = mBluetoothHeadset.getConnectedDevices();
                    for ( final BluetoothDevice dev : devices ) {
                        if(mBluetoothHeadset.isAudioConnected(dev) || mBluetoothHeadset.startVoiceRecognition(dev))
                        {
                            bluetoothConnectionListener.onDeviceConnected();
                            return;
                            //Log.v("TestBluetooth", "Connected device - " + dev.getName());
                           // Toast.makeText(mContext,"Connected device - " + dev.getName(), Toast.LENGTH_LONG).show();
                        }

                    }
                    bluetoothConnectionListener.onDeviceNotConnected();
                    // finally Close proxy connection after use.
                    mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothHeadset);

                }
            }
            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.HEADSET) {
                    mBluetoothHeadset = null;
                }
            }
        };

        // Establish connection to the proxy.
        mBluetoothAdapter.getProfileProxy(mContext, mProfileListener, BluetoothProfile.HEADSET);
    }

}
