package com.nptel.courses.online.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.firebase.FirebaseQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class CoursesRepository {

    public CoursesRepository() {
    }

    public LiveData<List<Course>> getCourses() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("/production/production/youtubeCourses");
        Query query = reference.orderBy("courseName");
        FirebaseQuery firebaseQuery = new FirebaseQuery(query);
        LiveData<List<Course>> coursesList;
        coursesList = Transformations.map(firebaseQuery, new Function<QuerySnapshot, List<Course>>() {
            @Override
            public List<Course> apply(QuerySnapshot input) {
                List<Course> courseList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : input) {
                    courseList.add(queryDocumentSnapshot.toObject(Course.class));
                }
                return courseList;
            }
        });
        return coursesList;
    }
}
