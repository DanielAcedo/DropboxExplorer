package com.example.daniel.dropboxexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.daniel.dropboxexplorer.Dropbox.AuthHelper;

public class AuthActivity extends AppCompatActivity {

    private ImageView btn_auth;
    boolean authRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authRequested = false;

        btn_auth = (ImageView) findViewById(R.id.btn_auth);
        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authRequested = true;
                AuthHelper.startAuth(AuthActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(authRequested){
            if(AuthHelper.getAccessToken() != null){
                Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }
}
