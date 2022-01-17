package com.craftyplanner.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

public class BluetoothHandler {

    private BluetoothAdapter bluetoothAdapter;

    public BluetoothHandler(Context context){

        initializeBluetooth(context);
        enableBluetooth();

    }

    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    private void initializeBluetooth(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}
