package com.diezsiete.lscapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.model.Concept;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private Concept[] mDictionaryData;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Concept concept);
    }

    public DictionaryAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.dictionary_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatly = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediatly);

        return new DictionaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {
        String word = mDictionaryData[position].getMeaning();
        holder.listItemDictionaryView.setText(word);
    }

    @Override
    public int getItemCount() {
        if (null == mDictionaryData) return 0;
        return mDictionaryData.length;
    }

    public void setDictionaryData(Concept[] weatherData) {
        mDictionaryData = weatherData;
        notifyDataSetChanged();
    }

    class DictionaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemDictionaryView;

        public DictionaryViewHolder(View itemView) {
            super(itemView);
            listItemDictionaryView = (TextView) itemView.findViewById(R.id.tv_dictionary_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Concept concept = mDictionaryData[adapterPosition];
            mOnClickListener.onListItemClick(concept);
        }
    }
}
