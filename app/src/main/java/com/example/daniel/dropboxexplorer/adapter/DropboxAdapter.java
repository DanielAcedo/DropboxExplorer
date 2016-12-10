package com.example.daniel.dropboxexplorer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.example.daniel.dropboxexplorer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class DropboxAdapter extends ArrayAdapter<Metadata> {

    public DropboxAdapter(Context context, List<Metadata> metadatas){
        super(context, R.layout.metadata_layout, new ArrayList<Metadata>(metadatas));
    }

    public void resetList(List<Metadata> metadatas){
        clear();
        addAll(metadatas);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        MetadataHolder holder = null;

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.metadata_layout, null);
            holder = new MetadataHolder();

            holder.name = (TextView)v.findViewById(R.id.txv_name);
            holder.icon = (ImageView)v.findViewById(R.id.imv_icon);

            v.setTag(holder);
        }else{
            holder = (MetadataHolder)v.getTag();
        }

        holder.name.setText(getItem(position).getName());

        if(getItem(position) instanceof FolderMetadata)
            holder.icon.setImageResource(R.drawable.foldericon);
        if(getItem(position) instanceof FileMetadata)
            holder.icon.setImageResource(R.drawable.documenticon);

        return v;

    }

    public static class MetadataHolder{
        TextView name;
        ImageView icon;
    }
}
