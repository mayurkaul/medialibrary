package deviceinfo.mayur.com.deviceinfo.album;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import deviceinfo.mayur.com.deviceinfo.R;
import deviceinfo.mayur.medialibrary.data.DataCompatActivity;
import deviceinfo.mayur.medialibrary.data.MediaItem;
import deviceinfo.mayur.medialibrary.util.Future;
import deviceinfo.mayur.medialibrary.util.FutureListener;

/**
 * Created by mayurkaul on 06/11/17.
 */


class AlbumItemViewHolder extends RecyclerView.ViewHolder {
    private final DataCompatActivity mContext;
    private final TextView mText;
    public final CardView mParentView;
    private ImageView mImage;
    private Future<Bitmap> mfuture;

    public AlbumItemViewHolder(View itemView, DataCompatActivity context) {
        super(itemView);
        mImage = itemView.findViewById(R.id.imageView);
        mText = itemView.findViewById(R.id.textDetails);
        mParentView = itemView.findViewById(R.id.parentView);
        mContext = context;
    }

    public void bind(final MediaItem item)
    {
        mImage.setImageResource(R.drawable.ic_gallery);
        mfuture = mContext.getThreadPool().submit(item.requestImage(MediaItem.TYPE_THUMBNAIL), new FutureListener<Bitmap>() {
            @Override
            public void onFutureDone(final Future<Bitmap> future) {
                mImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mImage.setImageBitmap(future.get());
                        mText.setText(item.getName());
                    }
                });
            }
        });

        mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void destroy(){
        if(mfuture!=null)
        {
            mfuture.cancel();
        }
        mfuture = null;
    }
}
