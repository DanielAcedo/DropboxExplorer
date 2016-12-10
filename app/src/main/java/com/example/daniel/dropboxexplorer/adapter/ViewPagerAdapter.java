package com.example.daniel.dropboxexplorer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daniel.dropboxexplorer.DropboxListFragment;

/**
 * Created by daniel on 10/12/16.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new DropboxListFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
