package com.example.daniel.dropboxexplorer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daniel.dropboxexplorer.Dropbox.AuthHelper;
import com.example.daniel.dropboxexplorer.Dropbox.Client;
import com.example.daniel.dropboxexplorer.Dropbox.DropBoxFileHelper;
import com.example.daniel.dropboxexplorer.adapter.LocalAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by daniel on 10/12/16.
 */

public class LocalListFragment extends Fragment {

    public interface LocalListListener{
        void onUpload();

        String getCurrentDropboxPath();
    }

    private ListView lv_local;
    private LocalAdapter adapter;

    private Stack<File> directoryStack;

    private File currentFile;
    private LocalListListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            listener = (LocalListListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" debe implementar LocalListListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directoryStack = new Stack<>();
        currentFile = Environment.getExternalStorageDirectory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_dropboxlist, null);

        lv_local = (ListView)rootView.findViewById(R.id.lv_dropbox);
        lv_local.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final File file = adapter.getItem(i);

                //If it's the first element, go back to last folder.
                if(i == 0){
                    if(!directoryStack.isEmpty()){
                        currentFile = directoryStack.pop();
                        adapter.resetList(Arrays.asList(currentFile.listFiles()));
                    }
                }else{
                    //If it's a file, listener
                    if(file.isFile()){
                        long size = file.length();

                        //If it's larger than 1MB, show a confirmation dialog
                        if(size > 1024*1024){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("El archivo pesa " +size/1024L/1024L+"MB. ¿Estás seguro de descargarlo?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //UPLOAD
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                        }else{
                            //UPLOAD
                        }

                    }

                    //If it's a folder, show it's contents
                    if(file.isDirectory()){
                        directoryStack.push(currentFile);
                        currentFile = file;
                        adapter.resetList(Arrays.asList(file.listFiles()));
                    }
                }
            }
        });

        //Load root directory
        adapter = new LocalAdapter(getContext(), Arrays.asList(Environment.getExternalStorageDirectory().listFiles()));
        lv_local.setAdapter(adapter);

        return rootView;
    }


    public File getCurrentFile(){
        return currentFile;
    }

    public void refresh(){
        adapter.resetList(Arrays.asList(currentFile.listFiles()));
    }

}
