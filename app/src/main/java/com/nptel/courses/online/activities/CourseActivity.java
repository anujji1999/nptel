package com.nptel.courses.online.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.nptel.courses.online.Constants;
import com.nptel.courses.online.MyApplication;
import com.nptel.courses.online.R;
import com.nptel.courses.online.SearchActivity;
import com.nptel.courses.online.ViewModels.ModuleViewModel;
import com.nptel.courses.online.WebViewActivity;
import com.nptel.courses.online.adapters.ModuleRecyclerAdapter;
import com.nptel.courses.online.databinding.ActivityCourseBinding;
import com.nptel.courses.online.entities.Module;

import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private ModuleRecyclerAdapter moduleRecyclerAdapter;
    private ActivityCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course);

        // Getting course ID from previous activity
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(MyApplication.courseName);


        moduleRecyclerAdapter = new ModuleRecyclerAdapter();
        binding.playlistsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.playlistsRecyclerView.setAdapter(moduleRecyclerAdapter);

        ModuleViewModel moduleViewModel = ViewModelProviders.of(this).get(ModuleViewModel.class);
        moduleViewModel.getModuleList(MyApplication.courseId).observe(this, new Observer<List<Module>>() {
            @Override
            public void onChanged(List<Module> modules) {
                moduleRecyclerAdapter.submitList(modules);
                if (modules == null)
                    binding.setAvailable(false);
                else binding.setAvailable(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.removeItem(R.id.playlists);
        menu.removeItem(R.id.favourite);
        menu.removeItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.search:
                intent = new Intent(CourseActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.share_app:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareText = "Get all NPTEL online course videos at one place.\nInstall the app now:\n"
                        + getResources().getString(R.string.play_store_dynamic_link);
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(intent, "Choose"));
                return true;
            case R.id.playlists:
                intent = new Intent(getApplicationContext(), CustomPlaylistActivity.class);
                startActivity(intent);
                return true;
            case R.id.favourite:
                intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("playlistID", "favourite");
                intent.putExtra("courseID", "favourite");
                startActivity(intent);
                return true;
            case R.id.change_course:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("courseID", "00").apply();
                MyApplication.courseId = "00";
                intent = new Intent(CourseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.app_feedback:
                intent = new Intent(CourseActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constants.feedbackForm);
                startActivity(intent);
                return true;
            case R.id.appreciate:
                intent = new Intent(CourseActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constants.appreciatePaymentPage);
                startActivity(intent);
                return true;
            case R.id.about_us:
                intent = new Intent(CourseActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}