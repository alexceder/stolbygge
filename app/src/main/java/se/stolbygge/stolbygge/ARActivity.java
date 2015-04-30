package se.stolbygge.stolbygge;

import android.app.FragmentManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.jni.ELIGHT_TYPE;
import com.metaio.sdk.jni.ETRACKING_STATE;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ARActivity extends ARViewActivity {

    /**
     * A list of parts and its data.
     */
    private ArrayList<Part> parts;

    /**
     * Store the current model to view.
     */
    private int current;

    /**
     * A list of correct geometries.
     */
    private ArrayList<IGeometry> correct_geometries;

    /**
     * A list of visual aid geometries.
     */
    private ArrayList<IGeometry> aid_geometries;

    /**
     *  Variable to store light
     */
    private ILight mDirectionalLight;

    /**
     *
     */
    private Camera camera;

    /**
     * Metaio SDK Callback handler
     */
    private FoundObjectCallback mCallbackHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackHandler = new FoundObjectCallback();

        // TODO: You can do better!
        parts = new ArrayList<>();
        parts.add(new Part("Plugg", "1", "plugg", "plugg", 2));
        parts.add(new Part("Vänster benpar", "1", "vanster_benpar", "sida", 1));
        parts.add(new Part("Sitts", "1", "sitts", "sits", 1));
        parts.add(new Part("Ryggstöd", "1", "ryggstod", "ryggtopp", 1));
        parts.add(new Part("Höger benpar", "1", "hoger_benpar", "sida", 1));
        parts.add(new Part("Ryggstödsdekoration", "1", "ryggstodsdekoration", "ryggstod", 1));

        current = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadContents() {
        try {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        } catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        correct_geometries = new ArrayList<>();
        aid_geometries = new ArrayList<>();

        // Initialize all geometries and hide them.
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

        setModel(current, current);

        // Setup directional light
        mDirectionalLight = metaioSDK.createLight();
        mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
        mDirectionalLight.setAmbientColor(new Vector3d(0, 0.15f, 0)); // slightly green
        mDirectionalLight.setDiffuseColor(new Vector3d(0.6f, 0.2f, 0)); // orange
        mDirectionalLight.setCoordinateSystemID(2);



    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getGUILayout() {
        return R.layout.part_list_fragment;
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
            Log.d("ARActivity", "Configuration XML not loaded: " + pathToXml);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onGeometryTouched(IGeometry geometry) {
        switch (geometry.getCoordinateSystemID()) {
            case 1:
                // Do something when clicking green geomtry
                break;

            default:
                break;
        }
    }

    /**
     * Event handler when overlay button is clicked.
     */
    public void onClickNext() {
        int last = current++;

        if (current < parts.size()) {
            setModel(last, current);
        } else {
            Log.d("ARActivity", "At the end of the list -- show dialog or something!");
        }
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
            Log.d("ARActivity", "At the end of the list -- show dialog or something!");
        }
    }

    /**
     * Set the current model.
     *
     * @param last int
     * @param next int
     */
    private void setModel(int last, int next) {
        Part part = parts.get(next);

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
        setTrackingConfiguration(part.getGeometry() + "/" + part.getGeometry() + "_tracking.xml");

        // All this updates the activity, there is no explicit reload.
    }

    /**
     * Load a single model from assets and create geometry
     *
     * @param pathToModel String
     * @return IGeometry
     */
    private IGeometry loadModel(final String pathToModel) {
        IGeometry geometry = null;

        try {
            final File fModelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), pathToModel);
            geometry = metaioSDK.createGeometry(fModelPath);
        } catch (Exception e) {
            Log.d("ARActivity", "Model not loaded: " + pathToModel);
        }

        return geometry;
    }

    final class FoundObjectCallback extends IMetaioSDKCallback {

        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues) {
            for (int i = 0; i < trackingValues.size(); i++) {
                final TrackingValues v = trackingValues.get(i);
                if (v.getCoordinateSystemID() == 1 && v.getState() == ETRACKING_STATE.ETS_FOUND) {
                    Log.d("ARActivity", "hittade object!");
                    PartListFragment partListFragment = (PartListFragment) getFragmentManager().findFragmentById(R.id.item_list);
                    partListFragment.onFound(current);
                    onClickNext();
                }
            }
        }
    }

    /**
     *  Everytime the surface changes update the focus of the camera
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(int width, int height)
    {
        camera = IMetaioSDKAndroid.getCamera(this);
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(params);
    }
}
