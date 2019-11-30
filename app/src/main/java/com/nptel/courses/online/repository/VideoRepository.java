package com.nptel.courses.online.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nptel.courses.online.Constants;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.firebase.FirebaseQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class VideoRepository {

    public VideoRepository() {
    }

    public LiveData<List<Video>> getVideos(String courseID, String playlistID) {
        CollectionReference reference = FirebaseFirestore
                .getInstance()
                .collection(Constants.firebaseCoursesList)
                .document(courseID)
                .collection("playlist")
                .document(playlistID)
                .collection("videos");
        Query query = reference.orderBy("serialNumber");
        FirebaseQuery firebaseQuery = new FirebaseQuery(query);
        LiveData<List<Video>> videosLiveData;
        videosLiveData = Transformations.map(firebaseQuery, new Function<QuerySnapshot, List<Video>>() {
            @Override
            public List<Video> apply(QuerySnapshot input) {
                List<Video> videoList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : input) {
                    videoList.add(queryDocumentSnapshot.toObject(Video.class));
                }
                return videoList;
            }
        });
        return videosLiveData;
    }
}
