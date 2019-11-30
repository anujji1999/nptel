package com.nptel.courses.online;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nptel.courses.online.activities.VideoActivity;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomPlaylistRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Map<String, Object>> moduleArrayList;
    private String userId;

    public CustomPlaylistRecyclerAdapter(Context context, ArrayList<Map<String, Object>> moduleArrayList) {
        this.context = context;
        this.moduleArrayList = moduleArrayList;
        userId = Utility.getUserId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_playlist_row, parent, false);
        return new VHPlaylist(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final VHPlaylist vh = (VHPlaylist) holder;
        vh.playlistName.setText((String) moduleArrayList.get(vh.getAdapterPosition()).get("moduleName"));

        vh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Custom Playlist", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("playlistID", "playlist" + moduleArrayList.get(vh.getAdapterPosition()).get("moduleID"));
                context.startActivity(intent);
                Utility.setUserActivity("VideoActivity called with ModuleName:" + moduleArrayList.get(vh.getAdapterPosition()).get("moduleName") + "# and moduleID:" + moduleArrayList.get(vh.getAdapterPosition()).get("moduleID"));
            }
        });
        vh.playlistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("playlistID", "playlist" + moduleArrayList.get(vh.getAdapterPosition()).get("moduleID"));
                intent.putExtra("courseID", "playlist");
                context.startActivity(intent);
            }
        });
        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Are you sure you want to delete the playlist: " + moduleArrayList.get(vh.getAdapterPosition()).get("moduleName") + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().document("production/production/users/" + userId + "/playlist/" + moduleArrayList.get(vh.getAdapterPosition()).get("moduleID"))
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            moduleArrayList.remove(vh.getAdapterPosition());
                                            notifyItemRemoved(vh.getAdapterPosition());
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class VHPlaylist extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView playlistName;
        private AppCompatImageView delete;
        private View.OnClickListener listener;

        public VHPlaylist(View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.playlistName);
            delete = itemView.findViewById(R.id.delete);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }
}
