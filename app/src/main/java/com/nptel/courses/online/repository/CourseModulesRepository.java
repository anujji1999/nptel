package com.nptel.courses.online.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nptel.courses.online.Constants;
import com.nptel.courses.online.entities.Module;
import com.nptel.courses.online.firebase.FirebaseQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class CourseModulesRepository {

    public CourseModulesRepository() {
    }

    public LiveData<List<Module>> getModules(String courseID) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(Constants.firebaseCoursesList + "/" + courseID + "/" + "playlist");
        Query query = reference.orderBy("serialNumber");
        FirebaseQuery firebaseQuery = new FirebaseQuery(query);
        LiveData<List<Module>> modulesLiveData;
        modulesLiveData = Transformations.map(firebaseQuery, new Function<QuerySnapshot, List<Module>>() {
            @Override
            public List<Module> apply(QuerySnapshot input) {
                List<Module> moduleList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : input) {
                    moduleList.add(queryDocumentSnapshot.toObject(Module.class));
                }
                return moduleList;
            }
        });
        return modulesLiveData;
    }
}
