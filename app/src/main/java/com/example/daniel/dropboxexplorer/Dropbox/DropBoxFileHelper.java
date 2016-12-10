package com.example.daniel.dropboxexplorer.Dropbox;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class DropBoxFileHelper {

    public interface FilesTaskCallback{
        void onFinish(List<Metadata> metadatas);
    }

    public static void getFiles(DbxClientV2 client, String path, FilesTaskCallback callback){
        new getFilesTask(client, path, callback).execute();
    }

    private static class getFilesTask extends AsyncTask<String, List<Metadata>, List<Metadata>>{

        private DbxClientV2 client;
        private FilesTaskCallback callback;
        private String path;

        public getFilesTask(DbxClientV2 client, String path, FilesTaskCallback callback){
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
            callback.onFinish(metadatas);
        }
    }
}
