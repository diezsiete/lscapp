package com.diezsiete.lscapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.model.Image;
import com.diezsiete.lscapp.utils.ImagePlaceHolderDrawableHelper;
import com.diezsiete.lscapp.widget.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuvaraj on 3/4/16.
 */
public class ImageGridAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Image> imageModels = new ArrayList<>();
    //Picasso p;

    public ImageGridAdapter(Context context) {
        mContext = context;
       /* Picasso p = new Builder(mContext)
                .memoryCache(new LruCache(24000))
                .build();
        p.setIndicatorsEnabled(true);
        p.setLoggingEnabled(true);
        Picasso.setSingletonInstance(p);*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.image_grid_resizable_grid_item, null);
        ImageGridViewHolder holder = new ImageGridViewHolder(itemView);
        holder.imageView = (DynamicHeightImageView) itemView.findViewById(R.id.dynamic_height_image_view);
        holder.positionTextView = (TextView) itemView.findViewById(R.id.item_position_view);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ImageGridViewHolder vh = (ImageGridViewHolder) viewHolder;
        Image item = imageModels.get(position);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) vh.imageView.getLayoutParams();
        float ratio = item.getHeight() / item.getWidth();
        rlp.height = (int) (rlp.width * ratio);
        vh.imageView.setLayoutParams(rlp);
        vh.positionTextView.setText("pos: " + position);
        vh.imageView.setRatio(item.getRatio());
        Picasso.with(mContext).load(item.getUrl()).placeholder(
            ImagePlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .fit()
                .centerCrop()
                .into(vh.imageView);
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }


    public void addDrawable(Image imageModel) {
        float ratio = (float) imageModel.getHeight() / (float) imageModel.getWidth();
        imageModel.setRatio(ratio);
        this.imageModels.add(imageModel);
    }

    public class ImageGridViewHolder extends ViewHolder {
        public DynamicHeightImageView imageView;
        public TextView positionTextView;
        public ImageGridViewHolder(View itemView) {
            super(itemView);
        }
    }
}
