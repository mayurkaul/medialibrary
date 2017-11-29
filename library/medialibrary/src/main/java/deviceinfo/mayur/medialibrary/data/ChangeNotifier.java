

package deviceinfo.mayur.medialibrary.data;

import android.net.Uri;

import java.util.concurrent.atomic.AtomicBoolean;


// This handles change notification for media sets.
public class ChangeNotifier {

    private MediaSet mMediaSet;
    private AtomicBoolean mContentDirty = new AtomicBoolean(true);

    public ChangeNotifier(MediaSet set, Uri uri, DataCompatActivity application) {
        mMediaSet = set;
        application.getDataManager().registerChangeNotifier(uri, this);
    }

    public ChangeNotifier(MediaSet set, Uri[] uris, DataCompatActivity application) {
        mMediaSet = set;
        for (int i = 0; i < uris.length; i++) {
            application.getDataManager().registerChangeNotifier(uris[i], this);
        }
    }

    // Returns the dirty flag and clear it.
    public boolean isDirty() {
        return mContentDirty.compareAndSet(true, false);
    }

    public void fakeChange() {
        onChange(false);
    }

    protected void onChange(boolean selfChange) {
        if (mContentDirty.compareAndSet(false, true)) {
            mMediaSet.notifyContentChanged();
        }
    }
}