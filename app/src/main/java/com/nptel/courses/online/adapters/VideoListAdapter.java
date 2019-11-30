package com.nptel.courses.online.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nptel.courses.online.databinding.VideoListItemBinding;
import com.nptel.courses.online.diffutils.VideoDiffCallback;
import com.nptel.courses.online.entities.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

// RecyclerView adapter for list of videos.
public class VideoListAdapter extends ListAdapter<Video, VideoListAdapter.VideoViewHolder> {

    private AppCompatActivity context;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private int currentPlayingVideoPosition;

    public VideoListAdapter(AppCompatActivity context, YouTubePlayerView youTubePlayerView) {
        super(new VideoDiffCallback());
        this.context = context;
        currentPlayingVideoPosition = -1;
        this.youTubePlayerView = youTubePlayerView;
        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                VideoListAdapter.this.youTubePlayer = youTubePlayer;
            }
        });
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, final int position) {
        holder.bind(getItem(position));
        holder.itemView.setTag(holder.getAdapterPosition());

    }

    private void setCurrentPlayingVideoPosition(int adapterPosition) {
        int previousPlayingVideoPosition = currentPlayingVideoPosition;
        currentPlayingVideoPosition = adapterPosition;
        if (previousPlayingVideoPosition != currentPlayingVideoPosition) {
            if (previousPlayingVideoPosition >= 0)
                notifyItemChanged(previousPlayingVideoPosition, 0);
            notifyItemChanged(currentPlayingVideoPosition, 1);
        }
    }

    private void playVideo(int adapterPosition) {
        setCurrentPlayingVideoPosition(adapterPosition);
        youTubePlayer.loadVideo(getItem(adapterPosition).getVideoID(), 0);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private VideoListItemBinding binding;

        VideoViewHolder(final VideoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (youTubePlayerView != null)
                        youTubePlayerView.setVisibility(View.VISIBLE);
                    playVideo(getAdapterPosition());
                }
            });
            binding.shareVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://youtu.be/" + binding.getVideo().getVideoID();
                    Toast.makeText(context, "creating share link...", Toast.LENGTH_SHORT).show();
                    FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse(url))
                            .setDomainUriPrefix("https://nptel.page.link")
                            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                            .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                            .addOnCompleteListener(context, new OnCompleteListener<ShortDynamicLink>() {
                                @Override
                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        Uri shortLink = task.getResult().getShortLink();
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_TEXT, shortLink != null ? shortLink.toString() : "error");
                                        context.startActivity(Intent.createChooser(intent, "Share URL"));
                                    } else {
                                        if (task.getException() != null)
                                            Crashlytics.log("Dynamic link error::" + task.getException().getMessage());
                                    }
                                }
                            });
                }
            });
        }
        void bind(Video video) {
            binding.setVideo(video);
        }
    }
}
