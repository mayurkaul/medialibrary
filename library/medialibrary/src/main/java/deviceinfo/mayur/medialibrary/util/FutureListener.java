package deviceinfo.mayur.medialibrary.util;

/**
 * Created by mayurkaul on 02/11/17.
 */

public interface FutureListener<T> {
    public void onFutureDone(Future<T> future);
}
