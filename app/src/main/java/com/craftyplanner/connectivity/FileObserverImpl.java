package com.craftyplanner.connectivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.FileObserver;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.Arrays;

public class FileObserverImpl extends FileObserver {

    String directoryPath;
    Context context;

    public FileObserverImpl(String path, Context context){
        super(path, FileObserver.CREATE);
        directoryPath = path;
        this.context = context;
    }



    @Override
    public void onEvent(int event, String path) {
        if(path!=null){

            sendNewProjectNotification();

            File directory = new File(path);
            Arrays.stream(directory.listFiles()).forEach(file -> {
                if(!file.isDirectory() && file.getName().equals("CraftyPlanner007")){
                    sendNewProjectNotification();
                }
            });

            //TODO:
            // -> get all files from directory
            // -> check if "CraftyPlanner007 file exists there
            // -> if yes, send message
            // -> if no, do nothing
        }
    }

    private void sendNewProjectNotification() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("New project available");
        alert.setMessage("Save project to personal Dashboard?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //TODO: add project to database
                // -> get ProjectDao reference
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //TODO: delete file from storage
                dialog.cancel();
            }
        });
        alert.show();
    }
}
