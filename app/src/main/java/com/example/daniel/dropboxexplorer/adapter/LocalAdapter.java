package com.example.daniel.dropboxexplorer.adapter;

import android.content.Context;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class LocalAdapter extends ArrayAdapter<File> {

    private List<File> fileList;

    public LocalAdapter(Context context, List<File> files){
        super(context, R.layout.metadata_layout);
        this.fileList = new ArrayList<File>(files);
        put(0, new File("..."));
        notifyDataSetChanged();
    }

    @Override
    public void add(File object) {
        fileList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Nullable
    @Override
    public File getItem(int position) {
        return fileList.get(position);
    }

    public void put(int position, File object){
        fileList.add(position, object);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Collection<? extends File> collection) {
        fileList.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void remove(File object) {
        fileList.remove(object);
        notifyDataSetChanged();
    }

    public void resetList(List<File> files){
        fileList.clear();
        fileList.addAll(files);
        put(0, new File("..."));
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

        if(position == 0){
            holder.icon.setImageResource(R.drawable.foldericon);
        }else{
            if(getItem(position).isDirectory())
                holder.icon.setImageResource(R.drawable.foldericon);
            if(getItem(position).isFile())
                holder.icon.setImageResource(R.drawable.documenticon);
        }

        return v;

    }

    public static class MetadataHolder{
        TextView name;
        ImageView icon;
    }
}
