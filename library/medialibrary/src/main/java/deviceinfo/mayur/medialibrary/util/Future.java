package deviceinfo.mayur.medialibrary.util;

/**
 * Created by mayurkaul on 02/11/17.
 */

public interface Future<T> {
    public void cancel();
    public boolean isCancelled();
    public boolean isDone();
    public T get();
    public void waitDone();
}
