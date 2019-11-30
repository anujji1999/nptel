package com.nptel.courses.online.ViewModels;

import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.repository.VideoRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class VideoViewModel extends ViewModel {
    private LiveData<List<Video>> videoListLiveData;

    public LiveData<List<Video>> getVideoListLiveData(String courseId, String playlistId) {
        if (videoListLiveData == null)
            videoListLiveData = new VideoRepository().getVideos(courseId, playlistId);
        return videoListLiveData;
    }
}
