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
import java.util.ArrayList;

public class ARInstructionsActivity extends ARViewActivity {

    //step one model
    //private IGeometry modelOnScreen;
    private GestureHandlerAndroid mGestureHandler;
    private int mGestureMask;
    private ILight mDirectionalLight;

    //Metaio SDK Callback handler
    private IMetaioSDKCallback mCallbackHandler;

    // Light
    private ILight mDirectionalLight;

    // Gemoetries
    private ArrayList<IGeometry> step_geometries;
    private ArrayList<IGeometry> correct_geometries;
    private ArrayList<IGeometry> aid_geometries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //modelOnScreen = null;
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
        } catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        // Initialize step geometries -- a.k.a. badass animations.
        ArrayList<Step> steps = Store.getInstance().getSteps();
        step_geometries = new ArrayList<>();

        for (Step step : steps) {
            IGeometry step_geometry = loadModel(
                    //"step_" + step.getStepNr() + "/step_" + step.getStepNr() + ".zip");
                    "steg_" + step.getStepNr() + ".obj");

            // TODO: Start animation
            //step_geometry.startAnimation("Default Take", true);

            // TODO: Figure out if we suffer performance loss having dynamic lights enabled
            step_geometry.setDynamicLightingEnabled(true);
            step_geometry.setVisible(false);

            step_geometries.add(step_geometry);
        }

        // Initialize all part geometries and hide them.
        ArrayList<Part> parts = Store.getInstance().getFindableParts();
        correct_geometries = new ArrayList<>();
        aid_geometries = new ArrayList<>();

        for (Part part : parts) {
            IGeometry correct = loadModel(part.getGeometry() + "/" + part.getGeometry() + "_correct.obj");
            IGeometry aid = loadModel(part.getGeometry() + "/" + part.getGeometry() + "_surface.obj");

            correct.setDynamicLightingEnabled(true);
            aid.setDynamicLightingEnabled(true);

            correct.setVisible(false);
            aid.setVisible(false);

            correct_geometries.add(correct);
            aid_geometries.add(aid);
        }

        // Setup directional light
        mDirectionalLight = metaioSDK.createLight();
        mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
        mDirectionalLight.setAmbientColor(new Vector3d(0, 0.15f, 0)); // slightly green
        mDirectionalLight.setDiffuseColor(new Vector3d(0.6f, 0.2f, 0)); // orange
        mDirectionalLight.setCoordinateSystemID(0);

        setStep(0, 0);
    }

    private IGeometry loadModel(final String pathToModel) {
        IGeometry geometry = null;

        try {
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);
            geometry = metaioSDK.createGeometry(fModelPath);
            Log.d("ARActivity", "in loadModel: loaded!" + fModelPath);
        } catch(Exception e) {
            Log.d("ARActivity", "in loadModel: not loaded" + pathToModel);
            return geometry;
        }
        return geometry;
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {
    }

    public void setStep(int last, int next) {
        // Hide current
        if (last != next) {
            step_geometries.get(last).setVisible(false);
        }

        // Show next
        step_geometries.get(next).setVisible(true);

        // WHAT IS THIS!?
        step_geometries.get(next).setRelativeToScreen(IGeometry.ANCHOR_CC);

        // Set coordinate systems
        step_geometries.get(next).setCoordinateSystemID(0);
    }
}
