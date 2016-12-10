package com.example.daniel.dropboxexplorer.Dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by daniel on 10/12/16.
 */

public class Client {
    private static DbxClientV2 clientV2;

    public static DbxClientV2 getClient(String accessToken){

        DbxRequestConfig config = new DbxRequestConfig("AndroidAPI_AD");

        if(clientV2 == null){
            clientV2 = new DbxClientV2(config, accessToken);
        }

        return clientV2;
    }
}
