package deviceinfo.mayur.medialibrary.data;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.vision.face.FaceDetector;

import deviceinfo.mayur.medialibrary.util.ThreadPool;

/**
 * Created by mayurkaul on 06/11/17.
 */

public abstract class DataCompatActivity extends AppCompatActivity {
    public abstract ThreadPool getThreadPool();
    public abstract DataManager getDataManager();
    public abstract FaceDetector getFaceDetector();
    public abstract ImageCacheService getImageCacheService();
}
