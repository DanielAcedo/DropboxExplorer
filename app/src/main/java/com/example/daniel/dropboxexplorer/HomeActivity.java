package com.example.daniel.dropboxexplorer;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.daniel.dropboxexplorer.adapter.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DropboxListFragment.DropboxListListener, LocalListFragment.LocalListListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private DropboxListFragment dropboxListFragment;
    private LocalListFragment localListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        localListFragment = new LocalListFragment();
        dropboxListFragment = new DropboxListFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(localListFragment);
        fragments.add(dropboxListFragment);

        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Local"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Dropbox"), 1);
        viewPager.setPageTransformer(true, new FlipPageViewTransformer());
    }

    @Override
    public void onDownload() {
        localListFragment.refresh();
    }

    public File getCurrentLocalFile(){
        return localListFragment.getCurrentFile();
    }

    @Override
    public void onUpload() {
        dropboxListFragment.refresh();
    }

    @Override
    public String getCurrentDropboxPath() {
        return getCurrentDropboxPath();
    }
}
