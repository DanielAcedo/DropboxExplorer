package com.example.daniel.dropboxexplorer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Collection;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class DropboxAdapter extends ArrayAdapter<Metadata> {

    private List<Metadata> metadataList;

    public DropboxAdapter(Context context, List<Metadata> metadatas){
        super(context, R.layout.metadata_layout);
        this.metadataList = new ArrayList<Metadata>(metadatas);
        put(0, new FolderMetadata("...","1"));
        notifyDataSetChanged();
    }

    @Override
    public void add(Metadata object) {
        metadataList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return metadataList.size();
    }

    @Nullable
    @Override
    public Metadata getItem(int position) {
        return metadataList.get(position);
    }

    public void put(int position, Metadata object){
        metadataList.add(position, object);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Collection<? extends Metadata> collection) {
        metadataList.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Metadata object) {
        metadataList.remove(object);
        notifyDataSetChanged();
    }

    public void resetList(List<Metadata> metadatas){
        metadataList.clear();
        metadataList.addAll(metadatas);
        put(0, new FolderMetadata("...","1"));
        notifyDataSetChanged();
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
