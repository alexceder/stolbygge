package se.stolbygge.stolbygge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.GestureHandlerAndroid;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.ELIGHT_TYPE;
import com.metaio.sdk.jni.ETRACKING_STATE;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ARInstructionsActivity extends ARViewActivity {

    /**
     * This has to exist for some reason.
     */
    private FoundObjectCallback mCallbackHandler;

    /**
     *
     */
    private Camera camera;

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
    private ArrayList<IGeometry> texturedModels;

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

    private ArrayList<Part> parts;

    /**
     * Some good to have values for gesture handling.
     *
     * Note: Should probably refactor all this into a class.
     */
    private float dx, dy, lastX, lastY;
    private Rotation rotation;
    private Rotation lastRotation;
    private int current;
    private int current_textured;
    private IGeometry current_geometry;

    /**
     * Intro dialog to be shown when activity is loading.
     */
    private ProgressDialog introDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open intro dialog that will be closed when the activity is finished loading
        introDialog = ProgressDialog.show(this, "Welcome to Stolbygge", "Let's get started!", true, false);

        mCallbackHandler = new FoundObjectCallback();

        mGestureHandler = new GestureHandlerAndroid(metaioSDK, GestureHandler.GESTURE_DRAG);

        parts = Store.getInstance().getFindableParts();

        current = 0;
        current_textured = 0;

        // TODO: Make sure these initial values are OK for all parts.
        dx = 0f;
        dy = 2f;

        rotation = new Rotation(dx, dy, 0);
        lastRotation = rotation;
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
        return R.layout.activity_main;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Animation is paused -- proceed.
        if (current_geometry != null &&
                current_geometry.getCoordinateSystemID() == 0) {
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
                    lastRotation = localRotation;

                    current_geometry.setRotation(localRotation);

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

        //
        texturedModels = new ArrayList<>();
        ArrayList<String> modelPaths = new ArrayList<>();
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

            // TODO: Check if needed
            mGestureHandler.addObject(texturedModel, 1);

            texturedModels.add(texturedModel);
        }

        // Initialize step geometries -- a.k.a. badass animations.
        ArrayList<Step> steps = Store.getInstance().getSteps();
        step_geometries = new ArrayList<>();

        for (Step step : steps) {
            IGeometry step_geometry = loadModel(
                    "steg_" + step.getStepNr() + "/steg_" + step.getStepNr() + ".zip");

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

        setTexturedModel(0, 0);

        setFragment(new MainFragment());

        introDialog.dismiss();
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
        current_geometry = step_geometries.get(next);
        current_geometry.setVisible(true);
        current_geometry.setDynamicLightingEnabled(true);

        // .. and set it up properly
        setGeometryState(current_geometry, ANIMATING);
        current_geometry.startAnimation("Default Take", true);

        current = next;
    }

    /**
     * This method toggles the step animation. It depends on the current coordinate system.
     *
     * @param index int
     */
    public void togglePauseStepAnimation(int index) {
        current_geometry = step_geometries.get(index);

        if (current_geometry.getCoordinateSystemID() == 0) {
            // Animation is paused --  START ANIMATION!
            setGeometryState(current_geometry, ANIMATING);
            current_geometry.startAnimation("Default Take", true);
        } else {
            // Animation is playing --  PAUSE ANIMATION!
            setGeometryState(current_geometry, PAUSED);
            current_geometry.pauseAnimation();
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
                if( current == 0 ) that.setScale(50f);
                else that.setScale(20f);
                that.setRelativeToScreen(IGeometry.ANCHOR_CC);
                mDirectionalLight.setCoordinateSystemID(0);
                that.setCoordinateSystemID(0);
                break;

            default:
                break;
        }
    }

    private void setTexturedModel(int last, int next) {
        // Hide last
        texturedModels.get(last).setVisible(false);

        // Show next
        current_geometry = texturedModels.get(next);
        current_geometry.setVisible(true);

        // Set coordinate systems
        current_geometry.setCoordinateSystemID(0);

        // Set rotation
        current_geometry.setRotation(lastRotation);

        // All this updates the activity, there is no explicit reload.
    }

    /**
     * Event handler when overlay button is clicked.
     *
     * @param position int - what to move to
     */
    public void onClickTexturedPosition(int position) {
        int last = current_textured;

        if (position == current_textured) {
            current_textured = texturedModels.size() - 1;
        } else {
            current_textured = position;
        }

        setTexturedModel(last, current_textured);
    }

    /**
     * Event handler when overlay button is clicked.
     *
     * @param position int - what to move to
     */
    public void onClickPosition(int position) {
        int last = current;
        current = position;

        if (current < parts.size() && last < parts.size()) {
            setModel(last, current);
        } else {
            Log.d("ARPartsActivity", "At the end of the list -- show dialog or something!");
        }
    }

    /**
     * Set the current model for identification.
     *
     * @param last int
     * @param next int
     */
    private void setModel(int last, int next) {
        current_geometry = null;

        // Hide current
        if (last != next) {
            correct_geometries.get(last).setVisible(false);
            aid_geometries.get(last).setVisible(false);
        }

        // Show next
        correct_geometries.get(next).setVisible(true);
        aid_geometries.get(next).setVisible(true);

        // Set coordinate systems
        correct_geometries.get(next).setCoordinateSystemID(1);
        aid_geometries.get(next).setCoordinateSystemID(2);

        // Set configuration
        Part part = parts.get(next);
        setTrackingConfiguration(part.getGeometry() + "/" + part.getGeometry() + "_tracking.xml");

        // All this updates the activity, there is no explicit reload.
    }

    /**
     * Handle fragment pushes as per http://stackoverflow.com/a/20667118
     */
    @Override
    public void onBackPressed() {
        // Cop-out!
        onCreateMain();

        // This code is close to working -- but I don't have the time to finish it right now.
        // TODO: Implement this crap.
        /*
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }

        if (fm.getBackStackEntryCount() > 0) {
            String changeTo = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();

            if (fm.getBackStackEntryCount() > 1) {
                fm.popBackStackImmediate();
            }

            // Make sure to set up the changed-to fragment.
            switch (changeTo) {
                case "MainFragment":
                    onCreateMain();
                    break;
                case "PartListFragment":
                    onCreateProductList();
                    break;
                case "ARInstructionsFragment":
                    onCreateARInstructionsView();
                    break;

                default:
                    break;
            }
        } else {
            super.onBackPressed();
        }
        */
    }

    private void setFragment(Fragment fragment) {
        String simple_name = fragment.getClass().getSimpleName();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment, simple_name);
        transaction.addToBackStack(simple_name);
        transaction.commit();
    }

    public void onCreateMain() {
        hideAllGeometries();
        setFragment(new MainFragment());
        setTexturedModel(current_textured, current_textured);

        // Tracking config is a bit stupid, but we need one.
        setTrackingConfiguration("TrackingData_Marker.xml");
    }

    public void onCreateProductList() {
        hideAllGeometries();
        setFragment(new PartListFragment());
        setModel(0, 0);
    }

    public void onCreateARInstructionsView() {
        hideAllGeometries();
        setTrackingConfiguration("TrackingData_Marker.xml");
        setFragment(new ARInstructionsFragment());
        setStep(0, 0);
    }

    private void hideAllGeometries() {
        for (IGeometry geometry : step_geometries) {
            geometry.setVisible(false);
        }

        for (IGeometry geometry : aid_geometries) {
            geometry.setVisible(false);
        }

        for (IGeometry geometry : correct_geometries) {
            geometry.setVisible(false);
        }

        for (IGeometry geometry : texturedModels) {
            geometry.setVisible(false);
        }
    }

    public void onClickNext() {
        int last = current++;

        if (current < parts.size()) {
            setModel(last, current);
        } else {
            Log.d("ARPartsActivity", "At the end of the list -- show dialog or something!");
        }
    }

    /**
     * Everytime the surface changes update the focus of the camera
     *
     * @param width int
     * @param height int
     */
    @Override
    public void onSurfaceChanged(int width, int height) {
        camera = IMetaioSDKAndroid.getCamera(this);
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(params);
    }

    public int getCurrentTextured() {
        return current_textured;
    }

    /**
     * Extended MetaioSDK Callback that handles the event where a 3D model has been found.
     *
     * TODO: Make sure this heavy shit only happens when it needs to.
     */
    final class FoundObjectCallback extends IMetaioSDKCallback {

        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues) {
            if (getFragmentManager().getBackStackEntryCount() > 0 &&
                    getFragmentManager().getBackStackEntryAt(
                            getFragmentManager().getBackStackEntryCount()-1).getName().equals("PartListFragment")) {
                for (int i = 0; i < trackingValues.size(); i++) {
                    final TrackingValues v = trackingValues.get(i);
                    if (v.getCoordinateSystemID() == 1 && v.getState() == ETRACKING_STATE.ETS_FOUND) {
                        ((PartListFragment) getFragmentManager()
                                .findFragmentByTag("PartListFragment")).onFound(current);

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                SystemClock.sleep(2000);
                                onClickNext();
                            }
                        }).start();
                    }
                }
            }
        }
    }
}
