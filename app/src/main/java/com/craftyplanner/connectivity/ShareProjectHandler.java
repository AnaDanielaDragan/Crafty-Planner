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
        String fileContents = ProjectContentParser.parseProjectToString(project);

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File internalFile = new File(context.getFilesDir(), filename);
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", internalFile);

        bluetoothHandler.sendFileViaBluetooth(contentUri);
    }

}
