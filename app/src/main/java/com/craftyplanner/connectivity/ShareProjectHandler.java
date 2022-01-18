package com.craftyplanner.connectivity;

import android.content.Context;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.craftyplanner.objects.Project;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareProjectHandler {

    Context context;
    BluetoothHandler bluetoothHandler;

    public ShareProjectHandler(Context context){

        this.context = context;
        bluetoothHandler = new BluetoothHandler(context);
    }

    public void startConnection(){
        bluetoothHandler.initializeBluetooth();
        bluetoothHandler.enableBluetooth();
    }

    public void sendProject(Project project){

        String filename = "CraftyPlanner007";

        createFileFromProject(filename, project);

        File internalFile = new File(context.getFilesDir(), filename);
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", internalFile);

        bluetoothHandler.sendFileViaBluetooth(contentUri);
    }

    private void createFileFromProject(String filename, Project project) {

        //create txt file from project object
        // -> parse project object to string
        // ->use code below

        String fileContents = "Hello world!";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
