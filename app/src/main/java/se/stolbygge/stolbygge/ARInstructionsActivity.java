package se.stolbygge.stolbygge;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.GestureHandlerAndroid;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.ELIGHT_TYPE;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.Vector2d;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class ARInstructionsActivity extends ARViewActivity {

    //step one model
    private IGeometry modelOnScreen;
    private GestureHandlerAndroid mGestureHandler;
    private int mGestureMask;
    private ILight mDirectionalLight;

    //Metaio SDK Callback handler
    private IMetaioSDKCallback mCallbackHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelOnScreen = null;
        mCallbackHandler = new IMetaioSDKCallback();
        mGestureMask = GestureHandler.GESTURE_ALL;
        mGestureHandler = new GestureHandlerAndroid(metaioSDK, mGestureMask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
        mGestureHandler.delete();
        mGestureHandler = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);

        mGestureHandler.setRotationAxis('y');
        mGestureHandler.onTouch(v, event);

        return true;
    }

    @Override
    //Not currently used, needed to extend ARViewActivity
    protected int getGUILayout() {
        return R.layout.activity_ar_instructions;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    @Override
    protected void loadContents() {
        //We extract all the assets from the app and let metaio access them.
        try {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        }
        catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        setStepView(1);
    }

    // Loads tracking model, returns an IGeometry
    private IGeometry loadModel(final String pathToModel) {
        IGeometry geometry = null;

        try {
            // get the file from given path
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);
            geometry = metaioSDK.createGeometry(fModelPath);
            Log.d("ARActivity", "in loadModel: loaded!" + fModelPath);
        } catch(Exception e) {
            Log.d("ARActivity", "in loadModel: not loaded" + pathToModel);
            return geometry;
        }
        return geometry;
    }

    // Needs to exist to make ARViewActivity happy.
    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    private void loadStepModel(String img) {
        //Load model
        modelOnScreen = loadModel(img);

        // Check that model not null
        if(modelOnScreen != null) {
            // Sets the directional light
            mDirectionalLight = metaioSDK.createLight();
            mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
            mDirectionalLight.setAmbientColor(new Vector3d(0.827f, 0.827f, 0.827f)); // Light Grey
            mDirectionalLight.setDiffuseColor(new Vector3d(1.000f, 0.980f, 0.804f)); // Goldenrod
            mDirectionalLight.setCoordinateSystemID(0);

            modelOnScreen.setCoordinateSystemID(mDirectionalLight.getCoordinateSystemID());
            // Anchors the model in front of camera
            modelOnScreen.setRelativeToScreen(IGeometry.ANCHOR_CC);
            // Should be scaled according to size of device instead.
            modelOnScreen.setScale(new Vector3d(1.6f, 1.6f, 1.6f) );
            modelOnScreen.setRotation(new Rotation(0.0f, 2.0f, 0.0f),true);
            modelOnScreen.setDynamicLightingEnabled(true);

            mGestureHandler.addObject(modelOnScreen, 1);

        } else {
            Log.d("*Step view*", "Model not loaded!");
        }
    }

    public void setStepView(int position) {
        String img = "steg_" + Integer.toString(position) + ".obj"; //TODO change when there are new cool things here
        loadStepModel(img);
    }
}
