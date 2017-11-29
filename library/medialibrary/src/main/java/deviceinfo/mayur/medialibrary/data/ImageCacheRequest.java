

package deviceinfo.mayur.medialibrary.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import deviceinfo.mayur.medialibrary.util.BitmapUtils;
import deviceinfo.mayur.medialibrary.util.ThreadPool;

abstract class ImageCacheRequest implements ThreadPool.Job<Bitmap> {
    private static final String TAG = "ImageCacheRequest";
    protected DataCompatActivity mApplication;
    private Path mPath;
    private int mType;
    private int mTargetSize;
    private long mTimeModified;
    public ImageCacheRequest(DataCompatActivity application,
                             Path path, long timeModified, int type, int targetSize) {
        mApplication = application;
        mPath = path;
        mType = type;
        mTargetSize = targetSize;
        mTimeModified = timeModified;
    }
    private String debugTag() {
        return mPath + "," + mTimeModified + "," +
                ((mType == MediaItem.TYPE_THUMBNAIL) ? "THUMB" :
                        (mType == MediaItem.TYPE_MICROTHUMBNAIL) ? "MICROTHUMB" : "?");
    }
    @Override
    public Bitmap run(ThreadPool.JobContext jc) {
        ImageCacheService cacheService = mApplication.getImageCacheService();
        BytesBufferPool.BytesBuffer buffer = MediaItem.getBytesBufferPool().get();
        try {
            boolean found = cacheService.getImageData(mPath, mTimeModified, mType, buffer);
            if (jc.isCancelled()) return null;
            if (found) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap;
                if (mType == MediaItem.TYPE_MICROTHUMBNAIL) {
                    bitmap = DecodeUtils.decodeUsingPool(jc,
                            buffer.data, buffer.offset, buffer.length, options);
                } else {
                    bitmap = DecodeUtils.decodeUsingPool(jc,
                            buffer.data, buffer.offset, buffer.length, options);
                }
                if (bitmap == null && !jc.isCancelled()) {
                    Log.w(TAG, "decode cached failed " + debugTag());
                }
                return beforeReturningBitmap(bitmap);
            }
        } finally {
            MediaItem.getBytesBufferPool().recycle(buffer);
        }
        Bitmap bitmap = onDecodeOriginal(jc, mType);
        if (jc.isCancelled()) return null;
        if (bitmap == null) {
            Log.w(TAG, "decode orig failed " + debugTag());
            return null;
        }
        if (mType == MediaItem.TYPE_MICROTHUMBNAIL) {
            bitmap = BitmapUtils.resizeAndCropCenter(bitmap, mTargetSize, true);
        } else {
            bitmap = BitmapUtils.resizeDownBySideLength(bitmap, mTargetSize, true);
        }
        if (jc.isCancelled()) return null;
        byte[] array = BitmapUtils.compressToBytes(bitmap);
        if (jc.isCancelled()) return null;
        cacheService.putImageData(mPath, mTimeModified, mType, array);
        return beforeReturningBitmap(bitmap);
    }
    public abstract Bitmap onDecodeOriginal(ThreadPool.JobContext jc, int targetSize);

    public abstract Bitmap beforeReturningBitmap(Bitmap bitmap);
}