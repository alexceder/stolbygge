package se.stolbygge.stolbygge;

import android.os.Bundle;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;

public class ARActivity extends ARViewActivity {

    // Andriods onCreate, standard functionality
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Standard functionality
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    // Returns the wanted gui layout
    @Override
    protected int getGUILayout() {
        // Returns 0 for now
        return 0;
    }

    // Metios callbackhandler
    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return null;
    }

    // Impoert models and tracking xml's
    @Override
    protected void loadContents() {
    }

    // Convert models to geometries
    @Override
    protected void onGeometryTouched(IGeometry geometry) {
    }
}

