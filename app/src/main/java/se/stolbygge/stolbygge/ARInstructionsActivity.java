package se.stolbygge.stolbygge;

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

public class ARInstructionsActivity extends ARViewActivity {

    /**
     * This has to exist for some reason.
     */
    private IMetaioSDKCallback mCallbackHandler;

    /**
     * The one true light.
     */
    private ILight mDirectionalLight;

    /**
     * Step Geometries (hopefully with animations).
     */
    private ArrayList<IGeometry> step_geometries;

    // These are going to be needed as fields later. Ignore warnings.
    private ArrayList<IGeometry> correct_geometries;
    private ArrayList<IGeometry> aid_geometries;

    /**
     * An integer describing the state ANIMATING.
     */
    public static final int ANIMATING = 1;

    /**
     * An integer describing the state PAUSED.
     */
    public static final int PAUSED = 2;

    /**
     * A Gesture handler.
     */
    private GestureHandlerAndroid mGestureHandler;

    /**
     * Some good to have values for gesture handling.
     *
     * Note: Should probably refactor all this into a class.
     */
    float dx, dy, lastX, lastY;
    Rotation rotation;
    int current;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackHandler = new IMetaioSDKCallback();

        mGestureHandler = new GestureHandlerAndroid(metaioSDK, GestureHandler.GESTURE_DRAG);

        // TODO: Make sure these initial values are OK for all parts.
        dx = 0f;
        dy = 2f;

        rotation = new Rotation(dx, dy, 0);
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
    protected int getGUILayout() {
        return R.layout.activity_ar_instructions;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Animation is paused -- proceed.
        if (step_geometries.get(current).getCoordinateSystemID() == 0) {
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
                    double angleY = dx / velocity;
                    double angleX = dy / velocity;

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

                    step_geometries.get(current).setRotation(localRotation);
                    //modelOnScreen.setRotation(localRotation);

                    break;
            }
        }

        return true;
    }

    @Override
    protected void loadContents() {
        try {
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        } catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        // Setup directional light
        mDirectionalLight = metaioSDK.createLight();
        mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
        mDirectionalLight.setAmbientColor(new Vector3d(0.827f, 0.827f, 0.827f)); // light grey
        mDirectionalLight.setDiffuseColor(new Vector3d(1.000f, 0.980f, 0.804f)); // golden rod

        // Initialize step geometries -- a.k.a. badass animations.
        ArrayList<Step> steps = Store.getInstance().getSteps();
        step_geometries = new ArrayList<>();

        for (Step step : steps) {
            IGeometry step_geometry = loadModel(
                    // TODO: Open this comment up when the models exist
                    "steg_" + step.getStepNr() + "/steg_" + step.getStepNr() + ".zip");
                    //"steg_1/steg_1.zip");

            setGeometryState(step_geometry, ANIMATING);
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

        setTrackingConfiguration("TrackingData_Marker.xml");

        setStep(0, 0);
        current = 0;
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {
        //
    }

    /**
     * Helper method to load geometry.
     *
     * @param pathToModel String
     * @return IGeometry
     */
    private IGeometry loadModel(final String pathToModel) {
        final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);

        return metaioSDK.createGeometry(fModelPath);
    }

    /**
     * Set the current configuration.
     *
     * @param pathToXml final String
     * @return boolean
     */
    private boolean setTrackingConfiguration(final String pathToXml) {
        boolean result = false;

        try {
            final File xmlPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToXml);
            result = metaioSDK.setTrackingConfiguration(xmlPath);
        } catch (Exception e) {
            Log.d("ARInstructionsActivity", "Configuration XML not loaded: " + pathToXml);
        }

        return result;
    }

    /**
     * Swap the current geometry for steps.
     *
     * @param last int
     * @param next int
     */
    public void setStep(int last, int next) {
        // Hide current
        if (last != next) {
            step_geometries.get(last).setVisible(false);
            step_geometries.get(last).stopAnimation();
        }

        // Show next
        step_geometries.get(next).setVisible(true);
        step_geometries.get(next).setDynamicLightingEnabled(true);

        // .. and set it up properly
        setGeometryState(step_geometries.get(next), ANIMATING);
        step_geometries.get(next).startAnimation("Default Take", true);

        current = next;
    }

    /**
     * This method toggles the step animation. It depends on the current coordinate system.
     *
     * @param index int
     */
    public void togglePauseStepAnimation(int index) {
        IGeometry current = step_geometries.get(index);

        if (current.getCoordinateSystemID() == 0) {
            // Animation is paused --  START ANIMATION!
            setGeometryState(current, ANIMATING);
            current.startAnimation("Default Take", true);
        } else {
            // Animation is playing --  PAUSE ANIMATION!
            setGeometryState(current, PAUSED);
            current.pauseAnimation();
        }
    }

    /**
     * Set geometry state. This is a helper function, because it was run a lot of times.
     *
     * IMPORTANT: The order of operations are vital to succes. Do NOT change them.
     *
     * @param that IGeometry
     * @param state int
     */
    private void setGeometryState(IGeometry that, int state) {
        switch (state) {
            case ANIMATING:
                that.setScale(10f);
                that.setRelativeToScreen(IGeometry.ANCHOR_NONE);
                mDirectionalLight.setCoordinateSystemID(1);
                that.setCoordinateSystemID(1);
                break;

            case PAUSED:
                if( current == 0 ) that.setScale(40f);
                else that.setScale(20f);
                that.setRelativeToScreen(IGeometry.ANCHOR_CC);
                mDirectionalLight.setCoordinateSystemID(0);
                that.setCoordinateSystemID(0);
                break;

            default:
                break;
        }
    }
}
