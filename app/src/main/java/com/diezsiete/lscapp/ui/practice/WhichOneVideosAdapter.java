package com.diezsiete.lscapp.ui.practice;

import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.diezsiete.lscapp.databinding.ItemVideoBinding;
import com.diezsiete.lscapp.ui.common.DataBoundListAdapter;
import com.diezsiete.lscapp.ui.common.DataBoundViewHolder;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.utils.signvideoplayer.SignVideoPlayerHelper;
import com.diezsiete.lscapp.utils.signvideoplayer.SignVideoPlayer;

import java.util.List;

public class WhichOneVideosAdapter extends DataBoundListAdapter<List<String>, ItemVideoBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final ClickCallback clickCallback;

    public WhichOneVideosAdapter(DataBindingComponent dataBindingComponent, ClickCallback clickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.clickCallback = clickCallback;
    }

    @Override
    public final DataBoundViewHolder<ItemVideoBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVideoBinding binding = createBinding(parent);
        DataBoundViewHolder<ItemVideoBinding> viewHolder = new DataBoundViewHolder<>(binding);
        binding.getRoot().setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus){
                final int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickCallback.onClick(position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    protected ItemVideoBinding createBinding(ViewGroup parent) {
        ItemVideoBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video,
                        parent, false, dataBindingComponent);
        return binding;
    }

    @Override
    protected void bind(ItemVideoBinding binding, List<String> videoUrl, int position) {
        binding.setOption(videoUrl);
        binding.setPosition(position);
    }


    @Override
    protected boolean areItemsTheSame(List<String> oldItem, List<String> newItem) {
        String oldItemString = TextUtils.join(" ", oldItem);
        String newItemString = TextUtils.join(" ", newItem);
        return oldItemString.equals(newItemString);
    }

    @Override
    protected boolean areContentsTheSame(List<String> oldItem, List<String> newItem) {
        return areItemsTheSame(oldItem, newItem);
    }

    public interface ClickCallback {
        void onClick(Integer optionIndex);
    }
}