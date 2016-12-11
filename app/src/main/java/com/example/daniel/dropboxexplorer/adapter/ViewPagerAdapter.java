package com.example.daniel.dropboxexplorer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daniel.dropboxexplorer.DropboxListFragment;
import com.example.daniel.dropboxexplorer.LocalListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 10/12/16.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = new ArrayList<>(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = fragments.get(0);
                break;
            case 1:
                fragment = fragments.get(1);
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
