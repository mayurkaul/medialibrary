package deviceinfo.mayur.medialibrary.util;

/**
 * Created by mayurkaul on 02/11/17.
 */

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class IdentityCache<K, V> {
    private final HashMap<K, Entry<K, V>> mWeakMap =
            new HashMap<K, Entry<K, V>>();
    private ReferenceQueue<V> mQueue = new ReferenceQueue<V>();
    public IdentityCache() {
    }
    private static class Entry<K, V> extends WeakReference<V> {
        K mKey;
        public Entry(K key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            mKey = key;
        }
    }
    private void cleanUpWeakMap() {
        Entry<K, V> entry = (Entry<K, V>) mQueue.poll();
        while (entry != null) {
            mWeakMap.remove(entry.mKey);
            entry = (Entry<K, V>) mQueue.poll();
        }
    }
    public synchronized V put(K key, V value) {
        cleanUpWeakMap();
        Entry<K, V> entry = mWeakMap.put(
                key, new Entry<K, V>(key, value, mQueue));
        return entry == null ? null : entry.get();
    }
    public synchronized V get(K key) {
        cleanUpWeakMap();
        Entry<K, V> entry = mWeakMap.get(key);
        return entry == null ? null : entry.get();
    }
    // This is currently unused.
    /*
    public synchronized void clear() {
        mWeakMap.clear();
        mQueue = new ReferenceQueue<V>();
    }
    */
    // This is for debugging only
    public synchronized ArrayList<K> keys() {
        Set<K> set = mWeakMap.keySet();
        ArrayList<K> result = new ArrayList<K>(set);
        return result;
    }
}