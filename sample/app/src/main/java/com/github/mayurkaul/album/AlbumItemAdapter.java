package com.github.mayurkaul.album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mayurkaul.R;

import java.util.ArrayList;

import deviceinfo.mayur.medialibrary.data.MediaDataContext;
import deviceinfo.mayur.medialibrary.data.MediaItem;
import deviceinfo.mayur.medialibrary.data.MediaSet;

/**
 * Created by mayurkaul on 06/11/17.
 */

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemViewHolder> {
    private final MediaSet mSet;
    private final ArrayList<MediaItem> mItems;
    private final MediaDataContext mContext;

    AlbumItemAdapter(MediaSet set, MediaDataContext context)
    {
        mSet = set;
        mItems = mSet.getMediaItem(0,mSet.getMediaItemCount());
        mContext = context;
    }

    @Override
    public AlbumItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.albumitem,parent,false);
        return new AlbumItemViewHolder(itemView,mContext);
    }

    @Override
    public void onBindViewHolder(AlbumItemViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewRecycled(AlbumItemViewHolder holder) {
        super.onViewRecycled(holder);
        holder.destroy();
    }
}
