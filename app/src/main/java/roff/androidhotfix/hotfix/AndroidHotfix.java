package roff.androidhotfix.hotfix;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import roff.androidhotfix.hotfix.util.AssetUtils;
import roff.androidhotfix.hotfix.util.DexUtils;

/**
 * Created by wuyongbo on 16-4-9.
 */
public class AndroidHotfix {

    private final static String TAG = AndroidHotfix.class.getSimpleName();

    //存放hack.apk中dex文件的目录名
    private static final String HOST_DEX_NAME = "host_dex";
    //assert中apk的文件名
    private static final String HOST_APK = "host.apk";

    //加载完patch后的dex文件的目录名
    private static final String PATCH_OPT_DIR = "patch_opt";

    /**
     * 初始化，加载assert中的host.apk中的dex文件到私有数据目录下
     * @param context
     * @return true 加载成功  false 加载失败
     */
    public static boolean init(Context context)
    {
        //创建存放dex的目录 /data/data/roff.nuwa_hotfix/files/host_dex
        File dexPath = new File(context.getFilesDir(), HOST_DEX_NAME);
        dexPath.mkdir();

        //拷贝host.apk中的dex到上述目录下
        String dexFile;
        try {
            dexFile = AssetUtils.copyAsset(context, HOST_APK, dexPath);
            Log.e(TAG, "copy " + HOST_APK + " success with path " + dexFile);
        } catch (IOException e) {
            Log.e(TAG, "copy " + HOST_APK + " failed!!!");
            e.printStackTrace();

            return false;
        }

        loadPatch(context, dexFile);

        return true;
    }

    /**
     * 加载补丁文件，并注入到host.apk的dex文件中
     * @param context
     * @param hostDex host dex的目录
     */
    public static void loadPatch(Context context, String hostDex)
    {
        if (context == null) {
            Log.e(TAG, "context is null");
            return;
        }
        if (!new File(hostDex).exists()) {
            Log.e(TAG, hostDex + " is null");
            return;
        }

        //创建存放加载完补丁后dex的目录 /data/data/roff.nuwa_hotfix/files/patch_opt
        File dexOptDir = new File(context.getFilesDir(), PATCH_OPT_DIR);
        dexOptDir.mkdir();

        //加载patch，跟hack的dex文件合并elements
        try {
            DexUtils.injectWithHostDex(hostDex, dexOptDir.getAbsolutePath());
            Log.d(TAG, "inject " + hostDex + " success!!!");
        } catch (Exception e) {
            Log.e(TAG, "inject " + hostDex + " failed!!!");
            e.printStackTrace();
        }
    }
}
