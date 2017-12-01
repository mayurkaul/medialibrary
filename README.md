# Medialibrary

[ ![Download](https://api.bintray.com/packages/mayurkoul2002/Awesome-Android/medialibrary/images/download.svg) ](https://bintray.com/mayurkoul2002/Awesome-Android/medialibrary/_latestVersion)
[![Build Status](https://travis-ci.org/mayurkaul/medialibrary.svg?branch=master)](https://travis-ci.org/mayurkaul/medialibrary)

This library is inspired with the Album structure of the Android Gallery and has abstracted library to get the Albums Data structure for Photos, Videos or All based on the Clustering of Photos based on Album, time, location, Tag, Size, etc.

![Example](img/example1.png)

## Installation ##

add mavencentral to all repositories of your root build.gradle file

```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```
Then in your project's build gradle, add the following dependancy

```gradle
dependencies {
    compile 'com.github.mayurkaul:medialibrary:1.0.1'
}
```
And you are all set !!!

## Usage ##

Extend the main Activity in the following fashion

```java
public class DummyActivity extends DataCompatActivity {
    public DummyActivity() {
        super();
    }

    @Override
    public ThreadPool getThreadPool() {
        return null;
    }

    @Override
    public DataManager getDataManager() {
        return null;
    }

    @Override
    public FaceDetector getFaceDetector() {
        return null;
    }

    @Override
    public ImageCacheService getImageCacheService() {
        return null;
    }
}

```

To instantiate the return types for the above, use the following snippet

```java
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
```
Since the whole methodology of this library is based on paths, you need to get the main rootObject containing the data structure and manipulation logic using the following

```java
mPath = Path.fromString(LocalAlbumSet.PATH_ALL.toString());
```
The options can be
```
LocalAlbumSet.PATH_IMAGE
LocalAlbumSet.PATH_VIDEO
LocalAlbumSet.PATH_ALL
```
