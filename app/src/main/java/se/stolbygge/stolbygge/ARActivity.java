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

import java.io.File;
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
        // May need a "VIZAID" to find contour
        // Load the model
        mKritterModel = loadModel("app/src/main/assets/kritter.obj");

        // Check that model not null
        if(mKritterModel != null) {
            // Unique id for every object
            mKritterModel.setCoordinateSystemID(1);
        } else {
            Log.d("ARActivity", "Model not loaded!");
        }

        setTrackingConfiguration("PATHTOTRACK.XML");

    }

    // Sets all parameters needed for tracking
    private boolean setTrackingConfiguration(final String s) {
        return false;
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    // Loads tracking model
    private IGeometry loadModel(final String path) {

        IGeometry geometry = null;

        try {
            // get the file from given path
            final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
            // creates a object of the given file
            geometry = metaioSDK.createGeometry(modelPath);
            Log.d("ARActivity", "in loadModel: loaded!");
        }catch(Exception e) {
            Log.d("ARActivity", "in loadModel: not loaded");
            return geometry;
        }

        return geometry;
    }
}
