package com.nptel.courses.online.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nptel.courses.online.Constants;
import com.nptel.courses.online.MyApplication;
import com.nptel.courses.online.R;
import com.nptel.courses.online.ViewModels.VideoViewModel;
import com.nptel.courses.online.WebViewActivity;
import com.nptel.courses.online.adapters.VideoListAdapter;
import com.nptel.courses.online.entities.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class VideoActivity extends AppCompatActivity {

    private VideoListAdapter videoListAdapter;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        //Playlist ID passed from previous Activity
        MyApplication.playlistId = getIntent().getStringExtra("playlistID");
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(MyApplication.courseName);
        if (MyApplication.playlistId.equals("search"))
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Searched Video");
        RecyclerView videoRecyclerView = findViewById(R.id.video_list_recycler_view);
        videoListAdapter = new VideoListAdapter(this, youTubePlayerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        videoRecyclerView.setLayoutManager(linearLayoutManager);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setAdapter(videoListAdapter);

        VideoViewModel videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);

        videoViewModel.getVideoListLiveData(MyApplication.courseId, MyApplication.playlistId).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoListAdapter.submitList(videos);
                findViewById(R.id.status).setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.removeItem(R.id.change_course);
        menu.removeItem(R.id.playlists);
        menu.removeItem(R.id.favourite);
        menu.removeItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.share_app:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareText = "Get all NPTEL online course videos at one place.\nInstall the app now:\n"
                        + getResources().getString(R.string.play_store_dynamic_link);
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(intent, "Choose"));
                return true;
            case R.id.favourite:
                intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("playlistID", "favourite");
                intent.putExtra("courseID", "favourite");
                finish();
                startActivity(intent);
                return true;
            case R.id.change_course:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("courseID", "00").apply();
                intent = new Intent(VideoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.app_feedback:
                intent = new Intent(VideoActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constants.feedbackForm);
                startActivity(intent);
                return true;
            case R.id.about_us:
                intent = new Intent(VideoActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (youTubePlayerView.getVisibility() == View.GONE)
            return;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (getSupportActionBar() != null)
                getSupportActionBar().hide();
        } else {
            youTubePlayerView.exitFullScreen();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (getSupportActionBar() != null)
                getSupportActionBar().show();
        }
    }
}