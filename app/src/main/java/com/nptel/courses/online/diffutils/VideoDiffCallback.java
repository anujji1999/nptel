package com.nptel.courses.online.diffutils;

import com.nptel.courses.online.entities.Video;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class VideoDiffCallback extends DiffUtil.ItemCallback<Video> {

    @Override
    public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
        return newItem.getVideoID().equals(oldItem.getVideoID());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
        return newItem.getTitle().equals(oldItem.getTitle()) && newItem.getDescription().equals(oldItem.getDescription());
    }
}
