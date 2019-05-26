package com.example.mybabymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ParentActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, ParentActivity parrentActivity) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mActivity = parrentActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context, "Wifi esta ON", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Wifi esta OFF", Toast.LENGTH_SHORT).show();
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            // Call if WifiP2pManager .requestPeers() to get a list of current peers
            if(mManager != null)
            {
                mManager.requestPeers(mChannel, mActivity.peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            // Respond to new connection or disconnections
            if(mManager == null)
            {
                return;
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected())
            {
                mManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
            }else {
                mActivity.connectionStatus.setText("Device Disconected");
            }
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            // Respond to this device's Wi-Fi state changing
        }
    }
}