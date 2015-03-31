package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.util.Log;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.tools.io.AssetsManager;

import java.io.IOException;

public class ARActivity extends ARViewActivity {

    @Override
    protected int getGUILayout() {
        Log.d("agil *** ", "in getGUILayout");

        try
        {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        }
        catch (IOException e)
        {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        return 0;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return null;
    }

    @Override
    protected void loadContents() {

    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }
}
