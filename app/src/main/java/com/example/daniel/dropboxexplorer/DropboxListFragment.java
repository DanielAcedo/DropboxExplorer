package com.example.daniel.dropboxexplorer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dropbox.core.v2.files.Metadata;
import com.example.daniel.dropboxexplorer.Dropbox.AuthHelper;
import com.example.daniel.dropboxexplorer.Dropbox.Client;
import com.example.daniel.dropboxexplorer.Dropbox.DropBoxFileHelper;
import com.example.daniel.dropboxexplorer.adapter.DropboxAdapter;

import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class DropboxListFragment extends Fragment {

    private ListView lv_dropbox;
    private DropboxAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_dropboxlist, null);

        lv_dropbox = (ListView)rootView.findViewById(R.id.lv_dropbox);

        DropBoxFileHelper.getFiles(Client.getClient(AuthHelper.getAccessToken()), "", new DropBoxFileHelper.FilesTaskCallback(){
            @Override
            public void onFinish(List<Metadata> metadatas) {
                adapter = new DropboxAdapter(getContext(), metadatas);
                lv_dropbox.setAdapter(adapter);
            }
        });

        return rootView;
    }
}
