package com.nptel.courses.online.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nptel.courses.online.CustomPlaylistRecyclerAdapter;
import com.nptel.courses.online.R;
import com.nptel.courses.online.Utility;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomPlaylistActivity extends AppCompatActivity {

    private ArrayList<Map<String, Object>> moduleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_playlist);

        Utility.setUserActivity("CustomPlaylistActivity");

        moduleArrayList = new ArrayList<>();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Your Playlists");


        final RecyclerView playlistsRecyclerView = findViewById(R.id.playlists_recycler_view);
        final CustomPlaylistRecyclerAdapter customPlaylistRecyclerAdapter = new CustomPlaylistRecyclerAdapter(this, moduleArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        playlistsRecyclerView.setLayoutManager(linearLayoutManager);
        playlistsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playlistsRecyclerView.setAdapter(customPlaylistRecyclerAdapter);


        FirebaseFirestore.getInstance().collection("production/production/users/" + Utility.getUserId() + "/playlist")
                .orderBy("moduleName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                moduleArrayList.add(queryDocumentSnapshot.getData());
                                customPlaylistRecyclerAdapter.notifyItemInserted(moduleArrayList.size() - 1);
                            }
                            if (moduleArrayList.size() < 1) {
                                ((TextView)findViewById(R.id.status)).setText(R.string.you_have_not_created_any_playlist);
                            }
                            else findViewById(R.id.status).setVisibility(View.GONE);
                        }
                    }
                });
    }
}
