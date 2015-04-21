package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.util.Log;

import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class ARActivity extends ARViewActivity {

    /**
     * kritter model
     */
    private IGeometry mCheckmark = null;

    /**
     * Metaio SDK Callback handler
     */
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

    //Not currently used, needed to extend ARViewActivity
    @Override
    protected int getGUILayout() {
        return R.layout.ar_interaction;
    }

    @Override
    protected void loadContents() {

        //We extract all the assets from the app and let metaio access them.
        try
        {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        }
        catch (IOException e)
        {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: "+e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        // May need a "VIZAID" to find contour
        // Load the model
        mCheckmark = loadModel("check_green.obj");

        // Check that model not null
        if(mCheckmark != null) {
            // Unique id for every object
            mCheckmark.setCoordinateSystemID(1);
        } else {
            Log.d("ARActivity", "Model not loaded!");
        }

        setTrackingConfiguration("chair/Tracking.xml");
    }

    // Sets all parameters needed for tracking
    private boolean setTrackingConfiguration(final String pathToXml) {
        boolean result = false;
        try
        {
            MetaioDebug.log("Hopefully this worked, loaded from:  " + pathToXml);
            final File xmlPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToXml);
            result = metaioSDK.setTrackingConfiguration(xmlPath);
            MetaioDebug.log("Hopefully this worked, loaded from:  " + xmlPath);

        }
        catch (Exception e)
        {
            MetaioDebug.log("SOMETHINGW WENT SHITFACE" + pathToXml);
            return result;
        }
        return result;
    }

    //Not currently used, needed to extend ARViewActivity
    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    // Loads tracking model
    private IGeometry loadModel(final String pathToModel) {

        IGeometry geometry = null;

        try {
            // get the file from given path
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);
            // creates a object of the given file

            //getAssets().

            geometry = metaioSDK.createGeometry(fModelPath);
            Log.d("ARActivity", "in loadModel: loaded!" + fModelPath);
        }catch(Exception e) {
            Log.d("ARActivity", "in loadModel: not loaded" + pathToModel);
            return geometry;
        }
        return geometry;
    }
}
