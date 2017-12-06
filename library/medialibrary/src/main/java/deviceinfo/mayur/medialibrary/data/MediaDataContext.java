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
    public abstract ThreadPool getThreadPool();
    public abstract DataManager getDataManager();
    public abstract FaceDetector getFaceDetector();
    public abstract ImageCacheService getImageCacheService();
    public abstract Looper getMainLooper();
    public abstract ContentResolver getContentResolver();
    public abstract Context getContext();
}
