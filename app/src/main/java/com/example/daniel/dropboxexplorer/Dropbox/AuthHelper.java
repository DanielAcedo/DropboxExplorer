package com.example.daniel.dropboxexplorer.Dropbox;

import android.content.Context;


import com.dropbox.core.android.Auth;
import com.dropbox.core.json.JsonReader;
import com.example.daniel.dropboxexplorer.R;

/**
 * Created by daniel on 10/12/16.
 */

public class AuthHelper {

    public static void startAuth(Context context) {
        Auth.startOAuth2Authentication(context, context.getResources().getString(R.string.app_key));
    }

    public static String getAccessToken(){
        return Auth.getOAuth2Token();

    }
}
