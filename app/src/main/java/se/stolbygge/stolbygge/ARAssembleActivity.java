package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.util.Log;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class ARAssembleActivity extends ARViewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void loadContents() {
        try {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        } catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }

        IGeometry correct = loadModel("kritter.obj");
        correct.setVisible(true);
        correct.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
        correct.setTranslation(new Vector3d(-150, -150, 0));

        // Set coordinate systems
        correct.setCoordinateSystemID(1);

        // Set configuration
        setTrackingConfiguration("TrackingData_Marker.xml");
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }

    @Override
    protected int getGUILayout() {
        return 0;
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return null;
    }

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
}
