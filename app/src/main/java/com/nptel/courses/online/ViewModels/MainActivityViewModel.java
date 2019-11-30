package com.nptel.courses.online.ViewModels;

import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.repository.CoursesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private LiveData<List<Course>> courseList;

    public LiveData<List<Course>> getCourseList() {
        if (courseList == null)
            courseList = new CoursesRepository().getCourses();
        return courseList;
    }
}
