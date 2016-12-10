package com.example.daniel.dropboxexplorer.Dropbox;

import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class DropBoxFileHelper {

    public interface GetFilesTaskCallback {
        void onFinish(List<Metadata> metadatas, String lastPath);
    }

    public interface DownloadFileTaskCallback {
        void onSuccess(String filename);

        void onError(String message);
    }

    public static void getFiles(DbxClientV2 client, String path, GetFilesTaskCallback callback){
        new getFilesTask(client, path, callback).execute();
    }

    public static void downloadFile(DbxClientV2 client, String path, DownloadFileTaskCallback callback){
        new downloadFile(client, path, callback).execute();
    }

    private static class getFilesTask extends AsyncTask<String, List<Metadata>, List<Metadata>>{

        private DbxClientV2 client;
        private GetFilesTaskCallback callback;
        private String path;

        public getFilesTask(DbxClientV2 client, String path, GetFilesTaskCallback callback){
            this.client = client;
            this.callback = callback;
            this.path = path;
        }

        @Override
        protected List<Metadata> doInBackground(String... strings) {
            List<Metadata> metadatas = new ArrayList<>();

            try {
                ListFolderResult result = client.files().listFolder(path);

                metadatas.addAll(result.getEntries());


            } catch (DbxException e) {
                e.printStackTrace();
            }

            return metadatas;
        }

        @Override
        protected void onPostExecute(List<Metadata> metadatas) {
            super.onPostExecute(metadatas);
            callback.onFinish(metadatas, path);
        }
    }

    private static class downloadFile extends AsyncTask<String, Boolean, Boolean>{
        private DownloadFileTaskCallback callback;
        private String path;
        private DbxClientV2 client;
        private String resultPath;
        private String errorMessage;

        public downloadFile(DbxClientV2 client, String path, DownloadFileTaskCallback callback){
            this.path = path;
            this.client = client;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean ok = false;

            try {
                String filename = client.files().getMetadata(path).getName();
                File file = new File(Environment.getExternalStorageDirectory(), filename);
                DbxDownloader downloader = client.files().download(path);
                downloader.download(new FileOutputStream(file));
                downloader.close();
                resultPath = file.getPath();
                ok = true;

            } catch (DbxException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                ok = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                ok = false;
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                ok = false;
            }

            return ok;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            if(ok){
                callback.onSuccess(resultPath);
            }else{
                callback.onError(errorMessage);
            }
        }
    }
}
