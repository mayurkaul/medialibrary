package deviceinfo.mayur.com.deviceinfo.album;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;
import java.util.List;

import deviceinfo.mayur.com.deviceinfo.R;
import deviceinfo.mayur.medialibrary.data.ContentListener;
import deviceinfo.mayur.medialibrary.data.DataCompatActivity;
import deviceinfo.mayur.medialibrary.data.DataManager;
import deviceinfo.mayur.medialibrary.data.ImageCacheService;
import deviceinfo.mayur.medialibrary.data.LocalAlbumSet;
import deviceinfo.mayur.medialibrary.data.MediaSet;
import deviceinfo.mayur.medialibrary.data.Path;
import deviceinfo.mayur.medialibrary.util.FilterUtils;
import deviceinfo.mayur.medialibrary.util.ThreadPool;

public class AlbumActivity extends DataCompatActivity implements AlbumSetAdapter.OnAlbumItemClickListener {

    private ThreadPool mThreadPool;
    private RecyclerView mRecyclerView;
    private DataManager mDataManager;
    private AlbumSetAdapter mAdapter;
    private AlbumItemAdapter mItemAdapter;
    private ImageCacheService mImageCacheService;
    private Object mLock = new Object();
    private MediaSet mRootObject;
    private Path mPath;
    private String bucketType,filterType;

    private AdapterView.OnItemSelectedListener mMainItemSelected, mFilterItemSelected;
    private FaceDetector mFaceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mRecyclerView = findViewById(R.id.albumList);
        setupListeners();
        setupSpinner();
        setupAlbum(mPath);
    }

    private void setupListeners() {
        mPath = Path.fromString(LocalAlbumSet.PATH_ALL.toString());
        bucketType = "All";
        mMainItemSelected = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                getPathByBucketType(item);
                changePathByFilter(filterType);
                setupAlbum(mPath);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        filterType = "CLUSTER BY ALBUM";
        mFilterItemSelected = new AdapterView.OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                getPathByBucketType(bucketType);
                changePathByFilter(item);
                setupAlbum(mPath);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void changePathByFilter(String item) {
        String basePath = mPath.toString();
        String newPath = null;
        filterType = item;
        if (item.equals("CLUSTER BY ALBUM")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_ALBUM);
        } else if (item.equals("CLUSTER BY TIME")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_TIME);
        } else if (item.equals("CLUSTER BY LOCATION")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_LOCATION);
        } else if (item.equals("CLUSTER BY TAG")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_TAG);
        } else if (item.equals("CLUSTER BY SIZE")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_SIZE);
        } else if (item.equals("CLUSTER BY FACE")) {
            newPath = FilterUtils.switchClusterPath(basePath, FilterUtils.CLUSTER_BY_FACE);
        }
        if(newPath != null)
        {
            mPath = Path.fromString(newPath);
        }
    }

    private void getPathByBucketType(String item) {
        bucketType = item;
        if (item.equals("Photos")) {
            mPath = Path.fromString(LocalAlbumSet.PATH_IMAGE.toString());
        } else if (item.equals("Videos")) {
            mPath = Path.fromString(LocalAlbumSet.PATH_VIDEO.toString());
        } else {
            mPath = Path.fromString(LocalAlbumSet.PATH_ALL.toString());
        }
    }

    private void setupAlbum(Path path) {
        mPath = path;
        mRootObject = getDataManager().getMediaSet(path);
        mRootObject.addContentListener(new ContentListener() {
            @Override
            public void onContentDirty() {
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter = new AlbumSetAdapter(mRootObject, this,this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        mRootObject.reload();
    }

    private void setupAlbumItems(Path path)
    {
        mRootObject = getDataManager().getMediaSet(path);
        mRootObject.addContentListener(new ContentListener() {
            @Override
            public void onContentDirty() {
                mAdapter.notifyDataSetChanged();
            }
        });
        mItemAdapter = new AlbumItemAdapter(mRootObject,this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mItemAdapter);
        mRootObject.reload();
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(mMainItemSelected);

        List<String> categories = new ArrayList<>();

        categories.add("All");
        categories.add("Photos");
        categories.add("Videos");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
        spinnerFilter.setOnItemSelectedListener(mFilterItemSelected);

        List<String> filter = new ArrayList<>();

        filter.add("CLUSTER BY ALBUM");
        filter.add("CLUSTER BY TIME");
        filter.add("CLUSTER BY LOCATION");
        filter.add("CLUSTER BY TAG");
        filter.add("CLUSTER BY SIZE");
        filter.add("CLUSTER BY FACE");


        ArrayAdapter<String> dataAdapterFilter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filter);
        dataAdapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(dataAdapterFilter);

    }

    @Override
    public synchronized DataManager getDataManager() {
        if (mDataManager == null) {
            mDataManager = new DataManager(this);
            mDataManager.initializeSourceMap();
        }
        return mDataManager;
    }

    @Override
    public FaceDetector getFaceDetector() {
        if (mFaceDetector == null) {
            mFaceDetector = new FaceDetector.Builder(this)
                    .setTrackingEnabled(false)
                    .build();
        }
        return mFaceDetector;
    }


    @Override
    public ImageCacheService getImageCacheService() {
        // This method may block on file I/O so a dedicated lock is needed here.
        synchronized (mLock) {
            if (mImageCacheService == null) {
                mImageCacheService = new ImageCacheService(this);
            }
            return mImageCacheService;
        }
    }

    @Override
    public synchronized ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPool();
        }
        return mThreadPool;
    }

    @Override
    public void onAlbumItemClicked(MediaSet item) {
        setupAlbumItems(item.getPath());
    }

    @Override
    public void onBackPressed() {
        if(mRecyclerView.getAdapter().equals(mItemAdapter))
        {
            getPathByBucketType(bucketType);
            changePathByFilter(filterType);
            setupAlbum(mPath);
        }
        else {
            super.onBackPressed();
        }
    }
}
