package com.craftyplanner.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class BluetoothHandler {

    private final Context context;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothHandler(Context context){
        this.context = context;
    }

    public void sendFileViaBluetooth(Uri projectFile){
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, projectFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> appsList = packageManager.queryIntentActivities( intent, 0);
        if(appsList.size() > 0 ){
            String packageName = null;
            String className = null;
            boolean found = false;

            for(ResolveInfo info: appsList){
                packageName = info.activityInfo.packageName;
                if( packageName.equals("com.android.bluetooth")){
                    className = info.activityInfo.name;
                    found = true;
                    break;
                }
            }
            if(found){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName(packageName, className);
                startActivity(context, intent, null);
            }
        }
    }

    public void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    public void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}
