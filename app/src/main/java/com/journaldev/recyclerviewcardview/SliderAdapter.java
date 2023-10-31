package com.journaldev.recyclerviewcardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.slider.Slider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    static private List<SlideItem> slideItemList;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SlideItem> slideItemList, ViewPager2 viewPager2) {
        this.slideItemList = slideItemList;
        this.viewPager2 = viewPager2;
    }

    static public SlideItem getCurrentImageResource(int currentPosition){
        return slideItemList.get(currentPosition);
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_container, parent,  false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slideItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return slideItemList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageslide);
        }
        void setImage(SlideItem slideItem){
            imageView.setImageResource(slideItem.getImage());
        }
    }
}
