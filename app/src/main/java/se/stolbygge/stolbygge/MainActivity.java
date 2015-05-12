package se.stolbygge.stolbygge;

import android.app.Activity;
import android.content.Intent;
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
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ARViewActivity {

    //kritter model
    private IGeometry modelOnScreen;
    private GestureHandlerAndroid mGestureHandler;
    private int mGestureMask;
    private ILight mDirectionalLight;
    private float lastX, lastY;
    float dx = 0, dy = 2; // initial model rotation
    Rotation rotation;
    private Rotation lastRotation;

    private ArrayList<IGeometry> texturedModels;
    private ArrayList<String> modelPaths;
    private int current;

    //Metaio SDK Callback handler
    private IMetaioSDKCallback mCallbackHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelOnScreen = null;
        mCallbackHandler = new IMetaioSDKCallback();
        mGestureMask = GestureHandler.GESTURE_DRAG;
        mGestureHandler = new GestureHandlerAndroid(metaioSDK, mGestureMask);
        current = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
        mGestureHandler.delete();
        mGestureHandler = null;
    }

    /*
        Function that is used on static models in AR view.
        Rotates model on touch. Only gesture drag accepted.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = event.getX() - dx;
                lastY = event.getY() - dy;

                break;

            case MotionEvent.ACTION_MOVE:
                // set delta touch position
                dx = event.getX() - lastX;
                dy = event.getY() - lastY;

                // convert pixels to radians, arbitrary velocity number
                int velocity = 10;

                // get rotation angles
                double angleY = ((dx / velocity));
                double angleX = (dy / velocity);

                // multiply rotation matrices to get local model rotation
                Rotation localRotation;
                Rotation rot1 = new Rotation();
                Rotation rot2 = new Rotation();
                rot1.setFromEulerAngleDegrees(new Vector3d((float) angleX, 0, 0));
                rot2.setFromEulerAngleDegrees(new Vector3d(0, (float) angleY, 0));
                localRotation = rot1.multiply(rot2);

                // multiply with inverse of initial rotation to rotate in global coordinate system
                localRotation = rotation.inverse().multiply(localRotation);
                float[] mat = new float[9];
                localRotation.getRotationMatrix(mat);
                texturedModels.get(current).setRotation(localRotation);
                lastRotation = localRotation;

                break;
        }

        return true;
    }

    @Override
    //Not currently used, needed to extend ARViewActivity
    protected int getGUILayout() {
        return R.layout.activity_main;
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

        // Sets the directional light
        mDirectionalLight = metaioSDK.createLight();
        mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
        mDirectionalLight.setAmbientColor(new Vector3d(0.827f, 0.827f, 0.827f)); // Light Grey
        mDirectionalLight.setDiffuseColor(new Vector3d(0.855f, 0.647f, 0.125f)); // Goldenrod
        mDirectionalLight.setCoordinateSystemID(0);

        rotation = new Rotation(dx, dy, 0);
        lastRotation = rotation;

        texturedModels = new ArrayList<>();
        modelPaths = new ArrayList<>();
        modelPaths.add("Textur_leftleg");
        modelPaths.add("Textur_rightleg");
        modelPaths.add("Textur_sits");
        modelPaths.add("Textur_ryggtopp");
        modelPaths.add("Textur_ryggstod");
        modelPaths.add("Textur_unselected");

        for (String modelPath : modelPaths) {
            IGeometry texturedModel = loadModel("stol/" + modelPath + "/" + modelPath + ".obj");

            texturedModel.setVisible(false);
            texturedModel.setRelativeToScreen(IGeometry.ANCHOR_CC);
            texturedModel.setScale(25f);
            texturedModel.setRotation(rotation, true);
            texturedModel.setDynamicLightingEnabled(true);

            mGestureHandler.addObject(texturedModel, 1);

            texturedModels.add(texturedModel);

        }

        setModel(current, current);
    }

    // Loads tracking model, returns an IGeometry
    private IGeometry loadModel(final String pathToModel) {
        IGeometry geometry = null;

        try {
            // get the file from given path
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);
            geometry = metaioSDK.createGeometry(fModelPath);
            Log.d("ARPartsActivity", "in loadModel: loaded!" + fModelPath);
        } catch (Exception e) {
            Log.d("ARPartsActivity", "in loadModel: not loaded" + pathToModel);
            return geometry;
        }
        return geometry;
    }

    // Needs to exist to make ARViewActivity happy.
    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    private void setModel(int last, int next) {
        // Hide current
        if (last != next) {
            texturedModels.get(last).setVisible(false);
        }

        // Show next
        texturedModels.get(next).setVisible(true);

        // Set coordinate systems
        texturedModels.get(next).setCoordinateSystemID(0);

        // Set rotation
        texturedModels.get(next).setRotation(lastRotation);

        // All this updates the activity, there is no explicit reload.
    }

    // Is called when the button "Product List" is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // The operation is to call ARPartsActivity.class that shows and handles the product list.
    public void onCreateProductList() {
        Intent intent = new Intent(this, ARPartsActivity.class);
        startActivity(intent);
    }

    public void onCreateARInstructionsView() {
        Intent intent = new Intent(this, ARInstructionsActivity.class);
        startActivity(intent);
    }

    public void highlightPart(int position) {
        Log.d("***" , "changing " + position);
        int last = current;
        if(position == current) {
            current = texturedModels.size() - 1;
        } else {
            current = position;
        }
        setModel(last, current);
    }
}
