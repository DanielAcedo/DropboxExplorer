package com.example.daniel.dropboxexplorer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.example.daniel.dropboxexplorer.Dropbox.AuthHelper;
import com.example.daniel.dropboxexplorer.Dropbox.Client;
import com.example.daniel.dropboxexplorer.Dropbox.DropBoxFileHelper;
import com.example.daniel.dropboxexplorer.adapter.DropboxAdapter;

import java.io.File;
import java.util.List;
import java.util.Stack;

/**
 * Created by daniel on 10/12/16.
 */

public class DropboxListFragment extends Fragment {

    public interface DropboxListListener {
        void onDownload();

        File getCurrentLocalFile();
    }

    private ListView lv_dropbox;
    private DropboxAdapter adapter;

    DropboxListListener listener;

    private Stack<String> directoryStack;

    private String currentPath;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            listener = (DropboxListListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" debe implementar DropboxListListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directoryStack = new Stack<>();
        currentPath = "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_dropboxlist, null);

        lv_dropbox = (ListView)rootView.findViewById(R.id.lv_dropbox);
        lv_dropbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final Metadata metadata = adapter.getItem(i);

                //If it's the first element, go back to last folder.
                if(i == 0){
                    if(!directoryStack.isEmpty()){
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setTitle("Cargando...");
                        dialog.show();

                        DropBoxFileHelper.getFiles(Client.getClient(AuthHelper.getAccessToken()), directoryStack.pop(), new DropBoxFileHelper.GetFilesTaskCallback() {
                            @Override
                            public void onFinish(List<Metadata> metadatas, String lastPath) {
                                dialog.dismiss();
                                adapter.resetList(metadatas);
                                currentPath = lastPath;
                            }
                        });
                    }
                }else{
                    //If it's a file, listener
                    if(metadata instanceof FileMetadata){
                        long size = ((FileMetadata)metadata).getSize();

                        //If it's larger than 1MB, show a confirmation dialog
                        if(size > 1024*1024){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("El archivo pesa " +size/1024L/1024L+"MB. ¿Estás seguro de descargarlo?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            downloadFile(metadata.getPathLower());
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                        }else{
                            downloadFile(metadata.getPathLower());
                        }

                    }

                    //If it's a folder, show it's contents
                    if(metadata instanceof FolderMetadata){
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setTitle("Cargando...");
                        dialog.show();

                        DropBoxFileHelper.getFiles(Client.getClient(AuthHelper.getAccessToken()), metadata.getPathLower(), new DropBoxFileHelper.GetFilesTaskCallback() {
                            @Override
                            public void onFinish(List<Metadata> metadatas, String lastPath) {
                                dialog.dismiss();
                                directoryStack.push(currentPath);
                                currentPath = lastPath;
                                adapter.resetList(metadatas);
                            }
                        });
                    }
                }
            }
        });

        //Load root directory
        DropBoxFileHelper.getFiles(Client.getClient(AuthHelper.getAccessToken()), "", new DropBoxFileHelper.GetFilesTaskCallback(){
            @Override
            public void onFinish(List<Metadata> metadatas, String lastPath) {
                adapter = new DropboxAdapter(getContext(), metadatas);
                lv_dropbox.setAdapter(adapter);
                currentPath = lastPath;
            }
        });

        return rootView;
    }

    /**
     * Downloads a file in Dropbox based on the path
     * @param metaPath Path on the file in Dropbox
     */
    private void downloadFile(String metaPath){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Descargando...");
        dialog.show();

        DropBoxFileHelper.downloadFile(Client.getClient(AuthHelper.getAccessToken()), metaPath, listener.getCurrentLocalFile(), new DropBoxFileHelper.DownloadFileTaskCallback() {
            @Override
            public void onSuccess(String filename) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Exito al descargar fichero: "+filename, Toast.LENGTH_SHORT).show();
                listener.onDownload();
            }

            @Override
            public void onError(String message) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error al descargar fichero: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh(){
        DropBoxFileHelper.getFiles(Client.getClient(AuthHelper.getAccessToken()), currentPath, new DropBoxFileHelper.GetFilesTaskCallback() {
            @Override
            public void onFinish(List<Metadata> metadatas, String lastPath) {
                directoryStack.push(currentPath);
                currentPath = lastPath;
                adapter.resetList(metadatas);
            }
        });
    }

    public String getCurrentPath(){
        return currentPath;
    }
}
