package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.util.Log;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class ModelView extends ARViewActivity{

    /**
     * kritter model
     */
    private IGeometry model3D = null;

    //Metaio SDK Callback handler
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
    //Not currently used, needed to extend ARViewActivity
    protected int getGUILayout() {
        return 0;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
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
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        // Load the model
        model3D = loadModel("star.obj");

        // Check that model not null
        if(model3D != null) {
            // Unique id for every object
            model3D.setCoordinateSystemID(1);
        } else {
            Log.d("*ModelView*", "Model not loaded!");
        }

    }

    // Loads tracking model
    private IGeometry loadModel(final String pathToModel) {

        IGeometry geometry = null;

        try {
            // get the file from given path
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);

            geometry = metaioSDK.createGeometry(fModelPath);
            Log.d("ARActivity", "in loadModel: loaded!" + fModelPath);
        }catch(Exception e) {
            Log.d("ARActivity", "in loadModel: not loaded" + pathToModel);
            return geometry;
        }
        return geometry;
    }

    //TODO: For handling on touch events
    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

}
