package com.diezsiete.lscapp.ui.practice;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diezsiete.lscapp.R;

import com.diezsiete.lscapp.ui.widget.DynamicHeightImageView;
import com.diezsiete.lscapp.utils.ImagePlaceHolderDrawableHelper;
import com.diezsiete.lscapp.vo.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuvaraj on 3/4/16.
 */
public class DiscoverImageGridAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Picture> pictureModels = new ArrayList<>();
    private final ClickCallback clickCallback;


    public DiscoverImageGridAdapter(Context context, ClickCallback clickCallback) {
        mContext = context;
        this.clickCallback = clickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_image_grid, null);
        ImageGridViewHolder holder = new ImageGridViewHolder(itemView);
        holder.imageView = (DynamicHeightImageView) itemView.findViewById(R.id.dynamic_height_image_view);
        holder.positionTextView = (TextView) itemView.findViewById(R.id.item_position_view);
        itemView.setTag(holder);

        itemView.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                final int adapterPostion = holder.getAdapterPosition();
                if (adapterPostion != RecyclerView.NO_POSITION) {
                    clickCallback.onClick(adapterPostion);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ImageGridViewHolder vh = (ImageGridViewHolder) viewHolder;
        Picture item = pictureModels.get(position);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) vh.imageView.getLayoutParams();
        float ratio = item.height / item.width;
        rlp.height = (int) (rlp.width * ratio);
        vh.imageView.setLayoutParams(rlp);
        //vh.positionTextView.setText("pos: " + position);
        vh.imageView.setRatio(item.ratio);
        Picasso.with(mContext).load(item.url).placeholder(
            ImagePlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .fit()
                .centerCrop()
                .into(vh.imageView);
    }

    @Override
    public int getItemCount() {
        return pictureModels.size();
    }


    public void addDrawable(Picture pictureModel) {
        float ratio = (float) pictureModel.height / (float) pictureModel.width;
        pictureModel.ratio = ratio;
        this.pictureModels.add(pictureModel);
    }

    public class ImageGridViewHolder extends ViewHolder {
        public DynamicHeightImageView imageView;
        public TextView positionTextView;
        public ImageGridViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ClickCallback {
        void onClick(Integer optionIndex);
    }
}