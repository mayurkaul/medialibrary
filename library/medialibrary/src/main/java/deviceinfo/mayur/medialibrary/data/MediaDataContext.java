package deviceinfo.mayur.medialibrary.data;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.vision.face.FaceDetector;

import deviceinfo.mayur.medialibrary.util.ThreadPool;

/**
 * Created by mayurkaul on 06/11/17.
 */

public interface MediaDataContext {
    ThreadPool getThreadPool();
    DataManager getDataManager();
    FaceDetector getFaceDetector();
    ImageCacheService getImageCacheService();
    Looper getMainLooper();
    ContentResolver getContentResolver();
    Context getContext();
}
