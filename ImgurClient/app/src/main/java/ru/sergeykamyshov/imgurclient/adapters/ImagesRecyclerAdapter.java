package ru.sergeykamyshov.imgurclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.sergeykamyshov.imgurclient.R;

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.ImagesHolder> {

    Context mContext;
    private int[] mTestImages = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five,
            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five};

    public ImagesRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        return new ImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesHolder holder, int position) {
        holder.mImageView.setImageResource(mTestImages[position]);
    }

    @Override
    public int getItemCount() {
        return mTestImages.length;
    }

    public class ImagesHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImagesHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img);
        }
    }
}
