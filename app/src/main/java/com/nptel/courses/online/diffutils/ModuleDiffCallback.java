package com.nptel.courses.online.diffutils;

import com.nptel.courses.online.entities.Module;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ModuleDiffCallback extends DiffUtil.ItemCallback<Module> {

    @Override
    public boolean areItemsTheSame(@NonNull Module oldItem, @NonNull Module newItem) {
        return newItem.getModuleID().equals(oldItem.getModuleID());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Module oldItem, @NonNull Module newItem) {
        return newItem.getModuleName().equals(oldItem.getModuleName()) && newItem.getModuleDescription().equals(oldItem.getModuleDescription());
    }
}
