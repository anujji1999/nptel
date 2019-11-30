package com.nptel.courses.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.ui.utils.ItemClickSupport;
import com.algolia.instantsearch.ui.views.Hits;
import com.nptel.courses.online.activities.VideoActivity;

import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Search Video");
        Searcher searcher = Searcher.create("PHH7NP0EPZ", "cfccffdb5886b41df62aca4411c8a791", "videos");
        InstantSearch helper = new InstantSearch(this, searcher);
        helper.search();
        ((Hits)findViewById(R.id.instantSearchHits))
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, int position, View v) {
                        JSONObject jsonObject = ((Hits)findViewById(R.id.instantSearchHits)).get(position);
                        String videoId = jsonObject.optString("objectID");
                        if (videoId.isEmpty())
                            Toast.makeText(SearchActivity.this, "This video is corrupted", Toast.LENGTH_SHORT).show();
                        else {
                            Intent intent  = new Intent(SearchActivity.this, VideoActivity.class);
                            intent.putExtra("videoId", videoId);
                            intent.putExtra("playlistId", "search");
                            startActivity(intent);
                        }
                    }
                });*/
    }
}
