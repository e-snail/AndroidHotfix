package roff.androidhotfix.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import roff.androidhotfix.hotfix.AndroidHotfix;

/**
 * Created by wuyongbo on 16-4-3.
 */
public class MainApplication extends Application {

    static final String TAG = MainApplication.class.getSimpleName();

    final String PatchFile = "/hotfix.jar";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        AndroidHotfix.init(this);
//        AndroidHotfix.loadPatch(this, Environment.getExternalStorageDirectory().getAbsolutePath().concat(PatchFile));
    }
}
