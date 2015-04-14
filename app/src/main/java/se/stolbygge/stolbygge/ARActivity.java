package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.EENV_MAP_FORMAT;
import com.metaio.tools.io.AssetsManager;

import java.io.IOException;

public class ARActivity extends ARViewActivity {

    /**
     * kritter model
     */
    private IGeometry mKritterModel = null;

    /**
     *
     * Metaio SDK Callback handler
     */
    //private MetaioSDKCallbackHandler mCallbackHandler;
    private IMetaioSDKCallback mCallbackHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackHandler = new IMetaioSDKCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    @Override
    protected int getGUILayout() {
        return 0;
    }

    @Override
    protected void loadContents() {
        // KAN BEHÖVA LADDA IN EN "VIZAID"
        mKritterModel = loadModel("PATH");


    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    private IGeometry loadModel(final String modelPath) {
        return null;
    }
}
