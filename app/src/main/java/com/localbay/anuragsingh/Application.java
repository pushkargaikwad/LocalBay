package com.localbay.anuragsingh;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by anuragsingh on 01/01/16.
 */
public class Application extends android.app.Application {
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "hlxA8wrxJT8fqG78acmLhziyOZPMRyOBQnpx7Hpb", "awvF4mjE4M3ycQb76AclKdXE78qJaRxB3fVlz87Z");
    }
}
