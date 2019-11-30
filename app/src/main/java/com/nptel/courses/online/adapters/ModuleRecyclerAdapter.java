package com.nptel.courses.online.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nptel.courses.online.activities.VideoActivity;
import com.nptel.courses.online.databinding.ModuleListItemBinding;
import com.nptel.courses.online.diffutils.ModuleDiffCallback;
import com.nptel.courses.online.entities.Module;

// RecyclerView adapter for list of modules(playlist).
public class ModuleRecyclerAdapter extends ListAdapter<Module, ModuleRecyclerAdapter.ModuleViewHolder> {

    public ModuleRecyclerAdapter() {
        super(new ModuleDiffCallback());
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModuleViewHolder(ModuleListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ModuleListItemBinding binding;

        ModuleViewHolder(ModuleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Module module) {
            binding.setModule(module);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), VideoActivity.class);
            intent.putExtra("playlistID", binding.getModule().getModuleID());
            intent.putExtra("courseID", binding.getModule().getCourseID());
            view.getContext().startActivity(intent);
        }
    }
}
