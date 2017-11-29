package deviceinfo.mayur.medialibrary.data;

/**
 * Created by mayurkaul on 02/11/17.
 */

import android.graphics.RectF;

import deviceinfo.mayur.medialibrary.util.Utils;

public class Face implements Comparable<Face> {
    private String mName;
    private String mPersonId;
    private RectF mPosition;
    public Face(String name, String personId, RectF rect) {
        mName = name;
        mPersonId = personId;
        Utils.assertTrue(mName != null && mPersonId != null && rect != null);
        mPosition = rect;
    }
    public RectF getPosition() {
        return mPosition;
    }
    public int getWidth() {
        return (int)(mPosition.right - mPosition.left);
    }
    public int getHeight() {
        return (int)(mPosition.bottom - mPosition.top);
    }
    public String getName() {
        return mName;
    }
    public String getPersonId() {
        return mPersonId;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Face) {
            Face face = (Face) obj;
            return mPersonId.equals(face.mPersonId);
        }
        return false;
    }
    public int compareTo(Face another) {
        return mName.compareTo(another.mName);
    }
}
