

package deviceinfo.mayur.medialibrary.data;

import java.util.ArrayList;
import java.util.HashMap;

public class PathMatcher {
    public static final int NOT_FOUND = -1;

    private ArrayList<String> mVariables = new ArrayList<String>();
    private Node mRoot = new Node();

    public PathMatcher() {
        mRoot = new Node();
    }

    public void add(String pattern, int kind) {
        String[] segments = Path.split(pattern);
        Node current = mRoot;
        for (int i = 0; i < segments.length; i++) {
            current = current.addChild(segments[i]);
        }
        current.setKind(kind);
    }

    public int match(Path path) {
        String[] segments = path.split();
        mVariables.clear();
        Node current = mRoot;
        for (int i = 0; i < segments.length; i++) {
            Node next = current.getChild(segments[i]);
            if (next == null) {
                next = current.getChild("*");
                if (next != null) {
                    mVariables.add(segments[i]);
                } else {
                    return NOT_FOUND;
                }
            }
            current = next;
        }
        return current.getKind();
    }

    public String getVar(int index) {
        return mVariables.get(index);
    }

    public int getIntVar(int index) {
        return Integer.parseInt(mVariables.get(index));
    }

    public long getLongVar(int index) {
        return Long.parseLong(mVariables.get(index));
    }

    private static class Node {
        private HashMap<String, Node> mMap;
        private int mKind = NOT_FOUND;

        Node addChild(String segment) {
            if (mMap == null) {
                mMap = new HashMap<String, Node>();
            } else {
                Node node = mMap.get(segment);
                if (node != null) return node;
            }

            Node n = new Node();
            mMap.put(segment, n);
            return n;
        }

        Node getChild(String segment) {
            if (mMap == null) return null;
            return mMap.get(segment);
        }

        void setKind(int kind) {
            mKind = kind;
        }

        int getKind() {
            return mKind;
        }
    }
}
