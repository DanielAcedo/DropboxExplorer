package com.example.daniel.dropboxexplorer.Dropbox;

import android.os.AsyncTask;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.File;
import java.io.FileInputStream;
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

    public interface UploadFileTaskCallback{
        void onSuccess(String filename);

        void onError(String message);
    }

    public static void getFiles(DbxClientV2 client, String path, GetFilesTaskCallback callback){
        new getFilesTask(client, path, callback).execute();
    }

    public static void downloadFile(DbxClientV2 client, String path, File to, DownloadFileTaskCallback callback){
        new downloadFile(client, path, to, callback).execute();
    }

    public static void uploadFile(DbxClientV2 client, String targetPath, File file, UploadFileTaskCallback callback){
        new uploadFile(client, targetPath, file, callback).execute();
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
        private File to;
        private String resultPath;
        private String errorMessage;

        public downloadFile(DbxClientV2 client, String path, File to, DownloadFileTaskCallback callback){
            this.path = path;
            this.client = client;
            this.callback = callback;
            this.to = to;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean ok = false;

            try {
                String filename = client.files().getMetadata(path).getName();
                File file = new File(to, filename);
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

    private static class uploadFile extends AsyncTask<String, Boolean, Boolean>{
        private UploadFileTaskCallback callback;
        private String targetPath;
        private DbxClientV2 client;
        private File file;
        private String resultPath;
        private String errorMessage;

        public uploadFile(DbxClientV2 client, String targetPath, File file, UploadFileTaskCallback callback){
            this.targetPath = targetPath;
            this.client = client;
            this.callback = callback;
            this.file = file;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean ok = false;

            try {
                resultPath = client.files().upload(targetPath+"/"+file.getName()).uploadAndFinish(new FileInputStream(file)).getPathLower();
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
