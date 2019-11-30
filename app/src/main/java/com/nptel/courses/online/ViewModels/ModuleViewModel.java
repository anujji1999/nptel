package com.nptel.courses.online.ViewModels;

import com.nptel.courses.online.entities.Module;
import com.nptel.courses.online.repository.CourseModulesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ModuleViewModel extends ViewModel {

    private LiveData<List<Module>> moduleList;

    public LiveData<List<Module>> getModuleList(String courseId) {
        if (moduleList == null) {
            moduleList = new CourseModulesRepository().getModules(courseId);
        }
        return moduleList;
    }
}
